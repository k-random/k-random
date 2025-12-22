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
import io.github.krandom.randomizers.misc.EnumRandomizer
import java.time.Month
import java.time.MonthDay

/**
 * A [Randomizer] that generates random [MonthDay].
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class MonthDayRandomizer : Randomizer<MonthDay?> {
  private val monthRandomizer: EnumRandomizer<Month>
  private val dayRandomizer: DayRandomizer

  /** Create a new [MonthDayRandomizer]. */
  constructor() {
    monthRandomizer = EnumRandomizer(Month::class)
    dayRandomizer = DayRandomizer()
  }

  /**
   * Create a new [MonthDayRandomizer].
   *
   * @param seed initial seed
   */
  constructor(seed: Long) {
    monthRandomizer = EnumRandomizer(Month::class, seed)
    dayRandomizer = DayRandomizer(seed)
  }

  override fun getRandomValue(): MonthDay {
    val randomMonth = monthRandomizer.getRandomValue()
    val randomDay = dayRandomizer.getRandomValue()
    return MonthDay.of(randomMonth, randomDay)
  }
}
