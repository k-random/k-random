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
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.random.Random

/**
 * Generate a random [LocalDateTime] in the given range.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class LocalDateTimeRangeRandomizer
/**
 * Create a new [LocalDateTimeRangeRandomizer].
 *
 * @param min min value (inclusive)
 * @param max max value (exclusive)
 * @param seed initial seed
 */
@JvmOverloads
constructor(min: LocalDateTime?, max: LocalDateTime?, seed: Long = Random.nextLong()) :
  AbstractRangeRandomizer<LocalDateTime>(min, max, seed) {
  override val defaultMinValue: LocalDateTime
    get() = LocalDateTime.MIN

  override val defaultMaxValue: LocalDateTime
    get() = LocalDateTime.MAX

  override fun getRandomValue(): LocalDateTime {
    val minSeconds = min.toEpochSecond(ZoneOffset.UTC)
    val maxSeconds = max.toEpochSecond(ZoneOffset.UTC)
    val seconds = nextDouble(minSeconds.toDouble(), maxSeconds.toDouble()).toLong()
    val minNanoSeconds = min.nano
    val maxNanoSeconds = max.nano
    val nanoSeconds = nextDouble(minNanoSeconds.toDouble(), maxNanoSeconds.toDouble()).toLong()
    val instant = Instant.ofEpochSecond(seconds, nanoSeconds)

    return LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
  }
}
