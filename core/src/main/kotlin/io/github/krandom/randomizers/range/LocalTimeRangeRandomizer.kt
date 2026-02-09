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

import java.time.LocalTime
import kotlin.random.Random

/**
 * Generate a random [LocalTime] in the given range.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class LocalTimeRangeRandomizer
/**
 * Create a new [LocalTimeRangeRandomizer].
 *
 * @param min min value (inclusive)
 * @param max max value (exclusive)
 * @param seed initial seed
 */
@JvmOverloads
constructor(min: LocalTime?, max: LocalTime?, seed: Long = Random.nextLong()) :
  AbstractRangeRandomizer<LocalTime>(min, max, seed) {
  override val defaultMinValue: LocalTime
    get() = LocalTime.MIN

  override val defaultMaxValue: LocalTime
    get() = LocalTime.MAX

  override fun getRandomValue(): LocalTime {
    val minNanoSecond = min.nano
    val minSecond = min.second
    val minMinute = min.minute
    val minHour = min.hour

    val maxNanoSecond = max.nano
    val maxSecond = max.second
    val maxMinute = max.minute
    val maxHour = max.hour

    val randomNanoSecond = nextDouble(minNanoSecond.toDouble(), maxNanoSecond.toDouble()).toInt()
    val randomSecond = nextDouble(minSecond.toDouble(), maxSecond.toDouble()).toInt()
    val randomMinute = nextDouble(minMinute.toDouble(), maxMinute.toDouble()).toInt()
    val randomHour = nextDouble(minHour.toDouble(), maxHour.toDouble()).toInt()
    return LocalTime.of(randomHour, randomMinute, randomSecond, randomNanoSecond)
  }
}
