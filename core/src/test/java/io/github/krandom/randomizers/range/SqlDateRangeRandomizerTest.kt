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
import io.kotest.matchers.shouldNotBe
import java.sql.Date as SqlDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SqlDateRangeRandomizerTest : AbstractRangeRandomizerTest<SqlDate>() {
  override val min: SqlDate = SqlDate(1460448795091L)
  override val max: SqlDate = SqlDate(1460448795179L)

  @BeforeEach
  fun setUp() {
    randomizer = SqlDateRangeRandomizer(min, max)
  }

  @Test
  fun `generated date should not be null`() {
    randomizer.getRandomValue() shouldNotBe null
  }

  @Test
  fun `generated date should be within specified range`() {
    val value = randomizer.getRandomValue()

    value shouldBeIn min..max
  }

  @Test
  fun `generated date should be always the same for the same seed`() {
    randomizer = SqlDateRangeRandomizer(min, max, SEED)
    val expected = SqlDate(1460448795154L)

    val randomDate = randomizer.getRandomValue()

    randomDate shouldBe expected
  }

  @Test
  fun `when specified min date is after max date then should throw illegal argument exception`() {
    shouldThrow<IllegalArgumentException> { DateRangeRandomizer(max, min) }
  }

  @Test
  fun `when specified min date is null then should use default min value`() {
    randomizer = SqlDateRangeRandomizer(null, max)

    val randomDate = randomizer.getRandomValue()

    randomDate shouldBeLessThanOrEqualTo max
  }

  @Test
  fun `when specified max date is null then should use default max value`() {
    randomizer = SqlDateRangeRandomizer(min, null)

    val randomDate = randomizer.getRandomValue()

    randomDate shouldBeGreaterThanOrEqualTo min
  }
}
