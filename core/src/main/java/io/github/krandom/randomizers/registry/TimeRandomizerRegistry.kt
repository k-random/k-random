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
package io.github.krandom.randomizers.registry

import io.github.krandom.KRandomParameters
import io.github.krandom.annotation.Priority
import io.github.krandom.api.Randomizer
import io.github.krandom.api.RandomizerRegistry
import io.github.krandom.randomizers.range.InstantRangeRandomizer
import io.github.krandom.randomizers.range.LocalDateRangeRandomizer
import io.github.krandom.randomizers.range.LocalDateTimeRangeRandomizer
import io.github.krandom.randomizers.range.LocalTimeRangeRandomizer
import io.github.krandom.randomizers.range.OffsetDateTimeRangeRandomizer
import io.github.krandom.randomizers.range.OffsetTimeRangeRandomizer
import io.github.krandom.randomizers.range.YearMonthRangeRandomizer
import io.github.krandom.randomizers.range.YearRangeRandomizer
import io.github.krandom.randomizers.range.ZonedDateTimeRangeRandomizer
import io.github.krandom.randomizers.time.DurationRandomizer
import io.github.krandom.randomizers.time.GregorianCalendarRandomizer
import io.github.krandom.randomizers.time.MonthDayRandomizer
import io.github.krandom.randomizers.time.PeriodRandomizer
import io.github.krandom.randomizers.time.TimeZoneRandomizer
import io.github.krandom.randomizers.time.ZoneIdRandomizer
import io.github.krandom.randomizers.time.ZoneOffsetRandomizer
import java.lang.reflect.Field
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.MonthDay
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.Period
import java.time.Year
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.GregorianCalendar
import java.util.TimeZone
import kotlin.reflect.KClass

private const val PRIORITY = -3

/**
 * A registry of randomizers for Java 8 JSR 310 types.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
@Priority(PRIORITY)
class TimeRandomizerRegistry : RandomizerRegistry {
  private val randomizers: MutableMap<KClass<*>, Randomizer<*>> = mutableMapOf()

  override fun init(parameters: KRandomParameters) {
    val seed = parameters.seed
    val minDate = parameters.dateRange.min
    val maxDate = parameters.dateRange.max
    val minTime = parameters.timeRange.min
    val maxTime = parameters.timeRange.max
    randomizers[Duration::class] = DurationRandomizer(seed)
    randomizers[GregorianCalendar::class] = GregorianCalendarRandomizer(seed)
    randomizers[Instant::class] =
      InstantRangeRandomizer(
        minDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
        maxDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
        seed,
      )
    randomizers[LocalDate::class] = LocalDateRangeRandomizer(minDate, maxDate, seed)
    randomizers[LocalDateTime::class] =
      LocalDateTimeRangeRandomizer(
        LocalDateTime.of(minDate, minTime),
        LocalDateTime.of(maxDate, maxTime),
        seed,
      )
    randomizers[LocalTime::class] = LocalTimeRangeRandomizer(minTime, maxTime, seed)
    randomizers[MonthDay::class] = MonthDayRandomizer(seed)
    randomizers[OffsetDateTime::class] =
      OffsetDateTimeRangeRandomizer(
        toOffsetDateTime(minDate, minTime),
        toOffsetDateTime(maxDate, maxTime),
        seed,
      )
    randomizers[OffsetTime::class] =
      OffsetTimeRangeRandomizer(
        minTime.atOffset(OffsetDateTime.now().offset),
        maxTime.atOffset(OffsetDateTime.now().offset),
        seed,
      )
    randomizers[Period::class] = PeriodRandomizer(seed)
    randomizers[TimeZone::class] = TimeZoneRandomizer(seed)
    randomizers[YearMonth::class] =
      YearMonthRangeRandomizer(
        YearMonth.of(minDate.year, minDate.month),
        YearMonth.of(maxDate.year, maxDate.month),
        seed,
      )
    randomizers[Year::class] =
      YearRangeRandomizer(Year.of(minDate.year), Year.of(maxDate.year), seed)
    randomizers[ZonedDateTime::class] =
      ZonedDateTimeRangeRandomizer(
        toZonedDateTime(minDate, minTime),
        toZonedDateTime(maxDate, maxTime),
        seed,
      )
    randomizers[ZoneOffset::class] = ZoneOffsetRandomizer(seed)
    randomizers[ZoneId::class] = ZoneIdRandomizer(seed)
  }

  override fun getRandomizer(field: Field): Randomizer<*>? {
    return getRandomizer(field.type)
  }

  override fun getRandomizer(type: Class<*>): Randomizer<*>? {
    return randomizers.get(type.kotlin)
  }

  companion object {
    @JvmStatic
    private fun toZonedDateTime(localDate: LocalDate, localTime: LocalTime): ZonedDateTime =
      LocalDateTime.of(localDate, localTime).atZone(ZoneId.systemDefault())

    @JvmStatic
    private fun toOffsetDateTime(localDate: LocalDate, localTime: LocalTime): OffsetDateTime {
      return LocalDateTime.of(localDate, localTime).atOffset(OffsetDateTime.now().offset)
    }
  }
}
