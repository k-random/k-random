/*
 * The MIT License
 *
 *   Copyright (c) 2023, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package io.github.krandom;

import static io.github.krandom.FieldPredicates.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.krandom.api.ContextAwareRandomizer;
import io.github.krandom.api.RandomizerContext;
import io.github.krandom.beans.*;
import io.github.krandom.beans.exclusion.A;
import io.github.krandom.beans.exclusion.B;
import io.github.krandom.beans.exclusion.C;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FieldExclusionTest {

  private KRandom kRandom;

  @BeforeEach
  void setUp() {
    kRandom = new KRandom();
  }

  @Test
  void excludedFieldsShouldNotBePopulated() {
    // given
    KRandomParameters parameters = new KRandomParameters().excludeField(named("name"));
    kRandom = new KRandom(parameters);

    // when
    Person person = kRandom.nextObject(Person.class);

    // then
    assertThat(person).isNotNull();
    assertThat(person.getName()).isNull();
  }

  @Test
  void excludedFieldsUsingSkipRandomizerShouldNotBePopulated() {
    // given
    KRandomParameters parameters =
        new KRandomParameters()
            .excludeField(named("name").and(ofType(String.class)).and(inClass(Human.class)));
    kRandom = new KRandom(parameters);

    // when
    Person person = kRandom.nextObject(Person.class);

    // then
    assertThat(person).isNotNull();
    assertThat(person.getName()).isNull();
  }

  @Test
  void excludedFieldsUsingFieldDefinitionShouldNotBePopulated() {
    // given
    KRandomParameters parameters = new KRandomParameters().excludeField(named("name"));
    kRandom = new KRandom(parameters);

    // when
    Person person = kRandom.nextObject(Person.class);

    // then
    assertThat(person).isNotNull();
    assertThat(person.getAddress()).isNotNull();
    assertThat(person.getAddress().getStreet()).isNotNull();

    // person.name and street.name should be null
    assertThat(person.getName()).isNull();
    assertThat(person.getAddress().getStreet().getName()).isNull();
  }

  @Test
  void excludedDottedFieldsShouldNotBePopulated() {
    // given
    KRandomParameters parameters =
        new KRandomParameters().excludeField(named("name").and(inClass(Street.class)));
    kRandom = new KRandom(parameters);

    // when
    Person person = kRandom.nextObject(Person.class);

    // then
    assertThat(person).isNotNull();
    assertThat(person.getAddress()).isNotNull();
    assertThat(person.getAddress().getStreet()).isNotNull();
    assertThat(person.getAddress().getStreet().getName()).isNull();
  }

  @Test
  void fieldsExcludedWithAnnotationShouldNotBePopulated() {
    Person person = kRandom.nextObject(Person.class);

    assertThat(person).isNotNull();
    assertThat(person.getExcluded()).isNull();
  }

  @Test
  @SuppressWarnings("deprecation")
  void fieldsExcludedWithAnnotationViaFieldDefinitionShouldNotBePopulated() {
    // given
    KRandomParameters parameters =
        new KRandomParameters().excludeField(isAnnotatedWith(Deprecated.class));
    kRandom = new KRandom(parameters);

    // when
    Website website = kRandom.nextObject(Website.class);

    // then
    assertThat(website).isNotNull();
    assertThat(website.getProvider()).isNull();
  }

  @Test
  void fieldsExcludedFromTypeViaFieldDefinitionShouldNotBePopulated() {
    // given
    KRandomParameters parameters = new KRandomParameters().excludeField(inClass(Address.class));
    kRandom = new KRandom(parameters);

    // when
    Person person = kRandom.nextObject(Person.class);

    // then
    assertThat(person).isNotNull();
    assertThat(person.getAddress()).isNotNull();
    // all fields declared in class Address must be null
    assertThat(person.getAddress().getCity()).isNull();
    assertThat(person.getAddress().getStreet()).isNull();
    assertThat(person.getAddress().getZipCode()).isNull();
    assertThat(person.getAddress().getCountry()).isNull();
  }

  @Test
  void testFirstLevelExclusion() {
    KRandomParameters parameters =
        new KRandomParameters().excludeField(named("b2").and(inClass(C.class)));
    kRandom = new KRandom(parameters);

    C c = kRandom.nextObject(C.class);

    assertThat(c).isNotNull();

    // B1 and its "children" must not be null
    assertThat(c.getB1()).isNotNull();
    assertThat(c.getB1().getA1()).isNotNull();
    assertThat(c.getB1().getA1().getS1()).isNotNull();
    assertThat(c.getB1().getA1().getS2()).isNotNull();
    assertThat(c.getB1().getA2()).isNotNull();
    assertThat(c.getB1().getA2().getS1()).isNotNull();
    assertThat(c.getB1().getA2().getS2()).isNotNull();

    // B2 must be null
    assertThat(c.getB2()).isNull();
  }

  @Test
  void testSecondLevelExclusion() { // goal: exclude only b2.a2
    KRandomParameters parameters =
        new KRandomParameters()
            .randomize(
                ofType(A.class).and(inClass(B.class)),
                new ContextAwareRandomizer<A>() {
                  private RandomizerContext context;

                  @Override
                  public void setRandomizerContext(RandomizerContext context) {
                    this.context = context;
                  }

                  @Override
                  public A getRandomValue() {
                    if (context.getCurrentField().equals("b2.a2")) {
                      return null;
                    }
                    return new KRandom().nextObject(A.class);
                  }
                });
    kRandom = new KRandom(parameters);
    C c = kRandom.nextObject(C.class);

    assertThat(c).isNotNull();

    // B1 and its "children" must not be null
    assertThat(c.getB1()).isNotNull();
    assertThat(c.getB1().getA1()).isNotNull();
    assertThat(c.getB1().getA1().getS1()).isNotNull();
    assertThat(c.getB1().getA1().getS2()).isNotNull();
    assertThat(c.getB1().getA2()).isNotNull();
    assertThat(c.getB1().getA2().getS1()).isNotNull();
    assertThat(c.getB1().getA2().getS2()).isNotNull();

    // Only B2.A2 must be null
    assertThat(c.getB2()).isNotNull();
    assertThat(c.getB2().getA1()).isNotNull();
    assertThat(c.getB2().getA1().getS1()).isNotNull();
    assertThat(c.getB2().getA1().getS2()).isNotNull();
    assertThat(c.getB2().getA2()).isNull();
  }

  @Test
  void testThirdLevelExclusion() { // goal: exclude only b2.a2.s2
    KRandomParameters parameters =
        new KRandomParameters()
            .randomize(
                FieldPredicates.named("s2").and(inClass(A.class)),
                new ContextAwareRandomizer<String>() {
                  private RandomizerContext context;

                  @Override
                  public void setRandomizerContext(RandomizerContext context) {
                    this.context = context;
                  }

                  @Override
                  public String getRandomValue() {
                    if (context.getCurrentField().equals("b2.a2.s2")) {
                      return null;
                    }
                    return new KRandom().nextObject(String.class);
                  }
                });
    kRandom = new KRandom(parameters);
    C c = kRandom.nextObject(C.class);

    // B1 and its "children" must not be null
    assertThat(c.getB1()).isNotNull();
    assertThat(c.getB1().getA1()).isNotNull();
    assertThat(c.getB1().getA1().getS1()).isNotNull();
    assertThat(c.getB1().getA1().getS2()).isNotNull();
    assertThat(c.getB1().getA2()).isNotNull();
    assertThat(c.getB1().getA2().getS1()).isNotNull();
    assertThat(c.getB1().getA2().getS2()).isNotNull();

    // Only B2.A2.S2 must be null
    assertThat(c.getB2()).isNotNull();
    assertThat(c.getB2().getA1()).isNotNull();
    assertThat(c.getB2().getA1().getS1()).isNotNull();
    assertThat(c.getB2().getA1().getS2()).isNotNull();
    assertThat(c.getB2().getA2().getS1()).isNotNull();
    assertThat(c.getB2().getA2().getS2()).isNull();
  }

  @Test
  void testFirstLevelCollectionExclusion() {
    KRandomParameters parameters =
        new KRandomParameters().excludeField(FieldPredicates.named("b3").and(inClass(C.class)));
    kRandom = new KRandom(parameters);

    C c = kRandom.nextObject(C.class);

    assertThat(c).isNotNull();

    // B1 and its "children" must not be null
    assertThat(c.getB1()).isNotNull();
    assertThat(c.getB1().getA1()).isNotNull();
    assertThat(c.getB1().getA1().getS1()).isNotNull();
    assertThat(c.getB1().getA1().getS2()).isNotNull();
    assertThat(c.getB1().getA2()).isNotNull();
    assertThat(c.getB1().getA2().getS1()).isNotNull();
    assertThat(c.getB1().getA2().getS2()).isNotNull();

    // B1 and its "children" must not be null
    assertThat(c.getB2()).isNotNull();
    assertThat(c.getB2().getA1()).isNotNull();
    assertThat(c.getB2().getA1().getS1()).isNotNull();
    assertThat(c.getB2().getA1().getS2()).isNotNull();
    assertThat(c.getB2().getA2()).isNotNull();
    assertThat(c.getB2().getA2().getS1()).isNotNull();
    assertThat(c.getB2().getA2().getS2()).isNotNull();

    // B3 must be null
    assertThat(c.getB3()).isNull();
  }

  @Test
  void testSecondLevelCollectionExclusion() { // b3.a2 does not make sense, should be ignored
    KRandomParameters parameters =
        new KRandomParameters()
            .randomize(
                FieldPredicates.named("a2").and(inClass(B.class)),
                new ContextAwareRandomizer<A>() {
                  private RandomizerContext context;

                  @Override
                  public void setRandomizerContext(RandomizerContext context) {
                    this.context = context;
                  }

                  @Override
                  public A getRandomValue() {
                    if (context.getCurrentField().equals("b3.a2")) {
                      return null;
                    }
                    return new KRandom().nextObject(A.class);
                  }
                });
    kRandom = new KRandom(parameters);

    C c = kRandom.nextObject(C.class);

    assertThat(c).isNotNull();

    // B1 and its "children" must not be null
    assertThat(c.getB1()).isNotNull();
    assertThat(c.getB1().getA1()).isNotNull();
    assertThat(c.getB1().getA1().getS1()).isNotNull();
    assertThat(c.getB1().getA1().getS2()).isNotNull();
    assertThat(c.getB1().getA2()).isNotNull();
    assertThat(c.getB1().getA2().getS1()).isNotNull();
    assertThat(c.getB1().getA2().getS2()).isNotNull();

    // B2 and its "children" must not be null
    assertThat(c.getB2()).isNotNull();
    assertThat(c.getB2().getA1()).isNotNull();
    assertThat(c.getB2().getA1().getS1()).isNotNull();
    assertThat(c.getB2().getA1().getS2()).isNotNull();
    assertThat(c.getB2().getA2()).isNotNull();
    assertThat(c.getB2().getA2().getS1()).isNotNull();
    assertThat(c.getB2().getA2().getS2()).isNotNull();

    // B3 must not be null
    assertThat(c.getB3()).isNotNull();
  }

  @Test
  void whenFieldIsExcluded_thenItsInlineInitializationShouldBeUsedAsIs() {
    // given
    KRandomParameters parameters =
        new KRandomParameters()
            .excludeField(
                named("myList")
                    .and(ofType(List.class))
                    .and(inClass(InlineInitializationBean.class)));
    kRandom = new KRandom(parameters);

    // when
    InlineInitializationBean bean = kRandom.nextObject(InlineInitializationBean.class);

    // then
    assertThat(bean).isNotNull();
    assertThat(bean.getMyList()).isEmpty();
  }

  @Test
  void
      whenFieldIsExcluded_thenItsInlineInitializationShouldBeUsedAsIs_EvenIfBeanHasNoPublicConstructor() {
    // given
    KRandomParameters parameters =
        new KRandomParameters()
            .excludeField(
                named("myList")
                    .and(ofType(List.class))
                    .and(inClass(InlineInitializationBeanPrivateConstructor.class)));
    kRandom = new KRandom(parameters);

    // when
    InlineInitializationBeanPrivateConstructor bean =
        kRandom.nextObject(InlineInitializationBeanPrivateConstructor.class);

    // then
    assertThat(bean.getMyList()).isEmpty();
  }

  @Test
  void fieldsExcludedWithOneModifierShouldNotBePopulated() {
    // given
    KRandomParameters parameters =
        new KRandomParameters().excludeField(hasModifiers(Modifier.TRANSIENT));
    kRandom = new KRandom(parameters);

    // when
    Person person = kRandom.nextObject(Person.class);

    // then
    assertThat(person).isNotNull();
    assertThat(person.getEmail()).isNull();
  }

  @Test
  void fieldsExcludedWithTwoModifiersShouldNotBePopulated() {
    // given
    KRandomParameters parameters =
        new KRandomParameters().excludeField(hasModifiers(Modifier.TRANSIENT | Modifier.PROTECTED));
    kRandom = new KRandom(parameters);

    // when
    Person person = kRandom.nextObject(Person.class);

    // then
    assertThat(person).isNotNull();
    assertThat(person.getEmail()).isNull();
  }

  @Test
  void fieldsExcludedWithTwoModifiersShouldBePopulatedIfOneModifierIsNotFit() {
    // given
    KRandomParameters parameters =
        new KRandomParameters().excludeField(hasModifiers(Modifier.TRANSIENT | Modifier.PUBLIC));
    kRandom = new KRandom(parameters);

    // when
    Person person = kRandom.nextObject(Person.class);

    // then
    assertThat(person).isNotNull();
    assertThat(person.getEmail()).isNotNull();
  }

  public static class InlineInitializationBean {
    private List<String> myList = new ArrayList<>();

    public List<String> getMyList() {
      return myList;
    }

    public void setMyList(List<String> myList) {
      this.myList = myList;
    }
  }

  public static class InlineInitializationBeanPrivateConstructor {
    private List<String> myList = new ArrayList<>();

    public List<String> getMyList() {
      return myList;
    }

    private InlineInitializationBeanPrivateConstructor() {}
  }
}
