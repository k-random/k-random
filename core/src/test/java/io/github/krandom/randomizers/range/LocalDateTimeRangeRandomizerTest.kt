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
import java.time.LocalDateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class LocalDateTimeRangeRandomizerTest : AbstractRangeRandomizerTest<LocalDateTime>() {
  override val min: LocalDateTime = LocalDateTime.MIN
  override val max: LocalDateTime = LocalDateTime.MAX

  @BeforeEach
  fun setUp() {
    randomizer = LocalDateTimeRangeRandomizer(min, max)
  }

  @Test
  fun `generated local date time should not throw any exception`() {
    shouldNotThrowAny { randomizer.getRandomValue() }
  }

  @Test
  fun `generated local date time should be within specified range`() {
    randomizer.getRandomValue() shouldBeIn min..max
  }

  @Test
  fun `generated local date time should be always the same for the same seed`() {
    randomizer = LocalDateTimeRangeRandomizer(min, max, SEED)
    val expected = LocalDateTime.parse("+446348406-04-09T16:32:16.990898895")

    val randomValue = randomizer.getRandomValue()

    randomValue shouldBe expected
  }

  @Test
  fun `when specified min date time is after max then should throw illegal argument exception`() {
    shouldThrow<IllegalArgumentException> { LocalDateTimeRangeRandomizer(max, min) }
  }

  @Test
  fun `when specified min is null then should use default min value`() {
    randomizer = LocalDateTimeRangeRandomizer(null, max)

    val randomValue = randomizer.getRandomValue()

    randomValue shouldBeLessThanOrEqualTo max
  }

  @Test
  fun `when specified max is null then should use default max value`() {
    randomizer = LocalDateTimeRangeRandomizer(min, null)

    val randomValue = randomizer.getRandomValue()

    randomValue shouldBeGreaterThanOrEqualTo min
  }

  @Test
  fun `when max date is after midnight then should not throw exception`() {
    val min = LocalDateTime.parse("2019-10-21T23:33:44")
    val max = LocalDateTime.parse("2019-10-22T00:33:22")
    randomizer = LocalDateTimeRangeRandomizer(min, max)

    val randomValue = randomizer.getRandomValue()

    randomValue shouldBeIn min..max
  }
}
