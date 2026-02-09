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
import java.time.OffsetDateTime
import kotlin.random.Random

/** Generate a random [OffsetDateTime] in the given range. */
class OffsetDateTimeRangeRandomizer
/**
 * Create a new [OffsetDateTimeRangeRandomizer].
 *
 * @param min min value (inclusive)
 * @param max max value (exclusive)
 * @param seed initial seed
 */
@JvmOverloads
constructor(min: OffsetDateTime?, max: OffsetDateTime?, seed: Long = Random.nextLong()) :
  AbstractRangeRandomizer<OffsetDateTime>(min, max, seed) {
  override val defaultMinValue: OffsetDateTime
    get() = KRandomParameters.DEFAULT_DATES_RANGE.min.toOffsetDateTime()

  override val defaultMaxValue: OffsetDateTime
    get() = KRandomParameters.DEFAULT_DATES_RANGE.max.toOffsetDateTime()

  private val zonedDateTimeRangeRandomizer: ZonedDateTimeRangeRandomizer =
    ZonedDateTimeRangeRandomizer(this.min.toZonedDateTime(), this.max.toZonedDateTime(), seed)

  override fun getRandomValue(): OffsetDateTime =
    zonedDateTimeRangeRandomizer.getRandomValue().toOffsetDateTime()
}
