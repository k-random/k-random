package org.jeasy.random.validation;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.PastOrPresent;
import java.time.*;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PastOrPresentAnnotationHandlerTest {

  private EasyRandom easyRandom;

  @BeforeEach
  void setUp() {
    easyRandom = new EasyRandom();
  }

  @Test
  void generatedBeanShouldBeValidAccordingToValidationConstraints() {
    TestBean testBean = easyRandom.nextObject(TestBean.class);

    assertThat(testBean.getTestDate()).isBeforeOrEqualTo(Date.from(Instant.now()));
    assertThat(testBean.getTestCalendar()).isLessThanOrEqualTo(Calendar.getInstance());
    assertThat(testBean.getTestInstant()).isBeforeOrEqualTo(Instant.now());
    assertThat(testBean.getTestLocalDate()).isBeforeOrEqualTo(LocalDate.now());
    assertThat(testBean.getTestLocalDateTime()).isBeforeOrEqualTo(LocalDateTime.now());
    assertThat(testBean.getTestLocalTime()).isBeforeOrEqualTo(LocalTime.now());
    assertThat(testBean.getTestMonthDay()).isLessThanOrEqualTo(MonthDay.now());
    assertThat(testBean.getTestOffsetDateTime()).isBeforeOrEqualTo(OffsetDateTime.now());
    assertThat(testBean.getTestOffsetTime()).isBeforeOrEqualTo(OffsetTime.now());
    assertThat(testBean.getTestYear()).isLessThanOrEqualTo(Year.now());
    assertThat(testBean.getTestYearMonth()).isLessThanOrEqualTo(YearMonth.now());
    assertThat(testBean.getTestZonedDateTime()).isBeforeOrEqualTo(ZonedDateTime.now());
    assertThat(testBean.getTestHijrahDate()).isLessThanOrEqualTo(HijrahDate.now());
    assertThat(testBean.getTestJapaneseDate()).isLessThanOrEqualTo(JapaneseDate.now());
    assertThat(testBean.getTestMinguoDate()).isLessThanOrEqualTo(MinguoDate.now());
    assertThat(testBean.getTestThaiBuddhistDate()).isLessThanOrEqualTo(ThaiBuddhistDate.now());
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
    @PastOrPresent private Date testDate;

    public Date getTestDate() {
      return testDate;
    }

    @PastOrPresent private Calendar testCalendar;

    public Calendar getTestCalendar() {
      return testCalendar;
    }

    @PastOrPresent private Instant testInstant;

    public Instant getTestInstant() {
      return testInstant;
    }

    @PastOrPresent private LocalDate testLocalDate;

    public LocalDate getTestLocalDate() {
      return testLocalDate;
    }

    @PastOrPresent private LocalDateTime testLocalDateTime;

    public LocalDateTime getTestLocalDateTime() {
      return testLocalDateTime;
    }

    @PastOrPresent private LocalTime testLocalTime;

    public LocalTime getTestLocalTime() {
      return testLocalTime;
    }

    @PastOrPresent private MonthDay testMonthDay;

    public MonthDay getTestMonthDay() {
      return testMonthDay;
    }

    @PastOrPresent private OffsetDateTime testOffsetDateTime;

    public OffsetDateTime getTestOffsetDateTime() {
      return testOffsetDateTime;
    }

    @PastOrPresent private OffsetTime testOffsetTime;

    public OffsetTime getTestOffsetTime() {
      return testOffsetTime;
    }

    @PastOrPresent private Year testYear;

    public Year getTestYear() {
      return testYear;
    }

    @PastOrPresent private YearMonth testYearMonth;

    public YearMonth getTestYearMonth() {
      return testYearMonth;
    }

    @PastOrPresent private ZonedDateTime testZonedDateTime;

    public ZonedDateTime getTestZonedDateTime() {
      return testZonedDateTime;
    }

    @PastOrPresent private HijrahDate testHijrahDate;

    public HijrahDate getTestHijrahDate() {
      return testHijrahDate;
    }

    @PastOrPresent private JapaneseDate testJapaneseDate;

    public JapaneseDate getTestJapaneseDate() {
      return testJapaneseDate;
    }

    @PastOrPresent private MinguoDate testMinguoDate;

    public MinguoDate getTestMinguoDate() {
      return testMinguoDate;
    }

    @PastOrPresent private ThaiBuddhistDate testThaiBuddhistDate;

    public ThaiBuddhistDate getTestThaiBuddhistDate() {
      return testThaiBuddhistDate;
    }
  }
}
