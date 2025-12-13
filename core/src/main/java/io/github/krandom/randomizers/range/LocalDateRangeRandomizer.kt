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

import java.time.LocalDate
import java.time.temporal.ChronoField
import kotlin.random.Random

/**
 * Generate a random [LocalDate] in the given range.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class LocalDateRangeRandomizer
/**
 * Create a new [LocalDateRangeRandomizer].
 *
 * @param min min value (inclusive)
 * @param max max value (exclusive)
 * @param seed initial seed
 */
@JvmOverloads
constructor(min: LocalDate?, max: LocalDate?, seed: Long = Random.nextLong()) :
  AbstractRangeRandomizer<LocalDate>(min, max, seed) {
  override val defaultMinValue: LocalDate
    get() = LocalDate.MIN

  override val defaultMaxValue: LocalDate
    get() = LocalDate.MAX

  override fun getRandomValue(): LocalDate {
    val minEpochDay = min.getLong(ChronoField.EPOCH_DAY)
    val maxEpochDay = max.getLong(ChronoField.EPOCH_DAY)
    val randomEpochDay = nextDouble(minEpochDay.toDouble(), maxEpochDay.toDouble()).toLong()
    return LocalDate.ofEpochDay(randomEpochDay)
  }
}
