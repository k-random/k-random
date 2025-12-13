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

import io.github.krandom.KRandomParameters
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import io.kotest.matchers.ranges.shouldBeIn
import io.kotest.matchers.shouldBe
import java.time.OffsetDateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class OffsetDateTimeRangeRandomizerTest : AbstractRangeRandomizerTest<OffsetDateTime>() {
  override val min: OffsetDateTime =
    KRandomParameters.DEFAULT_DATES_RANGE.getMin().toOffsetDateTime().minusYears(50)
  override val max: OffsetDateTime =
    KRandomParameters.DEFAULT_DATES_RANGE.getMax().toOffsetDateTime().plusYears(50)

  @BeforeEach
  fun setUp() {
    randomizer = OffsetDateTimeRangeRandomizer(min, max)
  }

  @Test
  fun `generated offset date time should not throw any exception`() {
    shouldNotThrowAny { randomizer.getRandomValue() }
  }

  @Test
  fun `generated offset date time should be within specified range`() {
    val value = randomizer.getRandomValue()

    value shouldBeIn min..max
  }

  @Test
  fun `generated offset date time should be always the same for the same seed`() {
    randomizer = OffsetDateTimeRangeRandomizer(min, max, SEED)
    val expected = OffsetDateTime.parse("2046-10-12T17:24:27Z")

    val randomValue = randomizer.getRandomValue()

    randomValue shouldBe expected
  }

  @Test
  fun `when specified min is after max then should throw illegal argument exception`() {
    shouldThrow<IllegalArgumentException> { OffsetDateTimeRangeRandomizer(max, min) }
  }

  @Test
  fun `when specified min is null then should use default min value`() {
    randomizer = OffsetDateTimeRangeRandomizer(null, max)

    val randomValue = randomizer.getRandomValue()

    randomValue shouldBeLessThanOrEqualTo max
  }

  @Test
  fun `when specified max is null then should use default max value`() {
    randomizer = OffsetDateTimeRangeRandomizer(min, null)

    val randomValue = randomizer.getRandomValue()

    randomValue shouldBeGreaterThanOrEqualTo min
  }
}
