package org.jeasy.random.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class FutureOrPresentAnnotationHandlerTest {

  private EasyRandom easyRandom;

  @BeforeEach
  void setUp() {
    easyRandom = new EasyRandom();
  }

  @Test
  void generatedBeanShouldBeValidAccordingToValidationConstraints() {
    TestBean testBean = easyRandom.nextObject(TestBean.class);

    assertThat(testBean.getTestDate()).isAfterOrEqualTo(Date.from(Instant.now()));
    assertThat(testBean.getTestCalendar()).isGreaterThanOrEqualTo(Calendar.getInstance());
    assertThat(testBean.getTestInstant()).isAfterOrEqualTo(Instant.now());
    assertThat(testBean.getTestLocalDate()).isAfterOrEqualTo(LocalDate.now());
    assertThat(testBean.getTestLocalDateTime()).isAfterOrEqualTo(LocalDateTime.now());
    assertThat(testBean.getTestLocalTime()).isAfterOrEqualTo(LocalTime.now());
    assertThat(testBean.getTestMonthDay()).isGreaterThanOrEqualTo(MonthDay.now());
    assertThat(testBean.getTestOffsetDateTime()).isAfterOrEqualTo(OffsetDateTime.now());
    assertThat(testBean.getTestOffsetTime()).isAfterOrEqualTo(OffsetTime.now());
    assertThat(testBean.getTestYear()).isGreaterThanOrEqualTo(Year.now());
    assertThat(testBean.getTestYearMonth()).isGreaterThanOrEqualTo(YearMonth.now());
    assertThat(testBean.getTestZonedDateTime()).isAfterOrEqualTo(ZonedDateTime.now());
    assertThat(testBean.getTestHijrahDate()).isGreaterThanOrEqualTo(HijrahDate.now());
    assertThat(testBean.getTestJapaneseDate()).isGreaterThanOrEqualTo(JapaneseDate.now());
    assertThat(testBean.getTestMinguoDate()).isGreaterThanOrEqualTo(MinguoDate.now());
    assertThat(testBean.getTestThaiBuddhistDate()).isGreaterThanOrEqualTo(ThaiBuddhistDate.now());
  }

  @Test
  void generatedBeanShouldBeValidUsingBeanValidationApi() {
    TestBean testBean = easyRandom.nextObject(TestBean.class);

    Validator validator;
    try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      validator = validatorFactory.getValidator();
    }
    Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);

    assertThat(violations).isEmpty();
  }

  static class TestBean {
    @FutureOrPresent private Date testDate;

    public Date getTestDate() {
      return testDate;
    }

    @FutureOrPresent private Calendar testCalendar;

    public Calendar getTestCalendar() {
      return testCalendar;
    }

    @FutureOrPresent private Instant testInstant;

    public Instant getTestInstant() {
      return testInstant;
    }

    @FutureOrPresent private LocalDate testLocalDate;

    public LocalDate getTestLocalDate() {
      return testLocalDate;
    }

    @FutureOrPresent private LocalDateTime testLocalDateTime;

    public LocalDateTime getTestLocalDateTime() {
      return testLocalDateTime;
    }

    @FutureOrPresent private LocalTime testLocalTime;

    public LocalTime getTestLocalTime() {
      return testLocalTime;
    }

    @FutureOrPresent private MonthDay testMonthDay;

    public MonthDay getTestMonthDay() {
      return testMonthDay;
    }

    @FutureOrPresent private OffsetDateTime testOffsetDateTime;

    public OffsetDateTime getTestOffsetDateTime() {
      return testOffsetDateTime;
    }

    @FutureOrPresent private OffsetTime testOffsetTime;

    public OffsetTime getTestOffsetTime() {
      return testOffsetTime;
    }

    @FutureOrPresent private Year testYear;

    public Year getTestYear() {
      return testYear;
    }

    @FutureOrPresent private YearMonth testYearMonth;

    public YearMonth getTestYearMonth() {
      return testYearMonth;
    }

    @FutureOrPresent private ZonedDateTime testZonedDateTime;

    public ZonedDateTime getTestZonedDateTime() {
      return testZonedDateTime;
    }

    @FutureOrPresent private HijrahDate testHijrahDate;

    public HijrahDate getTestHijrahDate() {
      return testHijrahDate;
    }

    @FutureOrPresent private JapaneseDate testJapaneseDate;

    public JapaneseDate getTestJapaneseDate() {
      return testJapaneseDate;
    }

    @FutureOrPresent private MinguoDate testMinguoDate;

    public MinguoDate getTestMinguoDate() {
      return testMinguoDate;
    }

    @FutureOrPresent private ThaiBuddhistDate testThaiBuddhistDate;

    public ThaiBuddhistDate getTestThaiBuddhistDate() {
      return testThaiBuddhistDate;
    }
  }
}
