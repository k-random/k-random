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
package io.github.krandom.parameters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;

import io.github.krandom.KRandom;
import io.github.krandom.KRandomParameters;
import io.github.krandom.ObjectCreationException;
import io.github.krandom.beans.Ape;
import io.github.krandom.beans.Bar;
import io.github.krandom.beans.ClassUsingAbstractEnum;
import io.github.krandom.beans.ComparableBean;
import io.github.krandom.beans.ConcreteBar;
import io.github.krandom.beans.Foo;
import io.github.krandom.beans.Human;
import io.github.krandom.beans.Mamals;
import io.github.krandom.beans.Person;
import io.github.krandom.beans.SocialPerson;
import java.util.Date;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ScanClasspathForConcreteTypesParameterTests {

  private KRandom kRandom;

  @Test
  void
      whenScanClasspathForConcreteTypesIsDisabled_thenShouldFailToPopulateInterfacesAndAbstractClasses() {
    KRandomParameters parameters = new KRandomParameters().scanClasspathForConcreteTypes(false);
    kRandom = new KRandom(parameters);

    assertThatThrownBy(() -> kRandom.nextObject(Mamals.class))
        .isInstanceOf(ObjectCreationException.class);
  }

  @Test
  void whenScanClasspathForConcreteTypesIsEnabled_thenShouldPopulateInterfacesAndAbstractClasses() {
    KRandomParameters parameters = new KRandomParameters().scanClasspathForConcreteTypes(true);
    kRandom = new KRandom(parameters);

    Mamals mamals = kRandom.nextObject(Mamals.class);

    assertThat(mamals.getMamal())
        .isOfAnyClassIn(Human.class, Ape.class, Person.class, SocialPerson.class);
    assertThat(mamals.getMamalImpl())
        .isOfAnyClassIn(Human.class, Ape.class, Person.class, SocialPerson.class);
  }

  @Test
  void
      whenScanClasspathForConcreteTypesIsEnabled_thenShouldPopulateConcreteTypesForFieldsWithGenericParameters() {
    KRandomParameters parameters = new KRandomParameters().scanClasspathForConcreteTypes(true);
    kRandom = new KRandom(parameters);

    ComparableBean comparableBean = kRandom.nextObject(ComparableBean.class);

    assertThat(comparableBean.getDateComparable())
        .isOfAnyClassIn(ComparableBean.AlwaysEqual.class, Date.class);
  }

  @Test
  void
      whenScanClasspathForConcreteTypesIsEnabled_thenShouldPopulateAbstractTypesWithConcreteSubTypes() {
    // Given
    KRandomParameters parameters = new KRandomParameters().scanClasspathForConcreteTypes(true);
    kRandom = new KRandom(parameters);

    // When
    Bar bar = kRandom.nextObject(Bar.class);

    // Then
    assertThat(bar).isNotNull();
    assertThat(bar).isInstanceOf(ConcreteBar.class);
    // https://github.com/k-random/k-random/issues/204
    assertThat(bar.getI()).isNotNull();
  }

  @Test
  void
      whenScanClasspathForConcreteTypesIsEnabled_thenShouldPopulateFieldsOfAbstractTypeWithConcreteSubTypes() {
    // Given
    KRandomParameters parameters = new KRandomParameters().scanClasspathForConcreteTypes(true);
    kRandom = new KRandom(parameters);

    // When
    Foo foo = kRandom.nextObject(Foo.class);

    // Then
    assertThat(foo).isNotNull();
    assertThat(foo.getBar()).isInstanceOf(ConcreteBar.class);
    assertThat(foo.getBar().getName()).isNotEmpty();
  }

  @Test
  void whenScanClasspathForConcreteTypesIsEnabled_thenShouldPopulateAbstractEnumeration() {
    KRandomParameters parameters = new KRandomParameters().scanClasspathForConcreteTypes(true);
    kRandom = new KRandom(parameters);

    ClassUsingAbstractEnum randomValue = kRandom.nextObject(ClassUsingAbstractEnum.class);

    then(randomValue.getTestEnum()).isNotNull();
  }

  // issue https://github.com/k-random/k-random/issues/353

  @Test
  void testScanClasspathForConcreteTypes_whenConcreteTypeIsAnInnerClass() {
    KRandomParameters parameters = new KRandomParameters().scanClasspathForConcreteTypes(true);
    KRandom kRandom = new KRandom(parameters);

    Foobar foobar = kRandom.nextObject(Foobar.class);

    Assertions.assertThat(foobar).isNotNull();
    Assertions.assertThat(foobar.getToto()).isNotNull();
  }

  public class Foobar {

    public abstract class Toto {}

    public class TotoImpl extends Toto {}

    private Toto toto;

    public Toto getToto() {
      return toto;
    }
  }
}
