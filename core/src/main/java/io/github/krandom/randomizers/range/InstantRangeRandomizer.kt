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

import java.time.Instant
import kotlin.random.Random

/**
 * Generate random [Instant] values within a range.
 * - Range is `[min, max)`: `min` inclusive, `max` exclusive.
 * - If `min` or `max` is `null`, the bound defaults to the smallest or largest representable epoch
 *   milli (`Long.MIN_VALUE` / `Long.MAX_VALUE`).
 * - Providing the same `seed` yields deterministic results.
 */
class InstantRangeRandomizer
/**
 * Create a new [InstantRangeRandomizer].
 *
 * @param min min value (inclusive)
 * @param max max value (exclusive)
 * @param seed initial seed
 */
@JvmOverloads
constructor(min: Instant?, max: Instant?, seed: Long = Random.nextLong()) :
  AbstractRangeRandomizer<Instant>(min, max, seed) {
  override val defaultMinValue: Instant
    get() = Instant.ofEpochMilli(Long.MIN_VALUE)

  override val defaultMaxValue: Instant
    get() = Instant.ofEpochMilli(Long.MAX_VALUE)

  override fun getRandomValue(): Instant {
    val minEpochMillis: Long =
      try {
        min.toEpochMilli()
      } catch (_: ArithmeticException) {
        Long.MIN_VALUE
      }

    val maxEpochMillis: Long =
      try {
        max.toEpochMilli()
      } catch (_: ArithmeticException) {
        Long.MAX_VALUE
      }

    val randomEpochMillis =
      nextDouble(minEpochMillis.toDouble(), maxEpochMillis.toDouble()).toLong()
    return Instant.ofEpochMilli(randomEpochMillis)
  }
}
