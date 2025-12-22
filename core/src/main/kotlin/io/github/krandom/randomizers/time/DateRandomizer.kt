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

import io.github.krandom.KRandomParameters
import io.github.krandom.api.Randomizer
import io.github.krandom.randomizers.range.DateRangeRandomizer
import java.time.ZonedDateTime
import java.util.Date
import kotlin.random.Random

/**
 * Generate a random [Date].
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class DateRandomizer
/**
 * Create a new [DateRandomizer].
 *
 * @param seed initial seed
 */
@JvmOverloads
constructor(
  seed: Long = Random.nextLong(),
  private val delegate: DateRangeRandomizer =
    DateRangeRandomizer(
      toDate(KRandomParameters.DEFAULT_DATES_RANGE.min),
      toDate(KRandomParameters.DEFAULT_DATES_RANGE.max),
      seed,
    ),
) : Randomizer<Date> {
  override fun getRandomValue(): Date {
    return delegate.getRandomValue()
  }

  companion object {
    @JvmStatic
    private fun toDate(zonedDateTime: ZonedDateTime): Date {
      return Date.from(zonedDateTime.toInstant())
    }
  }
}
