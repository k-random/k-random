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
package io.github.krandom.parameters

import io.github.krandom.KRandom
import io.github.krandom.KRandomParameters
import io.github.krandom.beans.TimeBean
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.ranges.shouldBeIn
import java.time.LocalDate
import java.time.LocalTime
import org.junit.jupiter.api.Test

internal class DateTimeRangeParameterTests {
  @Test
  fun `test date range`() {
    val minDate = LocalDate.of(2016, 1, 1)
    val maxDate = LocalDate.of(2016, 1, 31)
    val parameters = KRandomParameters().dateRange(minDate, maxDate)

    val timeBean = KRandom(parameters).nextObject(TimeBean::class.java)

    timeBean.shouldNotBeNull()
    timeBean.localDate shouldBeIn minDate..maxDate
  }

  @Test
  fun `test date max range`() {
    val minDate = LocalDate.MIN
    val maxDate = LocalDate.MAX
    val parameters = KRandomParameters().dateRange(minDate, maxDate)

    val timeBean = KRandom(parameters).nextObject(TimeBean::class.java)

    timeBean.shouldNotBeNull()
    timeBean.localDate shouldBeIn minDate..maxDate
  }

  @Test
  fun `test time range`() {
    val minTime = LocalTime.of(15, 0, 0)
    val maxTime = LocalTime.of(18, 0, 0)
    val parameters = KRandomParameters().timeRange(minTime, maxTime)

    val timeBean = KRandom(parameters).nextObject(TimeBean::class.java)

    timeBean.shouldNotBeNull()
    timeBean.localTime shouldBeIn minTime..maxTime
  }
}
