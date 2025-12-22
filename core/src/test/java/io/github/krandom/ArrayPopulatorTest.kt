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

import io.github.krandom.beans.ArrayBean
import io.github.krandom.beans.Person
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@Suppress("UNCHECKED_CAST")
@ExtendWith(MockKExtension::class)
internal class ArrayPopulatorTest {
  @MockK private lateinit var context: RandomizationContext
  @MockK private lateinit var kRandom: KRandom

  private lateinit var arrayPopulator: ArrayPopulator

  @BeforeEach
  fun setUp() {
    arrayPopulator = ArrayPopulator(kRandom)
  }

  @Test
  fun `get random array`() {
    every { context.parameters } returns KRandomParameters().collectionSizeRange(INT, INT)
    every { kRandom.doPopulateBean(String::class.java, context) } returns STRING
    every { kRandom.nextLong() } returns INT.toLong()

    val strings =
      (arrayPopulator.getRandomArray(Array<String>::class.java, context) as Array<String>).toSet()

    strings shouldContainOnly setOf(STRING)
  }

  /*
   * Integration tests for arrays population
   */
  @Test
  fun `test array population`() {
    val kRandom = KRandom()

    val strings = kRandom.nextObject(Array<String>::class.java)

    strings.shouldNotBeNull()
  }

  @Test
  fun `test primitive array population`() {
    val kRandom = KRandom()

    val ints = kRandom.nextObject(IntArray::class.java)

    ints.shouldNotBeNull()
  }

  @Test
  fun `primitive arrays should be correctly populated`() {
    val kRandom = KRandom()

    val bean = kRandom.nextObject(ArrayBean::class.java)

    // primitive types
    bean.shouldNotBeNull()
    bean.byteArray.toList().forAll { it.shouldBeInstanceOf<Byte>() }
    bean.shortArray.toList().forAll { it.shouldBeInstanceOf<Short>() }
    bean.intArray.toList().forAll { it.shouldBeInstanceOf<Int>() }
    bean.longArray.toList().forAll { it.shouldBeInstanceOf<Long>() }
    bean.floatArray.toList().forAll { it.shouldBeInstanceOf<Float>() }
    bean.doubleArray.toList().forAll { it.shouldBeInstanceOf<Double>() }
    bean.charArray.toList().forAll { it.shouldBeInstanceOf<Char>() }
    bean.booleanArray.toList().forAll { it.shouldBeInstanceOf<Boolean>() }
  }

  @Test
  fun `wrapper type arrays should be correctly populated`() {
    val kRandom = KRandom()

    val bean = kRandom.nextObject(ArrayBean::class.java)

    // wrapper types
    bean.shouldNotBeNull()
    bean.bytes.forAll { it.shouldBeInstanceOf<Byte>() }
    bean.shorts.forAll { it.shouldBeInstanceOf<Short>() }
    bean.integers.forAll { it.shouldBeInstanceOf<Int>() }
    bean.longs.forAll { it.shouldBeInstanceOf<Long>() }
    bean.floats.forAll { it.shouldBeInstanceOf<Float>() }
    bean.doubles.forAll { it.shouldBeInstanceOf<Double>() }
    bean.characters.forAll { it.shouldBeInstanceOf<Char>() }
    bean.booleans.forAll { it.shouldBeInstanceOf<Boolean>() }
  }

  @Test
  fun `arrays with custom types should be correctly populated`() {
    val kRandom = KRandom()

    val bean = kRandom.nextObject(ArrayBean::class.java)

    // custom types
    bean.shouldNotBeNull()
    bean.strings.shouldNotContain(null)
    bean.strings.shouldNotContain("")

    val persons = bean.persons
    assertContainsOnlyNonEmptyPersons(persons)
  }

  private fun assertContainsOnlyNonEmptyPersons(persons: Array<Person>) {
    for (person in persons) {
      person.shouldNotBeNull()
      person.address!!.city.shouldNotBeEmpty()
      person.address!!.zipCode.shouldNotBeEmpty()
      person.name.shouldNotBeEmpty()
    }
  }

  companion object {
    private const val INT = 10
    private const val STRING = "FOO"
  }
}
