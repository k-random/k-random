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
import io.github.krandom.FieldPredicates.named
import io.github.krandom.FieldPredicates.ofType
import io.github.krandom.api.Randomizer
import io.github.krandom.beans.AbstractBean
import io.github.krandom.beans.BoundedBaseClass
import io.github.krandom.beans.Gender
import io.github.krandom.beans.GenericBaseClassSeparateFile
import io.github.krandom.beans.Human
import io.github.krandom.beans.ImmutableBean
import io.github.krandom.beans.Node
import io.github.krandom.beans.Person
import io.github.krandom.beans.Salary
import io.github.krandom.beans.Street
import io.github.krandom.beans.TestBean
import io.github.krandom.beans.TestData
import io.github.krandom.beans.TestEnum
import io.github.krandom.util.ReflectionUtils
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.ranges.shouldBeIn
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import java.lang.reflect.Modifier
import java.text.SimpleDateFormat
import java.util.Date
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.fail

@Suppress("ArrayInDataClass")
@ExtendWith(MockKExtension::class)
internal class KRandomTest {
  @MockK private lateinit var randomizer: Randomizer<String>
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun `generated beans should be correctly populated`() {
    val person = kRandom.nextObject(Person::class.java)

    validatePerson(person)
  }

  @Test
  fun `should fail if setter invocation fails`() {
    val kRandom = KRandom()

    val thrown = shouldThrow<ObjectCreationException> { kRandom.nextObject(Salary::class.java) }

    thrown.message shouldContain
      "Unable to create a random instance of type class io.github.krandom.beans.Salary"
    thrown.cause.shouldNotBeNull()
    thrown.cause.shouldBeInstanceOf<ObjectCreationException>()
    thrown.cause!!.message shouldContain
      "Unable to invoke setter for field amount of class io.github.krandom.beans.Salary"
    thrown.cause!!.cause.shouldNotBeNull()
    thrown.cause!!.cause.shouldBeInstanceOf<IllegalArgumentException>()
    thrown.cause!!.cause!!.message shouldContain "Amount must be positive"
  }

  @Test
  fun `final fields should be populated`() {
    val person = kRandom.nextObject(Person::class.java)

    person.shouldNotBeNull()
    person.id.shouldNotBeNull()
  }

  @Test
  fun `static fields should not be populated`() {
    try {
      val human = kRandom.nextObject(Human::class.java)

      human.shouldNotBeNull()
    } catch (e: Exception) {
      fail("Should be able to populate types with static fields.", e)
    }
  }

  @Test
  fun `immutable beans should be populated`() {
    val immutableBean = kRandom.nextObject(ImmutableBean::class.java)

    immutableBean.shouldNotBeNull()
    immutableBean.finalCollection.shouldNotBeNull()
    immutableBean.finalValue.shouldNotBeNull()
  }

  @Test
  fun `generated beans number should be equal to specified number`() {
    val persons: List<Person> = kRandom.objects(Person::class.java, 2)

    persons shouldHaveSize 2
    persons.forAll { it.shouldBeInstanceOf<Person>() }
  }

  @Test
  fun `custom randomzier for fields should be used to populate objects`() {
    every { randomizer.getRandomValue() } returns FOO
    val parameters =
      KRandomParameters()
        .randomize(
          named("name").and(ofType(String::class.java)).and(inClass(Human::class.java)),
          randomizer,
        )
    kRandom = KRandom(parameters)

    val person = kRandom.nextObject(Person::class.java)

    person.shouldNotBeNull()
    person.name shouldBe FOO
  }

  @Test
  fun `custom randomzier for fields should be used to populate fields with one modifier`() {
    every { randomizer.getRandomValue() } returns FOO

    val parameters =
      KRandomParameters()
        .randomize(hasModifiers(Modifier.TRANSIENT).and(ofType(String::class.java)), randomizer)
    kRandom = KRandom(parameters)

    val person = kRandom.nextObject(Person::class.java)

    person.shouldNotBeNull()
    person.email.shouldNotBeNull()
    person.email shouldBe FOO
    person.name.shouldNotBeNull()
    person.name shouldNotBe FOO
  }

  @Test
  fun `custom randomzier for fields should be used to populate fields with multiple modifier`() {
    every { randomizer.getRandomValue() } returns FOO
    val modifiers = Modifier.TRANSIENT or Modifier.PRIVATE
    val parameters =
      KRandomParameters()
        .randomize(hasModifiers(modifiers).and(ofType(String::class.java)), randomizer)
    kRandom = KRandom(parameters)

    val person = kRandom.nextObject(Person::class.java)

    person.shouldNotBeNull()
    person.email shouldBe FOO
    person.name shouldNotBe FOO
  }

  @Test
  fun `custom randomzier for types should be used to populate objects`() {
    every { randomizer.getRandomValue() } returns FOO
    val parameters: KRandomParameters =
      KRandomParameters().randomize(String::class.java, randomizer)
    kRandom = KRandom(parameters)

    val string = kRandom.nextObject(String::class.java)

    string shouldBe FOO
  }

  @Test
  fun `custom randomzier for types should be used to populate fields`() {
    every { randomizer.getRandomValue() } returns FOO
    val parameters: KRandomParameters =
      KRandomParameters().randomize(String::class.java, randomizer)
    kRandom = KRandom(parameters)

    val human = kRandom.nextObject(Human::class.java)

    human.shouldNotBeNull()
    human.name shouldBe FOO
  }

  @Test
  fun `when specified number to generate is negative then should throw an illegal argument exception`() {
    shouldThrow<IllegalArgumentException> { kRandom.objects(Person::class.java, -2) }
  }

  @Test
  fun `when unable to instantiate field then should throw object generation exception`() {
    shouldThrow<ObjectCreationException> { kRandom.nextObject(AbstractBean::class.java) }
  }

  @Test
  fun `beans with recursive structure must not cause stack overflow exception`() {
    val node = kRandom.nextObject(Node::class.java)

    node.shouldNotBeNull()
    node.left.shouldNotBeNull()
    node.right.shouldNotBeNull()
    node.parents.shouldNotBeNull()
    node.value.shouldNotBeNull()
  }

  @Test
  fun `any type must be correctly populated`() {
    val any = kRandom.nextObject(Any::class.java)

    any.shouldNotBeNull()
  }

  @Test
  fun `annotated randomizer arguments should be correctly parsed`() {
    val startDateString = "2016-01-10 00:00:00"
    val endDateString = "2016-01-30 23:59:59"
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val startDate: Date = formatter.parse(startDateString)
    val endDate: Date = formatter.parse(endDateString)

    val data = kRandom.nextObject(TestData::class.java)

    data.shouldNotBeNull()
    data.date shouldBeIn startDate..endDate
    data.price shouldBeIn 200..500
  }

  @Test
  fun `next enum should not always return the same value`() {
    val distinctEnumBeans = HashSet<TestEnum?>()

    repeat(10) { distinctEnumBeans.add(kRandom.nextObject(TestEnum::class.java)) }

    distinctEnumBeans.size shouldBeGreaterThan 1
  }

  @Test
  fun `fields of type class should be skipped`() {
    try {
      val testBean = kRandom.nextObject(TestBean::class.java)

      testBean.shouldNotBeNull()
      testBean.exception.shouldBeNull()
      testBean.clazz.shouldBeNull()
    } catch (_: Exception) {
      fail("Should skip fields of type Class")
    }
  }

  @Test
  fun `different collections should be randomized with different sizes`() {
    data class Foo(val names: List<String?>, val addresses: List<String?>)

    val foo = KRandom().nextObject(Foo::class.java)

    foo.shouldNotBeNull()
    foo.names.size shouldNotBe foo.addresses.size
  }

  @Test
  fun `different arrays should be randomized with different sizes`() {
    data class Foo(val names: Array<String>, val addresses: Array<String>)

    val foo = KRandom().nextObject(Foo::class.java)

    foo.shouldNotBeNull()
    foo.names.size shouldNotBe foo.addresses.size
  }

  @Test
  fun `test generic type randomization`() {
    val concrete = kRandom.nextObject(Concrete::class.java)

    concrete.shouldNotBeNull()
    concrete.t.shouldNotBeNull()
    concrete.t.shouldBeInstanceOf<String>()
    concrete.t!!.isNotEmpty()
  }

  @Test
  fun `test multiple generic type randomization`() {
    val concreteMultipleGeneric = kRandom.nextObject(ConcreteMultipleGeneric::class.java)

    concreteMultipleGeneric.shouldNotBeNull()
    concreteMultipleGeneric.t.shouldNotBeNull()
    concreteMultipleGeneric.t.shouldBeInstanceOf<String>()
    concreteMultipleGeneric.t.shouldNotBeEmpty()
    concreteMultipleGeneric.s.shouldNotBeNull()
    concreteMultipleGeneric.s.shouldBeInstanceOf<Long>()
  }

  @Test
  fun `generic base class`() {
    val concreteWithGenericBaseClass = kRandom.nextObject(ConcreteWithGenericBaseClass::class.java)

    concreteWithGenericBaseClass.shouldNotBeNull()
    concreteWithGenericBaseClass.x.shouldBeInstanceOf<Int>()
    concreteWithGenericBaseClass.y.shouldBeInstanceOf<String>()
  }

  @Test
  fun `generic base class with bean`() {
    class Concrete(x: Street?, val y: String?) : GenericBaseClassSeparateFile<Street?>(x)

    val concrete = kRandom.nextObject(Concrete::class.java)

    concrete.shouldNotBeNull()
    concrete.x.shouldNotBeNull()
    concrete.x.shouldBeInstanceOf<Street>()
    concrete.y.shouldNotBeNull()
    concrete.y.shouldBeInstanceOf<String>()
  }

  @Test
  fun `bounded base class`() {
    class Concrete(x: IntWrapper?, val y: String?) :
      BoundedBaseClass<BoundedBaseClass.IntWrapper?>(x)

    val concrete = kRandom.nextObject(Concrete::class.java)

    concrete.shouldNotBeNull()
    concrete.x.shouldNotBeNull()
    concrete.x.shouldBeInstanceOf<BoundedBaseClass.IntWrapper>()
    concrete.y.shouldNotBeNull()
    concrete.y.shouldBeInstanceOf<String>()
  }

  @Test
  fun testMultipleGenericLevels() {
    abstract class BaseClass<T>(val x: T?)

    abstract class GenericBaseClass<T, P>(x: T?, val y: P?) : BaseClass<T?>(x)

    class Concrete(x: String?, y: Long?) : GenericBaseClass<String?, Long?>(x, y)

    val concrete = kRandom.nextObject(Concrete::class.java)

    concrete.shouldNotBeNull()
    concrete.x.shouldNotBeNull()
    concrete.y.shouldNotBeNull()
    concrete.x.shouldBeInstanceOf<String>()
    concrete.y.shouldBeInstanceOf<Long>()
  }

  @Test
  fun testComplexGenericTypeRandomization() { // not supported
    open class Base<T> {
      var t: T? = null
    }

    class Concrete : Base<MutableList<String?>?>()

    val creationException =
      shouldThrow<ObjectCreationException> { kRandom.nextObject(Concrete::class.java) }

    creationException.message shouldContain "Unable to create a random instance of type class"
  }

  @Test
  fun `test root generic type`() { // intermediate type in the hierarchy is not generic
    val concreteFromRootGenericType = kRandom.nextObject(ConcreteFromRootGenericType::class.java)

    concreteFromRootGenericType.shouldNotBeNull()
    concreteFromRootGenericType.x.shouldBeInstanceOf<String>()
  }

  private fun validatePerson(person: Person?) {
    person.shouldNotBeNull()
    person.email.shouldNotBeEmpty()
    person.gender shouldBeIn Gender.entries
    person.birthDate.shouldNotBeNull()
    person.phoneNumber.shouldNotBeEmpty()
    person.nicknames.shouldNotBeNull()
    person.name.shouldNotBeEmpty()
    val address = person.address
    address.shouldNotBeNull()
    address.city.shouldNotBeEmpty()
    address.country.shouldNotBeEmpty()
    address.zipCode.shouldNotBeEmpty()
    val street = address.street
    street.shouldNotBeNull()
    street.name.shouldNotBeEmpty()
    street.number.shouldNotBeNull()
    street.streetType.shouldNotBeNull()
  }

  @Disabled("Dummy test to see possible reasons of randomization failures")
  @Test
  fun `try to randomize all public concrete types in the classpath`() {
    var success = 0
    var failure = 0
    val publicConcreteTypes: List<Class<*>> =
      ReflectionUtils.getPublicConcreteSubTypesOf(Any::class.java)
    println("Found ${publicConcreteTypes.size} public concrete types in the classpath")
    for (aClass in publicConcreteTypes) {
      try {
        kRandom.nextObject(aClass)
        println("${aClass.getName()} has been successfully randomized")
        success++
      } catch (e: Throwable) {
        System.err.println("Unable to populate a random instance of type: ${aClass.getName()}")
        System.err.println(e)
        System.err.println("----------------------------------------------")
        failure++
      }
    }
    println("Success: $success")
    println("Failure: $failure")
  }

  open class Base<T> {
    var t: T? = null
  }

  class Concrete : Base<String>()

  open class MultipleGenericBase<T, S> {
    var t: T? = null
    var s: S? = null
  }

  class ConcreteMultipleGeneric : MultipleGenericBase<String?, Long?>()

  class ConcreteWithGenericBaseClass(x: Int, val y: String) : GenericBaseClassSeparateFile<Int>(x)

  abstract class BaseClass<T>(val x: T?)

  abstract class GenericBaseClass(x: String?) : BaseClass<String?>(x)

  class ConcreteFromRootGenericType(x: String?) : GenericBaseClass(x)

  companion object {
    private const val FOO = "foo"
  }
}
