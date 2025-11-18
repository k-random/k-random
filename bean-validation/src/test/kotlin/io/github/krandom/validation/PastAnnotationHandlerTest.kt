package io.github.krandom.validation

import io.github.krandom.KRandom
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.comparables.shouldBeLessThan
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.constraints.Past
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

internal class PastAnnotationHandlerTest {
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun `generated bean should be valid according to validation constraints`() {
    val testBean = kRandom.nextObject(TestBean::class.java)

    testBean.testDate shouldBeLessThan Date.from(Instant.now())
    testBean.testCalendar shouldBeLessThan Calendar.getInstance()
    testBean.testInstant shouldBeLessThan Instant.now()
    testBean.testLocalDate shouldBeLessThan LocalDate.now()
    testBean.testLocalDateTime shouldBeLessThan LocalDateTime.now()
    testBean.testLocalTime shouldBeLessThan LocalTime.now()
    testBean.testMonthDay shouldBeLessThan MonthDay.now()
    testBean.testOffsetDateTime shouldBeLessThan OffsetDateTime.now()
    testBean.testOffsetTime shouldBeLessThan OffsetTime.now()
    testBean.testYear shouldBeLessThan Year.now()
    testBean.testYearMonth shouldBeLessThan YearMonth.now()
    testBean.testZonedDateTime shouldBeLessThan ZonedDateTime.now()
    testBean.testHijrahDate shouldBeLessThan HijrahDate.now()
    testBean.testJapaneseDate shouldBeLessThan JapaneseDate.now()
    testBean.testMinguoDate shouldBeLessThan MinguoDate.now()
    testBean.testThaiBuddhistDate shouldBeLessThan ThaiBuddhistDate.now()
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
    @field:Past val testDate: Date,
    @field:Past val testCalendar: Calendar,
    @field:Past val testInstant: Instant,
    @field:Past val testLocalDate: LocalDate,
    @field:Past val testLocalDateTime: LocalDateTime,
    @field:Past val testLocalTime: LocalTime,
    @field:Past val testMonthDay: MonthDay,
    @field:Past val testOffsetDateTime: OffsetDateTime,
    @field:Past val testOffsetTime: OffsetTime,
    @field:Past val testYear: Year,
    @field:Past val testYearMonth: YearMonth,
    @field:Past val testZonedDateTime: ZonedDateTime,
    @field:Past val testHijrahDate: HijrahDate,
    @field:Past val testJapaneseDate: JapaneseDate,
    @field:Past val testMinguoDate: MinguoDate,
    @field:Past val testThaiBuddhistDate: ThaiBuddhistDate,
  )
}
