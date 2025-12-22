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
package io.github.krandom

import io.github.krandom.FieldPredicates.hasModifiers
import io.github.krandom.FieldPredicates.inClass
import io.github.krandom.FieldPredicates.isAnnotatedWith
import io.github.krandom.FieldPredicates.named
import io.github.krandom.FieldPredicates.ofType
import io.github.krandom.api.ContextAwareRandomizer
import io.github.krandom.api.RandomizerContext
import io.github.krandom.beans.Address
import io.github.krandom.beans.Human
import io.github.krandom.beans.Person
import io.github.krandom.beans.Street
import io.github.krandom.beans.Website
import io.github.krandom.beans.exclusion.A
import io.github.krandom.beans.exclusion.B
import io.github.krandom.beans.exclusion.C
import java.lang.Deprecated
import java.lang.reflect.Modifier
import kotlin.String
import kotlin.Suppress
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class FieldExclusionTest {
  private var kRandom: KRandom? = null

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun excludedFieldsShouldNotBePopulated() {
    // given
    val parameters = KRandomParameters().excludeField(named("name"))
    kRandom = KRandom(parameters)

    // when
    val person = kRandom!!.nextObject(Person::class.java)

    // then
    Assertions.assertThat<Person?>(person).isNotNull()
    Assertions.assertThat(person!!.name).isNull()
  }

  @Test
  fun excludedFieldsUsingSkipRandomizerShouldNotBePopulated() {
    // given
    val parameters =
      KRandomParameters()
        .excludeField(named("name").and(ofType(String::class.java)).and(inClass(Human::class.java)))
    kRandom = KRandom(parameters)

    // when
    val person = kRandom!!.nextObject(Person::class.java)

    // then
    Assertions.assertThat<Person?>(person).isNotNull()
    Assertions.assertThat(person!!.name).isNull()
  }

  @Test
  fun excludedFieldsUsingFieldDefinitionShouldNotBePopulated() {
    // given
    val parameters = KRandomParameters().excludeField(named("name"))
    kRandom = KRandom(parameters)

    // when
    val person = kRandom!!.nextObject(Person::class.java)

    // then
    Assertions.assertThat<Person?>(person).isNotNull()
    Assertions.assertThat<Address?>(person!!.address).isNotNull()
    Assertions.assertThat<Street?>(person!!.address!!.street).isNotNull()

    // person.name and street.name should be null
    Assertions.assertThat(person!!.name).isNull()
    Assertions.assertThat(person!!.address!!.street!!.getName()).isNull()
  }

  @Test
  fun excludedDottedFieldsShouldNotBePopulated() {
    // given
    val parameters =
      KRandomParameters().excludeField(named("name").and(inClass(Street::class.java)))
    kRandom = KRandom(parameters)

    // when
    val person = kRandom!!.nextObject(Person::class.java)

    // then
    Assertions.assertThat<Person?>(person).isNotNull()
    Assertions.assertThat<Address?>(person!!.address).isNotNull()
    Assertions.assertThat<Street?>(person!!.address!!.street).isNotNull()
    Assertions.assertThat(person!!.address!!.street!!.getName()).isNull()
  }

  @Test
  fun fieldsExcludedWithAnnotationShouldNotBePopulated() {
    val person = kRandom!!.nextObject(Person::class.java)

    Assertions.assertThat<Person?>(person).isNotNull()
    Assertions.assertThat(person!!.excluded).isNull()
  }

  @Test
  @Suppress("deprecation")
  fun fieldsExcludedWithAnnotationViaFieldDefinitionShouldNotBePopulated() {
    // given
    val parameters = KRandomParameters().excludeField(isAnnotatedWith(Deprecated::class.java))
    kRandom = KRandom(parameters)

    // when
    val website = kRandom!!.nextObject(Website::class.java)

    // then
    Assertions.assertThat<Website?>(website).isNotNull()
    Assertions.assertThat(website!!.getProvider()).isNull()
  }

  @Test
  fun fieldsExcludedFromTypeViaFieldDefinitionShouldNotBePopulated() {
    // given
    val parameters = KRandomParameters().excludeField(inClass(Address::class.java))
    kRandom = KRandom(parameters)

    // when
    val person = kRandom!!.nextObject(Person::class.java)

    // then
    Assertions.assertThat<Person?>(person).isNotNull()
    Assertions.assertThat<Address?>(person!!.address).isNotNull()
    // all fields declared in class Address must be null
    Assertions.assertThat(person!!.address!!.city).isNull()
    Assertions.assertThat<Street?>(person!!.address!!.street).isNull()
    Assertions.assertThat(person!!.address!!.zipCode).isNull()
    Assertions.assertThat(person!!.address!!.country).isNull()
  }

  @Test
  fun testFirstLevelExclusion() {
    val parameters = KRandomParameters().excludeField(named("b2").and(inClass(C::class.java)))
    kRandom = KRandom(parameters)

    val c = kRandom!!.nextObject(C::class.java)

    Assertions.assertThat<C?>(c).isNotNull()

    // B1 and its "children" must not be null
    Assertions.assertThat<B?>(c!!.b1).isNotNull()
    Assertions.assertThat<A>(c!!.b1!!.a1).isNotNull()
    Assertions.assertThat(c!!.b1!!.a1.s1).isNotNull()
    Assertions.assertThat(c!!.b1!!.a1.s2).isNotNull()
    Assertions.assertThat<A>(c!!.b1!!.a2).isNotNull()
    Assertions.assertThat(c!!.b1!!.a2.s1).isNotNull()
    Assertions.assertThat(c!!.b1!!.a2.s2).isNotNull()

    // B2 must be null
    Assertions.assertThat<B?>(c!!.b2).isNull()
  }

  @Test
  fun testSecondLevelExclusion() { // goal: exclude only b2.a2
    val parameters =
      KRandomParameters()
        .randomize<A?>(
          ofType(A::class.java).and(inClass(B::class.java)),
          object : ContextAwareRandomizer<A?> {
            private var context: RandomizerContext? = null

            override fun setRandomizerContext(context: RandomizerContext) {
              this.context = context
            }

            override fun getRandomValue(): A? {
              if (context!!.getCurrentField() == "b2.a2") {
                return null
              }
              return KRandom().nextObject(A::class.java)
            }
          },
        )
    kRandom = KRandom(parameters)
    val c = kRandom!!.nextObject(C::class.java)

    Assertions.assertThat<C?>(c).isNotNull()

    // B1 and its "children" must not be null
    Assertions.assertThat<B?>(c!!.b1).isNotNull()
    Assertions.assertThat<A>(c!!.b1!!.a1).isNotNull()
    Assertions.assertThat(c!!.b1!!.a1.s1).isNotNull()
    Assertions.assertThat(c!!.b1!!.a1.s2).isNotNull()
    Assertions.assertThat<A>(c!!.b1!!.a2).isNotNull()
    Assertions.assertThat(c!!.b1!!.a2.s1).isNotNull()
    Assertions.assertThat(c!!.b1!!.a2.s2).isNotNull()

    // Only B2.A2 must be null
    Assertions.assertThat<B?>(c!!.b2).isNotNull()
    Assertions.assertThat<A>(c!!.b2!!.a1).isNotNull()
    Assertions.assertThat(c!!.b2!!.a1.s1).isNotNull()
    Assertions.assertThat(c!!.b2!!.a1.s2).isNotNull()
    Assertions.assertThat<A>(c!!.b2!!.a2).isNull()
  }

  @Test
  fun testThirdLevelExclusion() { // goal: exclude only b2.a2.s2
    val parameters =
      KRandomParameters()
        .randomize<String?>(
          named("s2").and(inClass(A::class.java)),
          object : ContextAwareRandomizer<String?> {
            private var context: RandomizerContext? = null

            override fun setRandomizerContext(context: RandomizerContext) {
              this.context = context
            }

            override fun getRandomValue(): String? {
              if (context!!.getCurrentField() == "b2.a2.s2") {
                return null
              }
              return KRandom().nextObject(String::class.java)
            }
          },
        )
    kRandom = KRandom(parameters)
    val c = kRandom!!.nextObject(C::class.java)

    // B1 and its "children" must not be null
    Assertions.assertThat<B?>(c!!.b1).isNotNull()
    Assertions.assertThat<A>(c!!.b1!!.a1).isNotNull()
    Assertions.assertThat(c!!.b1!!.a1.s1).isNotNull()
    Assertions.assertThat(c!!.b1!!.a1.s2).isNotNull()
    Assertions.assertThat<A>(c!!.b1!!.a2).isNotNull()
    Assertions.assertThat(c!!.b1!!.a2.s1).isNotNull()
    Assertions.assertThat(c!!.b1!!.a2.s2).isNotNull()

    // Only B2.A2.S2 must be null
    Assertions.assertThat<B?>(c!!.b2).isNotNull()
    Assertions.assertThat<A>(c!!.b2!!.a1).isNotNull()
    Assertions.assertThat(c!!.b2!!.a1.s1).isNotNull()
    Assertions.assertThat(c!!.b2!!.a1.s2).isNotNull()
    Assertions.assertThat(c!!.b2!!.a2.s1).isNotNull()
    Assertions.assertThat(c!!.b2!!.a2.s2).isNull()
  }

  @Test
  fun testFirstLevelCollectionExclusion() {
    val parameters = KRandomParameters().excludeField(named("b3").and(inClass(C::class.java)))
    kRandom = KRandom(parameters)

    val c = kRandom!!.nextObject(C::class.java)

    Assertions.assertThat<C?>(c).isNotNull()

    // B1 and its "children" must not be null
    Assertions.assertThat<B?>(c!!.b1).isNotNull()
    Assertions.assertThat<A>(c!!.b1!!.a1).isNotNull()
    Assertions.assertThat(c!!.b1!!.a1.s1).isNotNull()
    Assertions.assertThat(c!!.b1!!.a1.s2).isNotNull()
    Assertions.assertThat<A>(c!!.b1!!.a2).isNotNull()
    Assertions.assertThat(c!!.b1!!.a2.s1).isNotNull()
    Assertions.assertThat(c!!.b1!!.a2.s2).isNotNull()

    // B1 and its "children" must not be null
    Assertions.assertThat<B?>(c!!.b2).isNotNull()
    Assertions.assertThat<A>(c!!.b2!!.a1).isNotNull()
    Assertions.assertThat(c!!.b2!!.a1.s1).isNotNull()
    Assertions.assertThat(c!!.b2!!.a1.s2).isNotNull()
    Assertions.assertThat<A>(c!!.b2!!.a2).isNotNull()
    Assertions.assertThat(c!!.b2!!.a2.s1).isNotNull()
    Assertions.assertThat(c!!.b2!!.a2.s2).isNotNull()

    // B3 must be null
    Assertions.assertThat<B>(c!!.b3).isNull()
  }

  @Test
  fun testSecondLevelCollectionExclusion() { // b3.a2 does not make sense, should be ignored
    val parameters =
      KRandomParameters()
        .randomize<A?>(
          named("a2").and(inClass(B::class.java)),
          object : ContextAwareRandomizer<A?> {
            private var context: RandomizerContext? = null

            override fun setRandomizerContext(context: RandomizerContext) {
              this.context = context
            }

            override fun getRandomValue(): A? {
              if (context!!.getCurrentField() == "b3.a2") {
                return null
              }
              return KRandom().nextObject(A::class.java)
            }
          },
        )
    kRandom = KRandom(parameters)

    val c = kRandom!!.nextObject(C::class.java)

    Assertions.assertThat<C?>(c).isNotNull()

    // B1 and its "children" must not be null
    Assertions.assertThat<B?>(c!!.b1).isNotNull()
    Assertions.assertThat<A>(c!!.b1!!.a1).isNotNull()
    Assertions.assertThat(c!!.b1!!.a1.s1).isNotNull()
    Assertions.assertThat(c!!.b1!!.a1.s2).isNotNull()
    Assertions.assertThat<A>(c!!.b1!!.a2).isNotNull()
    Assertions.assertThat(c!!.b1!!.a2.s1).isNotNull()
    Assertions.assertThat(c!!.b1!!.a2.s2).isNotNull()

    // B2 and its "children" must not be null
    Assertions.assertThat<B?>(c!!.b2).isNotNull()
    Assertions.assertThat<A>(c!!.b2!!.a1).isNotNull()
    Assertions.assertThat(c!!.b2!!.a1.s1).isNotNull()
    Assertions.assertThat(c!!.b2!!.a1.s2).isNotNull()
    Assertions.assertThat<A>(c!!.b2!!.a2).isNotNull()
    Assertions.assertThat(c!!.b2!!.a2.s1).isNotNull()
    Assertions.assertThat(c!!.b2!!.a2.s2).isNotNull()

    // B3 must not be null
    Assertions.assertThat<B>(c!!.b3).isNotNull()
  }

  @Test
  fun whenFieldIsExcluded_thenItsInlineInitializationShouldBeUsedAsIs() {
    // given
    val parameters =
      KRandomParameters()
        .excludeField(
          named("myList")
            .and(ofType(MutableList::class.java))
            .and(inClass(InlineInitializationBean::class.java))
        )
    kRandom = KRandom(parameters)

    // when
    val bean: InlineInitializationBean? = kRandom!!.nextObject(InlineInitializationBean::class.java)

    // then
    Assertions.assertThat<InlineInitializationBean?>(bean).isNotNull()
    Assertions.assertThat<String?>(bean!!.myList).isEmpty()
  }

  @Test
  fun whenFieldIsExcluded_thenItsInlineInitializationShouldBeUsedAsIs_EvenIfBeanHasNoPublicConstructor() {
    // given
    val parameters =
      KRandomParameters()
        .excludeField(
          named("myList")
            .and(ofType(MutableList::class.java))
            .and(inClass(InlineInitializationBeanPrivateConstructor::class.java))
        )
    kRandom = KRandom(parameters)

    // when
    val bean: InlineInitializationBeanPrivateConstructor? =
      kRandom!!.nextObject(InlineInitializationBeanPrivateConstructor::class.java)

    // then
    Assertions.assertThat<String?>(bean!!.myList).isEmpty()
  }

  @Test
  fun fieldsExcludedWithOneModifierShouldNotBePopulated() {
    // given
    val parameters = KRandomParameters().excludeField(hasModifiers(Modifier.TRANSIENT))
    kRandom = KRandom(parameters)

    // when
    val person = kRandom!!.nextObject(Person::class.java)

    // then
    Assertions.assertThat<Person?>(person).isNotNull()
    Assertions.assertThat(person!!.email).isNull()
  }

  @Test
  fun fieldsExcludedWithTwoModifiersShouldNotBePopulated() {
    // given
    val parameters =
      KRandomParameters().excludeField(hasModifiers(Modifier.TRANSIENT or Modifier.PRIVATE))
    kRandom = KRandom(parameters)

    // when
    val person = kRandom!!.nextObject(Person::class.java)

    // then
    Assertions.assertThat<Person?>(person).isNotNull()
    Assertions.assertThat(person!!.email).isNull()
  }

  @Test
  fun fieldsExcludedWithTwoModifiersShouldBePopulatedIfOneModifierIsNotFit() {
    // given
    val parameters =
      KRandomParameters().excludeField(hasModifiers(Modifier.TRANSIENT or Modifier.PUBLIC))
    kRandom = KRandom(parameters)

    // when
    val person = kRandom!!.nextObject(Person::class.java)

    // then
    Assertions.assertThat<Person?>(person).isNotNull()
    Assertions.assertThat(person!!.email).isNotNull()
  }

  class InlineInitializationBean {
    var myList: MutableList<String?>? = ArrayList<String?>()
  }

  class InlineInitializationBeanPrivateConstructor private constructor() {
    val myList: MutableList<String?> = ArrayList<String?>()
  }
}
