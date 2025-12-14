package io.github.krandom.validation

import io.github.krandom.KRandom
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import io.kotest.matchers.nulls.shouldNotBeNull
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.constraints.PastOrPresent
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

internal class PastOrPresentAnnotationHandlerTest {
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun `generated bean should be valid according to validation constraints`() {
    val testBean = kRandom.nextObject(TestBean::class.java)

    testBean.shouldNotBeNull()
    testBean.testDate shouldBeLessThanOrEqualTo Date.from(Instant.now())
    testBean.testCalendar shouldBeLessThanOrEqualTo Calendar.getInstance()
    testBean.testInstant shouldBeLessThanOrEqualTo Instant.now()
    testBean.testLocalDate shouldBeLessThanOrEqualTo LocalDate.now()
    testBean.testLocalDateTime shouldBeLessThanOrEqualTo LocalDateTime.now()
    testBean.testLocalTime shouldBeLessThanOrEqualTo LocalTime.now()
    testBean.testMonthDay shouldBeLessThanOrEqualTo MonthDay.now()
    testBean.testOffsetDateTime shouldBeLessThanOrEqualTo OffsetDateTime.now()
    testBean.testOffsetTime shouldBeLessThanOrEqualTo OffsetTime.now()
    testBean.testYear shouldBeLessThanOrEqualTo Year.now()
    testBean.testYearMonth shouldBeLessThanOrEqualTo YearMonth.now()
    testBean.testZonedDateTime shouldBeLessThanOrEqualTo ZonedDateTime.now()
    testBean.testHijrahDate shouldBeLessThanOrEqualTo HijrahDate.now()
    testBean.testJapaneseDate shouldBeLessThanOrEqualTo JapaneseDate.now()
    testBean.testMinguoDate shouldBeLessThanOrEqualTo MinguoDate.now()
    testBean.testThaiBuddhistDate shouldBeLessThanOrEqualTo ThaiBuddhistDate.now()
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
    @field:PastOrPresent val testDate: Date,
    @field:PastOrPresent val testCalendar: Calendar,
    @field:PastOrPresent val testInstant: Instant,
    @field:PastOrPresent val testLocalDate: LocalDate,
    @field:PastOrPresent val testLocalDateTime: LocalDateTime,
    @field:PastOrPresent val testLocalTime: LocalTime,
    @field:PastOrPresent val testMonthDay: MonthDay,
    @field:PastOrPresent val testOffsetDateTime: OffsetDateTime,
    @field:PastOrPresent val testOffsetTime: OffsetTime,
    @field:PastOrPresent val testYear: Year,
    @field:PastOrPresent val testYearMonth: YearMonth,
    @field:PastOrPresent val testZonedDateTime: ZonedDateTime,
    @field:PastOrPresent val testHijrahDate: HijrahDate,
    @field:PastOrPresent val testJapaneseDate: JapaneseDate,
    @field:PastOrPresent val testMinguoDate: MinguoDate,
    @field:PastOrPresent val testThaiBuddhistDate: ThaiBuddhistDate,
  )
}
