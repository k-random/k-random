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

internal class FloatRangeRandomizerTest : AbstractRangeRandomizerTest<Float>() {
  override val min: Float = 0.001F
  override val max: Float = 0.002F

  @BeforeEach
  fun setUp() {
    randomizer = FloatRangeRandomizer(min, max)
  }

  @Test
  fun `generated value should be within specified range`() {
    val randomValue: Float = randomizer.getRandomValue()

    randomValue shouldBeIn min..max
  }

  @Test
  fun `when specified min value is after max value then throw illegal argument exception`() {
    shouldThrow<IllegalArgumentException> { FloatRangeRandomizer(max, min) }
  }

  @Test
  fun `when specified min value is null then should use default min value`() {
    randomizer = FloatRangeRandomizer(null, max)

    val randomFloat = randomizer.getRandomValue()

    randomFloat shouldBeLessThanOrEqualTo max
  }

  @Test
  fun `when specified max value is null then should use default max value`() {
    randomizer = FloatRangeRandomizer(min, null)

    val randomFloat = randomizer.getRandomValue()

    randomFloat shouldBeGreaterThanOrEqualTo min
  }

  @Test
  fun `should always generate the same value for the same seed`() {
    randomizer = FloatRangeRandomizer(min, max, SEED)

    val float = randomizer.getRandomValue()

    float shouldBe 0.0017231742f
  }
}
