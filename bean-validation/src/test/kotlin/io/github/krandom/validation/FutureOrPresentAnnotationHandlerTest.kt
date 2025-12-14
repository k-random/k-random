package io.github.krandom.validation

import io.github.krandom.KRandom
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.nulls.shouldNotBeNull
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.constraints.FutureOrPresent
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class FutureOrPresentAnnotationHandlerTest {
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun `generated bean should be valid according to validation constraints`() {
    val testBean = kRandom.nextObject(TestBean::class.java)

    testBean.shouldNotBeNull()
    testBean.testDate shouldBeGreaterThanOrEqualTo Date.from(Instant.now())
    testBean.testCalendar shouldBeGreaterThanOrEqualTo Calendar.getInstance()
    testBean.testInstant shouldBeGreaterThanOrEqualTo Instant.now()
    testBean.testLocalDate shouldBeGreaterThanOrEqualTo LocalDate.now()
    testBean.testLocalDateTime shouldBeGreaterThanOrEqualTo LocalDateTime.now()
    testBean.testLocalTime shouldBeGreaterThanOrEqualTo LocalTime.now()
    testBean.testMonthDay shouldBeGreaterThanOrEqualTo MonthDay.now()
    testBean.testOffsetDateTime shouldBeGreaterThanOrEqualTo OffsetDateTime.now()
    testBean.testOffsetTime shouldBeGreaterThanOrEqualTo OffsetTime.now()
    testBean.testYear shouldBeGreaterThanOrEqualTo Year.now()
    testBean.testYearMonth shouldBeGreaterThanOrEqualTo YearMonth.now()
    testBean.testZonedDateTime shouldBeGreaterThanOrEqualTo ZonedDateTime.now()
    testBean.testHijrahDate shouldBeGreaterThanOrEqualTo HijrahDate.now()
    testBean.testJapaneseDate shouldBeGreaterThanOrEqualTo JapaneseDate.now()
    testBean.testMinguoDate shouldBeGreaterThanOrEqualTo MinguoDate.now()
    testBean.testThaiBuddhistDate shouldBeGreaterThanOrEqualTo ThaiBuddhistDate.now()
  }

  @Test
  fun `generated bean should be valid using bean validation api`() {
    val testBean = kRandom.nextObject(TestBean::class.java)

    val validator: Validator
    Validation.buildDefaultValidatorFactory().use { validatorFactory ->
      validator = validatorFactory.validator
    }
    val violations = validator.validate(testBean)

    violations.shouldBeEmpty()
  }

  internal data class TestBean(
    @field:FutureOrPresent val testDate: Date,
    @field:FutureOrPresent val testCalendar: Calendar,
    @field:FutureOrPresent val testInstant: Instant,
    @field:FutureOrPresent val testLocalDate: LocalDate,
    @field:FutureOrPresent val testLocalDateTime: LocalDateTime,
    @field:FutureOrPresent val testLocalTime: LocalTime,
    @field:FutureOrPresent val testMonthDay: MonthDay,
    @field:FutureOrPresent val testOffsetDateTime: OffsetDateTime,
    @field:FutureOrPresent val testOffsetTime: OffsetTime,
    @field:FutureOrPresent val testYear: Year,
    @field:FutureOrPresent val testYearMonth: YearMonth,
    @field:FutureOrPresent val testZonedDateTime: ZonedDateTime,
    @field:FutureOrPresent val testHijrahDate: HijrahDate,
    @field:FutureOrPresent val testJapaneseDate: JapaneseDate,
    @field:FutureOrPresent val testMinguoDate: MinguoDate,
    @field:FutureOrPresent val testThaiBuddhistDate: ThaiBuddhistDate,
  )
}
