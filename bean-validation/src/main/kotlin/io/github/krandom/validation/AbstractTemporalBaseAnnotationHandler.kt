package io.github.krandom.validation

import io.github.krandom.KRandom
import io.github.krandom.api.Randomizer
import io.github.krandom.randomizers.range.DateRangeRandomizer
import io.github.krandom.randomizers.range.InstantRangeRandomizer
import io.github.krandom.randomizers.range.LocalDateRangeRandomizer
import io.github.krandom.randomizers.range.LocalDateTimeRangeRandomizer
import io.github.krandom.randomizers.range.LocalTimeRangeRandomizer
import io.github.krandom.randomizers.range.MonthDayRangeRandomizer
import io.github.krandom.randomizers.range.OffsetDateTimeRangeRandomizer
import io.github.krandom.randomizers.range.OffsetTimeRangeRandomizer
import io.github.krandom.randomizers.range.YearMonthRangeRandomizer
import io.github.krandom.randomizers.range.YearRangeRandomizer
import io.github.krandom.randomizers.range.ZonedDateTimeRangeRandomizer
import java.lang.reflect.Field
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.MonthDay
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.Year
import java.time.YearMonth
import java.time.ZonedDateTime
import java.time.chrono.HijrahDate
import java.time.chrono.JapaneseDate
import java.time.chrono.MinguoDate
import java.time.chrono.ThaiBuddhistDate
import java.util.Calendar
import java.util.Date
import kotlin.getValue

/** @author Alex Schiff */
open class AbstractTemporalBaseAnnotationHandler
protected constructor(protected val zonedDateTimeRange: ClosedRange<ZonedDateTime>) :
  BeanValidationAnnotationHandler {
  private val randomizerByType: Map<Class<*>, Randomizer<*>> by lazy {
    val date =
      DateRangeRandomizer(
          Date.from(zonedDateTimeRange.start.toInstant()),
          Date.from(zonedDateTimeRange.endInclusive.toInstant()),
        )
        .getRandomValue()
    mapOf(
      Date::class.java to Randomizer { date },
      Calendar::class.java to
        Randomizer {
          val calendar = Calendar.getInstance()
          calendar.time = date
          calendar
        },
      Instant::class.java to
        Randomizer {
          InstantRangeRandomizer(
              zonedDateTimeRange.start.toInstant(),
              zonedDateTimeRange.endInclusive.toInstant(),
            )
            .getRandomValue()
        },
      LocalDate::class.java to
        Randomizer {
          LocalDateRangeRandomizer(
              zonedDateTimeRange.start.toLocalDate(),
              zonedDateTimeRange.endInclusive.toLocalDate(),
            )
            .getRandomValue()
        },
      LocalDateTime::class.java to
        Randomizer {
          LocalDateTimeRangeRandomizer(
              zonedDateTimeRange.start.toLocalDateTime(),
              zonedDateTimeRange.endInclusive.toLocalDateTime(),
            )
            .getRandomValue()
        },
      LocalTime::class.java to
        Randomizer {
          LocalTimeRangeRandomizer(
              zonedDateTimeRange.start.toLocalTime(),
              zonedDateTimeRange.endInclusive.toLocalTime(),
            )
            .getRandomValue()
        },
      MonthDay::class.java to
        Randomizer {
          MonthDayRangeRandomizer(
              MonthDay.from(zonedDateTimeRange.start),
              MonthDay.from(zonedDateTimeRange.endInclusive),
            )
            .getRandomValue()
        },
      OffsetDateTime::class.java to
        Randomizer {
          OffsetDateTimeRangeRandomizer(
              zonedDateTimeRange.start.toOffsetDateTime(),
              zonedDateTimeRange.endInclusive.toOffsetDateTime(),
            )
            .getRandomValue()
        },
      OffsetTime::class.java to
        Randomizer {
          OffsetTimeRangeRandomizer(
              zonedDateTimeRange.start.toOffsetDateTime().toOffsetTime(),
              zonedDateTimeRange.endInclusive.toOffsetDateTime().toOffsetTime(),
            )
            .getRandomValue()
        },
      Year::class.java to
        Randomizer {
          YearRangeRandomizer(
              Year.from(zonedDateTimeRange.start),
              Year.from(zonedDateTimeRange.endInclusive),
            )
            .getRandomValue()
        },
      YearMonth::class.java to
        Randomizer {
          YearMonthRangeRandomizer(
              YearMonth.from(zonedDateTimeRange.start),
              YearMonth.from(zonedDateTimeRange.endInclusive),
            )
            .getRandomValue()
        },
      ZonedDateTime::class.java to
        Randomizer {
          ZonedDateTimeRangeRandomizer(zonedDateTimeRange.start, zonedDateTimeRange.endInclusive)
            .getRandomValue()
        },
      HijrahDate::class.java to
        Randomizer {
          HijrahDate.from(
            LocalDateRangeRandomizer(
                zonedDateTimeRange.start.toLocalDate(),
                zonedDateTimeRange.endInclusive.toLocalDate(),
              )
              .getRandomValue()
          )
        },
      JapaneseDate::class.java to
        Randomizer {
          JapaneseDate.from(
            LocalDateRangeRandomizer(
                zonedDateTimeRange.start.toLocalDate(),
                zonedDateTimeRange.endInclusive.toLocalDate(),
              )
              .getRandomValue()
          )
        },
      MinguoDate::class.java to
        Randomizer {
          MinguoDate.from(
            LocalDateRangeRandomizer(
                zonedDateTimeRange.start.toLocalDate(),
                zonedDateTimeRange.endInclusive.toLocalDate(),
              )
              .getRandomValue()
          )
        },
      ThaiBuddhistDate::class.java to
        Randomizer {
          ThaiBuddhistDate.from(
            LocalDateRangeRandomizer(
                zonedDateTimeRange.start.toLocalDate(),
                zonedDateTimeRange.endInclusive.toLocalDate(),
              )
              .getRandomValue()
          )
        },
    )
  }

  override fun getRandomizer(field: Field): Randomizer<*>? =
    randomizerByType[field.type] ?: Randomizer { KRandom().nextObject(field.type) }
}
