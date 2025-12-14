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
package io.github.krandom.parameters

import io.github.krandom.KRandom
import io.github.krandom.KRandomParameters
import io.github.krandom.ObjectCreationException
import io.github.krandom.beans.Ape
import io.github.krandom.beans.Bar
import io.github.krandom.beans.ClassUsingAbstractEnum
import io.github.krandom.beans.ComparableBean
import io.github.krandom.beans.ComparableBean.AlwaysEqual
import io.github.krandom.beans.ConcreteBar
import io.github.krandom.beans.Foo
import io.github.krandom.beans.Human
import io.github.krandom.beans.Mamals
import io.github.krandom.beans.Person
import io.github.krandom.beans.SocialPerson
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldBeOneOf
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.types.shouldBeInstanceOf
import java.util.Date
import org.junit.jupiter.api.Test

internal class ScanClasspathForConcreteTypesParameterTests {
  private lateinit var kRandom: KRandom

  @Test
  fun `when scan classpath for concrete types is disabled then should fail to populate nonconcrete classes`() {
    val parameters = KRandomParameters().scanClasspathForConcreteTypes(false)
    kRandom = KRandom(parameters)

    shouldThrow<ObjectCreationException> { kRandom.nextObject(Mamals::class.java) }
  }

  @Test
  fun `when scan classpath for concrete types is enabled then should populate nonconcrete classes`() {
    val parameters = KRandomParameters().scanClasspathForConcreteTypes(true)
    kRandom = KRandom(parameters)

    val mammals = kRandom.nextObject(Mamals::class.java)

    mammals.shouldNotBeNull()
    mammals.mamal::class shouldBeOneOf
      listOf(Human::class, Ape::class, Person::class, SocialPerson::class)
    mammals.mamalImpl::class shouldBeOneOf
      listOf(Human::class, Ape::class, Person::class, SocialPerson::class)
  }

  @Test
  fun `when scan classpath for concrete types is enabled then should populate concrete types`() {
    val parameters = KRandomParameters().scanClasspathForConcreteTypes(true)
    kRandom = KRandom(parameters)

    val comparableBean = kRandom.nextObject(ComparableBean::class.java)

    comparableBean.shouldNotBeNull()
    comparableBean.dateComparable::class shouldBeOneOf listOf(AlwaysEqual::class, Date::class)
  }

  @Test
  fun `when scan classpath for concrete types is enabled then should populate abstract types with concrete subtypes`() {
    val parameters = KRandomParameters().scanClasspathForConcreteTypes(true)
    kRandom = KRandom(parameters)

    val bar = kRandom.nextObject(Bar::class.java)

    bar.shouldNotBeNull()
    bar.shouldBeInstanceOf<ConcreteBar>()
    bar.i.shouldNotBeNull()
  }

  @Test
  fun `when scan classpath for concrete types is enabled then should populate fields`() {
    val parameters = KRandomParameters().scanClasspathForConcreteTypes(true)
    kRandom = KRandom(parameters)

    val foo = kRandom.nextObject(Foo::class.java)

    foo.shouldNotBeNull()
    foo.bar.shouldBeInstanceOf<ConcreteBar>()
    foo.bar.name.shouldNotBeEmpty()
  }

  @Test
  fun `when scan classpath for concrete types is enabled then should populate abstract enumeration`() {
    val parameters = KRandomParameters().scanClasspathForConcreteTypes(true)
    kRandom = KRandom(parameters)

    val randomValue = kRandom.nextObject(ClassUsingAbstractEnum::class.java)

    randomValue.shouldNotBeNull()
    randomValue.testEnum.shouldNotBeNull()
  }

  @Test
  fun `test scan classpath for concrete types when concrete type is an inner class`() {
    val parameters = KRandomParameters().scanClasspathForConcreteTypes(true)
    val kRandom = KRandom(parameters)

    val foobar: Foobar? = kRandom.nextObject(Foobar::class.java)

    foobar.shouldNotBeNull()
    foobar.toto.shouldBeInstanceOf<Foobar.TotoImpl>()
  }

  internal data class Foobar(val toto: Toto) {
    abstract class Toto

    class TotoImpl : Toto()
  }
}
