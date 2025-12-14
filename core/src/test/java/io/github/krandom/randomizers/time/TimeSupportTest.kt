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

import io.github.krandom.FieldPredicates.inClass
import io.github.krandom.FieldPredicates.named
import io.github.krandom.FieldPredicates.ofType
import io.github.krandom.KRandom
import io.github.krandom.KRandomParameters
import io.github.krandom.beans.TimeBean
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import java.time.Instant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TimeSupportTest {
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun `three ten types should be populated`() {
    val timeBean = kRandom.nextObject(TimeBean::class.java)

    timeBean.shouldNotBeNull()
    timeBean.duration.shouldNotBeNull()
    timeBean.instant.shouldNotBeNull()
    timeBean.localTime.shouldNotBeNull()
    timeBean.localDate.shouldNotBeNull()
    timeBean.localDateTime.shouldNotBeNull()
    timeBean.monthDay.shouldNotBeNull()
    timeBean.offsetDateTime.shouldNotBeNull()
    timeBean.offsetTime.shouldNotBeNull()
    timeBean.period.shouldNotBeNull()
    timeBean.yearMonth.shouldNotBeNull()
    timeBean.year.shouldNotBeNull()
    timeBean.zonedDateTime.shouldNotBeNull()
    timeBean.zoneOffset.shouldNotBeNull()
    timeBean.zoneId.shouldNotBeNull()
  }

  @Test
  fun `three ten randomizers can be overridden by custom randomizers`() {
    val parameters =
      KRandomParameters()
        .excludeField(
          named("instant").and(ofType(Instant::class.java)).and(inClass(TimeBean::class.java))
        )
    kRandom = KRandom(parameters)

    val timeBean = kRandom.nextObject(TimeBean::class.java)

    timeBean.shouldNotBeNull()
    timeBean.instant.shouldBeNull()
  }
}
