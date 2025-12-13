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

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import io.kotest.matchers.ranges.shouldBeIn
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DoubleRangeRandomizerTest : AbstractRangeRandomizerTest<Double>() {
  override val min: Double = 1.0
  override val max: Double = 10.0

  @BeforeEach
  fun setUp() {
    randomizer = DoubleRangeRandomizer(min, max)
  }

  @Test
  fun `generated value should be within specified range`() {
    val randomValue: Double = randomizer.getRandomValue()

    randomValue shouldBeIn min..max
  }

  @Test
  fun `when specified min value is after max value then throw illegal argument exception`() {
    shouldThrow<IllegalArgumentException> { DoubleRangeRandomizer(max, min) }
  }

  @Test
  fun `when specified min value is null then should use default min value`() {
    randomizer = DoubleRangeRandomizer(null, max)

    val randomDouble = randomizer.getRandomValue()

    randomDouble shouldBeLessThanOrEqualTo max
  }

  @Test
  fun `when specified max value is null then should use default max value`() {
    randomizer = DoubleRangeRandomizer(min, null)

    val randomDouble = randomizer.getRandomValue()

    randomDouble shouldBeGreaterThanOrEqualTo min
  }

  @Test
  fun `should always generate the same value for the same seed`() {
    val doubleRangeRandomizer = DoubleRangeRandomizer(min, max, SEED)

    val double = doubleRangeRandomizer.getRandomValue()

    double shouldBe 7.508567826974321
  }

  /* This test is for the first comment on https://stackoverflow.com/a/3680648/5019386. This test
   * never fails (tested with IntelliJ's feature "Repeat Test Until Failure") */
  @Test
  fun `test infinity`() {
    val doubleRangeRandomizer = DoubleRangeRandomizer(-Double.MAX_VALUE, Double.MAX_VALUE)

    val double = doubleRangeRandomizer.getRandomValue()

    double shouldBeIn -Double.MAX_VALUE..Double.MAX_VALUE
  }
}
