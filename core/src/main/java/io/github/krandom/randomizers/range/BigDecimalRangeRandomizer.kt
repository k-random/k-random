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

import io.github.krandom.api.Randomizer
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

/**
 * Generate a random [BigDecimal] in the given range.
 *
 * @author Rémi Alvergnat (toilal.dev@gmail.com)
 */
class BigDecimalRangeRandomizer
/**
 * Create a new [BigDecimalRangeRandomizer].
 *
 * @param min min value (inclusive)
 * @param max max value (exclusive)
 * @param seed initial seed
 * @param scale of the `BigDecimal` value to be returned.
 * @param roundingMode of the `BigDecimal` value to be returned.
 * @param delegate the delegate randomizer for the double value.
 */
@JvmOverloads
constructor(
  min: Double? = null,
  max: Double? = null,
  seed: Long = Random.nextLong(),
  private val scale: Int? = null,
  internal val roundingMode: RoundingMode = RoundingMode.HALF_UP,
  private val delegate: DoubleRangeRandomizer = DoubleRangeRandomizer(min, max, seed),
) : Randomizer<BigDecimal> {
  override fun getRandomValue(): BigDecimal =
    BigDecimal(delegate.getRandomValue()).let { bd ->
      scale?.let { bd.setScale(it, roundingMode) } ?: bd
    }
}
