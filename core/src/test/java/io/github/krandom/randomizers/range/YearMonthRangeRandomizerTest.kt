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
import java.time.YearMonth
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class YearMonthRangeRandomizerTest : AbstractRangeRandomizerTest<YearMonth>() {
  override var min: YearMonth = YearMonth.of(1000, 1)
  override var max: YearMonth = YearMonth.of(3000, 12)

  @BeforeEach
  fun setUp() {
    randomizer = YearMonthRangeRandomizer(min, max)
  }

  @Test
  fun `generated year month should not throw any exception`() {
    shouldNotThrowAny { randomizer.getRandomValue() }
  }

  @Test
  fun `generated year month should be within specified range`() {
    val value = randomizer.getRandomValue()

    value shouldBeIn min..max
  }

  @Test
  fun `generated year month should be always the same for the same seed`() {
    randomizer = YearMonthRangeRandomizer(min, max, SEED)
    val expected = YearMonth.of(2446, 11)

    val randomValue = randomizer.getRandomValue()

    randomValue shouldBe expected
  }

  @Test
  fun `when specified min year month is after max year month then should throw illegal argument exception`() {
    shouldThrow<IllegalArgumentException> { YearMonthRangeRandomizer(max, min) }
  }

  @Test
  fun `when specified min year month is null then should use default min value`() {
    randomizer = YearMonthRangeRandomizer(null, max)

    val randomValue = randomizer.getRandomValue()

    randomValue shouldBeLessThanOrEqualTo max
  }

  @Test
  fun `when specified max year month is null then should use default max value`() {
    randomizer = YearMonthRangeRandomizer(min, null)

    val randomValue = randomizer.getRandomValue()

    randomValue shouldBeGreaterThanOrEqualTo min
  }
}
