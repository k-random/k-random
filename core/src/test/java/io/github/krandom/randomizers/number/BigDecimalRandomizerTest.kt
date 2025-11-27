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
package io.github.krandom.randomizers.number

import io.github.krandom.randomizers.AbstractRandomizerTest
import io.kotest.matchers.shouldBe
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random.Default.nextLong
import org.junit.jupiter.api.Test

internal class BigDecimalRandomizerTest : AbstractRandomizerTest<BigDecimal>() {
  @Test
  fun `generated value should have provided positive scale`() {
    // given
    val scale = 1
    randomizer = BigDecimalRandomizer(nextLong(), scale)

    // when
    val bigDecimal = randomizer.getRandomValue()

    bigDecimal.scale() shouldBe scale
  }

  @Test
  fun `generated value should have provided negative scale`() {
    // given
    val scale = -1
    randomizer = BigDecimalRandomizer(nextLong(), scale)

    // when
    val bigDecimal = randomizer.getRandomValue()

    bigDecimal.scale() shouldBe scale
  }

  @Test
  fun `test custom rounding mode`() {
    // given
    val initialSeed: Long = 123
    val scale = 1
    val roundingMode = RoundingMode.DOWN
    randomizer = BigDecimalRandomizer(initialSeed, scale, roundingMode)

    // when
    val bigDecimal = randomizer.getRandomValue()

    bigDecimal shouldBe BigDecimal("0.7")
  }
}
