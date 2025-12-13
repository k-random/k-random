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
import java.math.BigDecimal
import java.math.RoundingMode
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BigDecimalRangeRandomizerTest : AbstractRangeRandomizerTest<BigDecimal>() {
  override val min: BigDecimal = BigDecimal("1.1")
  override val max: BigDecimal = BigDecimal("9.9")

  @BeforeEach
  fun setUp() {
    randomizer = BigDecimalRangeRandomizer(min.toDouble(), max.toDouble())
  }

  @Test
  fun `generated value should be within specified range`() {
    val randomValue = randomizer.getRandomValue()

    randomValue shouldBeIn min..max
  }

  @Test
  fun `when specified min value is after max value then throw illegal argument exception`() {
    shouldThrow<IllegalArgumentException> {
      BigDecimalRangeRandomizer(max.toDouble(), min.toDouble())
    }
  }

  @Test
  fun `when specified min value is null then should use default min value`() {
    randomizer = BigDecimalRangeRandomizer(null, max.toDouble())

    val randomBigDecimal = randomizer.getRandomValue()

    randomBigDecimal shouldBeLessThanOrEqualTo max
  }

  @Test
  fun `when specified max value is null then should use default max value`() {
    randomizer = BigDecimalRangeRandomizer(min.toDouble(), null)

    val randomBigDecimal = randomizer.getRandomValue()

    randomBigDecimal shouldBeGreaterThanOrEqualTo min
  }

  @Test
  fun `should always generate the same value for the same seed`() {
    randomizer = BigDecimalRangeRandomizer(min.toDouble(), max.toDouble(), SEED)

    val bigDecimal = randomizer.getRandomValue()

    bigDecimal shouldBe BigDecimal("7.46393298637489266411648713983595371246337890625")
  }

  @Test
  fun `generated value should have provided positive scale`() {
    val scale = 2
    randomizer = BigDecimalRangeRandomizer(min.toDouble(), max.toDouble(), scale = scale)

    val bigDecimal = randomizer.getRandomValue()

    bigDecimal.scale() shouldBe scale
  }

  @Test
  @Throws(NoSuchFieldException::class, IllegalAccessException::class)
  fun `generated value should have provided positive scale and rounding mode`() {
    val scale = 2
    val roundingMode = RoundingMode.DOWN
    randomizer =
      BigDecimalRangeRandomizer(
        min = min.toDouble(),
        max = max.toDouble(),
        scale = scale,
        roundingMode = roundingMode,
      )

    val bigDecimal = randomizer.getRandomValue()

    bigDecimal.scale() shouldBe scale
    (randomizer as BigDecimalRangeRandomizer).roundingMode shouldBe roundingMode
  }

  @Test
  fun `generated value should have provided negative scale`() {
    val scale = -2
    randomizer = BigDecimalRangeRandomizer(min.toDouble(), max.toDouble(), scale = scale)

    val bigDecimal = randomizer.getRandomValue()

    bigDecimal.scale() shouldBe scale
  }

  @Test
  fun `test custom rounding mode`() {
    val scale = 2
    val seed = 123
    val roundingMode = RoundingMode.DOWN
    randomizer =
      BigDecimalRangeRandomizer(min.toDouble(), max.toDouble(), seed.toLong(), scale, roundingMode)

    val bigDecimal = randomizer.getRandomValue()

    bigDecimal shouldBe BigDecimal("7.46")
  }
}
