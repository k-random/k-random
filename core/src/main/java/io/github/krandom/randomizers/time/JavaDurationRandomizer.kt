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
package io.github.krandom.randomizers.time

import io.github.krandom.api.Randomizer
import io.github.krandom.randomizers.range.IntRangeRandomizer
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
import kotlin.random.Random

/**
 * A [Randomizer] that generates random [Duration].
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class JavaDurationRandomizer
/**
 * Create a new [JavaDurationRandomizer].
 *
 * @param seed initial seed
 * @param unit the unit of the duration to generate
 * @param amountRandomizer the randomizer for the duration amount
 */
@JvmOverloads
constructor(
  seed: Long = Random.nextLong(),
  private val unit: TemporalUnit = ChronoUnit.HOURS,
  private val amountRandomizer: IntRangeRandomizer =
    IntRangeRandomizer(DURATION_RANGE.first, DURATION_RANGE.last, seed),
) : Randomizer<Duration> {
  init {
    requireValid(unit)
  }

  override fun getRandomValue(): Duration {
    val randomAmount = amountRandomizer.getRandomValue()
    return Duration.of(randomAmount.toLong(), unit)
  }

  companion object Companion {
    val DURATION_RANGE = 0..100

    @JvmStatic
    private fun requireValid(unit: TemporalUnit) =
      require(!(unit.isDurationEstimated && unit !== ChronoUnit.DAYS)) {
        "Temporal unit $unit can't be used to create Duration objects"
      }
  }
}
