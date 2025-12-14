package io.github.krandom.validation

import io.github.krandom.KRandom
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.constraints.Future
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.MonthDay
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.Year
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.chrono.HijrahDate
import java.time.chrono.JapaneseDate
import java.time.chrono.MinguoDate
import java.time.chrono.ThaiBuddhistDate
import java.util.Calendar
import java.util.Date
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class FutureAnnotationHandlerTest {
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun generatedBeanShouldBeValidAccordingToValidationConstraints() {
    val testBean = kRandom.nextObject(TestBean::class.java)

    testBean.shouldNotBeNull()
    testBean.testDate shouldBeGreaterThan Date.from(Instant.now())
    testBean.testCalendar shouldBeGreaterThan Calendar.getInstance()
    testBean.testInstant shouldBeGreaterThan Instant.now()
    testBean.testLocalDate shouldBeGreaterThan LocalDate.now()
    testBean.testLocalDateTime shouldBeGreaterThan LocalDateTime.now()
    testBean.testLocalTime shouldBeGreaterThan LocalTime.now(ZoneId.systemDefault())
    testBean.testMonthDay shouldBeGreaterThan MonthDay.now()
    testBean.testOffsetDateTime shouldBeGreaterThan OffsetDateTime.now()
    testBean.testOffsetTime shouldBeGreaterThan OffsetTime.now()
    testBean.testYear shouldBeGreaterThan Year.now()
    testBean.testYearMonth shouldBeGreaterThan YearMonth.now()
    testBean.testZonedDateTime shouldBeGreaterThan ZonedDateTime.now()
    testBean.testHijrahDate shouldBeGreaterThan HijrahDate.now()
    testBean.testJapaneseDate shouldBeGreaterThan JapaneseDate.now()
    testBean.testMinguoDate shouldBeGreaterThan MinguoDate.now()
    testBean.testThaiBuddhistDate shouldBeGreaterThan ThaiBuddhistDate.now()
  }

  @Test
  fun generatedBeanShouldBeValidUsingBeanValidationApi() {
    val testBean = kRandom.nextObject(TestBean::class.java)

    val validator: Validator
    Validation.buildDefaultValidatorFactory().use { validatorFactory ->
      validator = validatorFactory.validator
    }
    val violations = validator.validate(testBean)

    violations.shouldBeEmpty()
  }

  internal data class TestBean(
    @field:Future val testDate: Date,
    @field:Future val testCalendar: Calendar,
    @field:Future val testInstant: Instant,
    @field:Future val testLocalDate: LocalDate,
    @field:Future val testLocalDateTime: LocalDateTime,
    @field:Future val testLocalTime: LocalTime,
    @field:Future val testMonthDay: MonthDay,
    @field:Future val testOffsetDateTime: OffsetDateTime,
    @field:Future val testOffsetTime: OffsetTime,
    @field:Future val testYear: Year,
    @field:Future val testYearMonth: YearMonth,
    @field:Future val testZonedDateTime: ZonedDateTime,
    @field:Future val testHijrahDate: HijrahDate,
    @field:Future val testJapaneseDate: JapaneseDate,
    @field:Future val testMinguoDate: MinguoDate,
    @field:Future val testThaiBuddhistDate: ThaiBuddhistDate,
  )
}
