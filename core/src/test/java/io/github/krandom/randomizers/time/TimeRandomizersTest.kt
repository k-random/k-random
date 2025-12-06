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
import io.github.krandom.randomizers.AbstractRandomizerTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import java.sql.Date as SqlDate
import java.sql.Time
import java.sql.Timestamp
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month.MARCH
import java.time.MonthDay
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.Period
import java.time.Year
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.time.temporal.ChronoUnit.HOURS
import java.time.temporal.ChronoUnit.MILLIS
import java.time.temporal.ChronoUnit.MINUTES
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import kotlin.time.toKotlinDuration
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource

internal class TimeRandomizersTest : AbstractRandomizerTest<Randomizer<*>>() {
  @ParameterizedTest
  @MethodSource("generateRandomizers")
  fun `generated time should not be null`(randomizer: Randomizer<*>) {
    // when
    val randomTime: Any? = randomizer.getRandomValue()

    randomTime.shouldNotBeNull()
  }

  @ParameterizedTest
  @MethodSource("generateSeededRandomizersAndTheirExpectedValues")
  fun `should generate the same value for the same seed`(
    randomizer: Randomizer<*>,
    expected: Any?,
  ) {
    // when
    val actual: Any? = randomizer.getRandomValue()

    actual shouldBe expected
  }

  @ParameterizedTest
  @EnumSource(
    value = ChronoUnit::class,
    names = ["NANOS", "MICROS", "MILLIS", "SECONDS", "MINUTES", "HOURS", "HALF_DAYS", "DAYS"],
  )
  fun `should allow to create duration randomizer with suitable temporal units`(unit: ChronoUnit) {
    val randomizer = JavaDurationRandomizer(unit = unit)

    val randomDuration = randomizer.getRandomValue()

    randomDuration shouldBeGreaterThanOrEqualTo Duration.ZERO
  }

  @ParameterizedTest
  @EnumSource(
    value = ChronoUnit::class,
    names = ["WEEKS", "MONTHS", "YEARS", "DECADES", "CENTURIES", "MILLENNIA", "ERAS", "FOREVER"],
  )
  fun `should disallow to create duration randomizer with estimated temporal units`(
    unit: ChronoUnit
  ) {
    shouldThrow<IllegalArgumentException> { JavaDurationRandomizer(unit = unit) }
  }

  companion object {
    @JvmStatic
    fun generateRandomizers() =
      listOf(
        // keep-sorted start
        Arguments.of(CalendarRandomizer()),
        Arguments.of(DateRandomizer()),
        Arguments.of(DurationRandomizer()),
        Arguments.of(GregorianCalendarRandomizer()),
        Arguments.of(InstantRandomizer()),
        Arguments.of(JavaDurationRandomizer()),
        Arguments.of(LocalDateRandomizer()),
        Arguments.of(LocalDateTimeRandomizer()),
        Arguments.of(LocalTimeRandomizer()),
        Arguments.of(MonthDayRandomizer()),
        Arguments.of(OffsetDateTimeRandomizer()),
        Arguments.of(OffsetTimeRandomizer()),
        Arguments.of(PeriodRandomizer()),
        Arguments.of(SqlDateRandomizer()),
        Arguments.of(SqlTimeRandomizer()),
        Arguments.of(SqlTimestampRandomizer()),
        Arguments.of(YearMonthRandomizer()),
        Arguments.of(YearRandomizer()),
        Arguments.of(ZoneOffsetRandomizer()),
        // keep-sorted end
      )

    @JvmStatic
    fun generateSeededRandomizersAndTheirExpectedValues(): List<Arguments> {
      val date = 1718736844570L
      val nanoOfSecond = 723174202
      val inMillis = 5106534569952410475L
      val epochSecond = 1718736844L
      val nanoAdjustment = 570000000L
      val localDateTime = LocalDateTime.of(2024, MARCH, 20, 16, 42, 58, nanoOfSecond)
      val offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.ofTotalSeconds(28923))
      val offsetTime =
        OffsetTime.of(LocalTime.of(16, 42, 58, nanoOfSecond), ZoneOffset.ofTotalSeconds(28923))
      val gregorianCalendar = GregorianCalendar().apply { timeInMillis = inMillis }
      return listOf(
        // keep-sorted start
        Arguments.of(CalendarRandomizer(SEED), Calendar.getInstance().apply { time = Date(date) }),
        Arguments.of(DateRandomizer(SEED), Date(date)),
        Arguments.of(DurationRandomizer(SEED), Duration.of(72L, HOURS).toKotlinDuration()),
        Arguments.of(GregorianCalendarRandomizer(SEED), gregorianCalendar),
        Arguments.of(InstantRandomizer(SEED), Instant.ofEpochSecond(epochSecond, nanoAdjustment)),
        Arguments.of(JavaDurationRandomizer(SEED), Duration.of(72L, HOURS)),
        Arguments.of(JavaDurationRandomizer(SEED, MILLIS), Duration.of(72L, MILLIS)),
        Arguments.of(JavaDurationRandomizer(SEED, MINUTES), Duration.of(72L, MINUTES)),
        Arguments.of(LocalDateRandomizer(SEED), LocalDate.of(2024, MARCH, 20)),
        Arguments.of(LocalDateTimeRandomizer(SEED), localDateTime),
        Arguments.of(LocalTimeRandomizer(SEED), LocalTime.of(16, 42, 58, nanoOfSecond)),
        Arguments.of(MonthDayRandomizer(SEED), MonthDay.of(MARCH, 20)),
        Arguments.of(OffsetDateTimeRandomizer(SEED), offsetDateTime),
        Arguments.of(OffsetTimeRandomizer(SEED), offsetTime),
        Arguments.of(PeriodRandomizer(SEED), Period.of(2024, 3, 20)),
        Arguments.of(SqlDateRandomizer(SEED), SqlDate(date)),
        Arguments.of(SqlTimeRandomizer(SEED), Time(date)),
        Arguments.of(SqlTimestampRandomizer(SEED), Timestamp(date)),
        Arguments.of(YearMonthRandomizer(SEED), YearMonth.of(2024, MARCH)),
        Arguments.of(YearRandomizer(SEED), Year.of(2024)),
        Arguments.of(ZoneOffsetRandomizer(SEED), ZoneOffset.ofTotalSeconds(28923)),
        // keep-sorted end
      )
    }
  }
}
