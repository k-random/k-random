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
package io.github.krandom.randomizers.range

import io.github.krandom.KRandom
import io.github.krandom.KRandomParameters
import io.github.krandom.beans.Street
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.ranges.shouldBeIn
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class IntRangeRandomizerTest : AbstractRangeRandomizerTest<Int>() {
  override val min: Int = 1
  override val max: Int = 10

  @BeforeEach
  fun setUp() {
    randomizer = IntRangeRandomizer(min, max)
  }

  @Test
  fun `generated value should be within specified range`() {
    val randomValue: Int = randomizer.getRandomValue()

    randomValue shouldBeIn min..max
  }

  @Test
  fun `when specified min value is after max value then throw illegal argument exception`() {
    shouldThrow<IllegalArgumentException> { IntRangeRandomizer(max, min).getRandomValue() }
  }

  @Test
  fun `when specified min value is null then should use default min value`() {
    randomizer = IntRangeRandomizer(null, max)

    val randomInteger = randomizer.getRandomValue()

    randomInteger shouldBeLessThanOrEqualTo max
  }

  @Test
  fun `when specified maxvalue is null then should use default max value`() {
    randomizer = IntRangeRandomizer(min, null)

    val randomInteger = randomizer.getRandomValue()

    randomInteger shouldBeGreaterThanOrEqualTo min
  }

  @Test
  fun `should always generate the same value for the same seed`() {
    val intRangeRandomizer = IntRangeRandomizer(min, max, SEED)

    val i = intRangeRandomizer.getRandomValue()

    i shouldBe 7
  }

  /*
   * Integration tests
   */
  @Test
  fun `generated value should be within specified range when used to randomize primitive integer type`() {
    val parameters =
      KRandomParameters().randomize(Int::class.javaPrimitiveType!!, IntRangeRandomizer(min, max))
    val kRandom = KRandom(parameters)

    val integer = kRandom.nextObject(Int::class.javaPrimitiveType!!)

    integer.shouldNotBeNull()
    integer shouldBeIn min..max
  }

  @Test
  fun `generated value should be within specified range when used to randomize wrapper integer type`() {
    val parameters = KRandomParameters().randomize(Int::class.java, IntRangeRandomizer(min, max))
    val kRandom = KRandom(parameters)

    val integer = kRandom.nextObject(Int::class.java)

    integer!! shouldBeIn min..max
  }

  @Test
  fun `generated value should be within specified range when used to randomize non integer type`() {
    val parameters = KRandomParameters().randomize(Int::class.java, IntRangeRandomizer(min, max))
    val kRandom = KRandom(parameters)

    val street = kRandom.nextObject(Street::class.java)

    street.shouldNotBeNull()
    street.number shouldBeIn min..max
  }
}
