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

import java.sql.Date as SqlDate
import kotlin.random.Random

/**
 * Generate a random [SqlDate] in a given range.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class SqlDateRangeRandomizer
/**
 * Create a new [SqlDateRangeRandomizer].
 *
 * @param min min value (inclusive)
 * @param max max value (exclusive)
 * @param seed initial seed
 */
@JvmOverloads
constructor(min: SqlDate?, max: SqlDate?, seed: Long = Random.nextLong()) :
  AbstractRangeRandomizer<SqlDate>(min, max, seed) {
  override val defaultMinValue: SqlDate
    get() = SqlDate(Long.MIN_VALUE)

  override val defaultMaxValue: SqlDate
    get() = SqlDate(Long.MAX_VALUE)

  override fun getRandomValue(): SqlDate {
    val minDateTime = min.time
    val maxDateTime = max.time
    val randomDateTime = nextDouble(minDateTime.toDouble(), maxDateTime.toDouble()).toLong()
    return SqlDate(randomDateTime)
  }
}
