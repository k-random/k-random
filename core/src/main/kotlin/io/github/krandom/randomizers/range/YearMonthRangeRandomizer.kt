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

import io.github.krandom.KRandomParameters
import java.time.YearMonth
import java.time.temporal.ChronoField
import kotlin.random.Random

/** Generate a random [YearMonth] in the given range. */
class YearMonthRangeRandomizer
/**
 * Create a new [YearMonthRangeRandomizer].
 *
 * @param min min value (inclusive)
 * @param max max value (exclusive)
 * @param seed initial seed
 */
@JvmOverloads
constructor(min: YearMonth?, max: YearMonth?, seed: Long = Random.nextLong()) :
  AbstractRangeRandomizer<YearMonth>(min, max, seed) {
  override val defaultMinValue: YearMonth
    get() = KRandomParameters.DEFAULT_DATES_RANGE.min.let { YearMonth.of(it.year, it.month) }

  override val defaultMaxValue: YearMonth
    get() = KRandomParameters.DEFAULT_DATES_RANGE.max.let { YearMonth.of(it.year, it.month) }

  override fun getRandomValue(): YearMonth {
    val minYear = min.getLong(ChronoField.YEAR)
    val maxYear = max.getLong(ChronoField.YEAR)
    val randomYear = nextDouble(minYear.toDouble(), maxYear.toDouble()).toLong()
    val minMonth = min.getLong(ChronoField.MONTH_OF_YEAR)
    val maxMonth = max.getLong(ChronoField.MONTH_OF_YEAR)
    val randomMonth = nextDouble(minMonth.toDouble(), maxMonth.toDouble()).toLong()
    return YearMonth.of(Math.toIntExact(randomYear), Math.toIntExact(randomMonth))
  }
}
