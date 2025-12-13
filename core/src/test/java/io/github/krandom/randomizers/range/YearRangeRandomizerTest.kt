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

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import io.kotest.matchers.ranges.shouldBeIn
import io.kotest.matchers.shouldBe
import java.time.Year
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class YearRangeRandomizerTest : AbstractRangeRandomizerTest<Year>() {
  override var min: Year = Year.of(1000)
  override var max: Year = Year.of(3000)

  @BeforeEach
  fun setUp() {
    randomizer = YearRangeRandomizer(min, max)
  }

  @Test
  fun `generated year should not be null`() {
    shouldNotThrowAny { randomizer.getRandomValue() }
  }

  @Test
  fun `generated year should be within specified range`() {
    val value = randomizer.getRandomValue()

    value shouldBeIn min..max
  }

  @Test
  fun `generated year should be always the same for the same seed`() {
    randomizer = YearRangeRandomizer(min, max, SEED)
    val expected = Year.of(2446)

    val randomValue = randomizer.getRandomValue()

    randomValue shouldBe expected
  }

  @Test
  fun `when specified min year is after max year then should throw illegal argument exception`() {
    shouldThrow<IllegalArgumentException> { YearRangeRandomizer(max, min) }
  }

  @Test
  fun `when specified min year is null then should use default min value`() {
    randomizer = YearRangeRandomizer(null, max)

    val randomValue = randomizer.getRandomValue()

    randomValue shouldBeLessThanOrEqualTo max
  }

  @Test
  fun `when specified max year is null then should use default max value`() {
    randomizer = YearRangeRandomizer(min, null)

    val randomValue = randomizer.getRandomValue()

    randomValue shouldBeGreaterThanOrEqualTo min
  }
}
