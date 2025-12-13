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
import java.time.Instant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class InstantRangeRandomizerTest : AbstractRangeRandomizerTest<Instant>() {
  override val min: Instant = Instant.ofEpochMilli(Long.MIN_VALUE)
  override val max: Instant = Instant.ofEpochMilli(Long.MAX_VALUE)

  @BeforeEach
  fun setUp() {
    randomizer = InstantRangeRandomizer(min, max)
  }

  @Test
  fun `generated instant should not throw any exception`() {
    shouldNotThrowAny { randomizer.getRandomValue() }
  }

  @Test
  fun `generated instant should be within specified range`() {
    randomizer.getRandomValue() shouldBeIn min..max
  }

  @Test
  fun `generated instant should be always the same for the same seed`() {
    randomizer = InstantRangeRandomizer(min, max, SEED)
    val expected = Instant.parse("+130459354-01-19T05:47:51.168Z")

    val randomValue = randomizer.getRandomValue()

    randomValue shouldBe expected
  }

  @Test
  fun `when specified min is after max then should throw illegal argument exception`() {
    shouldThrow<IllegalArgumentException> { InstantRangeRandomizer(max, min) }
  }

  @Test
  fun `when specified min is null then should use default min value`() {
    randomizer = InstantRangeRandomizer(null, max)

    val randomValue = randomizer.getRandomValue()

    randomValue shouldBeLessThanOrEqualTo max
  }

  @Test
  fun `when specified max is null then should use default max value`() {
    randomizer = InstantRangeRandomizer(min, null)

    val randomValue = randomizer.getRandomValue()

    randomValue shouldBeGreaterThanOrEqualTo min
  }

  @Test
  fun `when max date is after midnight then should not throw exception`() {
    val min = Instant.parse("2019-10-21T23:33:44.00Z")
    val max = Instant.parse("2019-10-22T00:33:22.00Z")
    randomizer = InstantRangeRandomizer(min, max)

    val randomValue = randomizer.getRandomValue()

    randomValue shouldBeIn min..max
  }
}
