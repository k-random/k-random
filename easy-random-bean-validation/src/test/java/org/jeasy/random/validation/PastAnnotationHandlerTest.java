package org.jeasy.random.validation;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Past;
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

class PastAnnotationHandlerTest {

  private EasyRandom easyRandom;

  @BeforeEach
  void setUp() {
    easyRandom = new EasyRandom();
  }

  @Test
  void generatedBeanShouldBeValidAccordingToValidationConstraints() {
    // given
    // when
    TestBean testBean = easyRandom.nextObject(TestBean.class);

    // then
    assertThat(testBean.getTestDate()).isBefore(Date.from(Instant.now()));
    assertThat(testBean.getTestCalendar()).isLessThan(Calendar.getInstance());
    assertThat(testBean.getTestInstant()).isBefore(Instant.now());
    assertThat(testBean.getTestLocalDate()).isBefore(LocalDate.now());
    assertThat(testBean.getTestLocalDateTime()).isBefore(LocalDateTime.now());
    assertThat(testBean.getTestLocalTime()).isBefore(LocalTime.now());
    assertThat(testBean.getTestMonthDay()).isLessThan(MonthDay.now());
    assertThat(testBean.getTestOffsetDateTime()).isBefore(OffsetDateTime.now());
    assertThat(testBean.getTestOffsetTime()).isBefore(OffsetTime.now());
    assertThat(testBean.getTestYear()).isLessThan(Year.now());
    assertThat(testBean.getTestYearMonth()).isLessThan(YearMonth.now());
    assertThat(testBean.getTestZonedDateTime()).isBefore(ZonedDateTime.now());
    assertThat(testBean.getTestHijrahDate()).isLessThan(HijrahDate.now());
    assertThat(testBean.getTestJapaneseDate()).isLessThan(JapaneseDate.now());
    assertThat(testBean.getTestMinguoDate()).isLessThan(MinguoDate.now());
    assertThat(testBean.getTestThaiBuddhistDate()).isLessThan(ThaiBuddhistDate.now());
  }

  @Test
  void generatedBeanShouldBeValidUsingBeanValidationApi() {
    // given
    // when
    TestBean testBean = easyRandom.nextObject(TestBean.class);

    Validator validator;
    try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      validator = validatorFactory.getValidator();
    }
    Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);

    // then
    assertThat(violations).isEmpty();
  }

  static class TestBean {
    @Past private Date testDate;

    public Date getTestDate() {
      return testDate;
    }

    @Past private Calendar testCalendar;

    public Calendar getTestCalendar() {
      return testCalendar;
    }

    @Past private Instant testInstant;

    public Instant getTestInstant() {
      return testInstant;
    }

    @Past private LocalDate testLocalDate;

    public LocalDate getTestLocalDate() {
      return testLocalDate;
    }

    @Past private LocalDateTime testLocalDateTime;

    public LocalDateTime getTestLocalDateTime() {
      return testLocalDateTime;
    }

    @Past private LocalTime testLocalTime;

    public LocalTime getTestLocalTime() {
      return testLocalTime;
    }

    @Past private MonthDay testMonthDay;

    public MonthDay getTestMonthDay() {
      return testMonthDay;
    }

    @Past private OffsetDateTime testOffsetDateTime;

    public OffsetDateTime getTestOffsetDateTime() {
      return testOffsetDateTime;
    }

    @Past private OffsetTime testOffsetTime;

    public OffsetTime getTestOffsetTime() {
      return testOffsetTime;
    }

    @Past private Year testYear;

    public Year getTestYear() {
      return testYear;
    }

    @Past private YearMonth testYearMonth;

    public YearMonth getTestYearMonth() {
      return testYearMonth;
    }

    @Past private ZonedDateTime testZonedDateTime;

    public ZonedDateTime getTestZonedDateTime() {
      return testZonedDateTime;
    }

    @Past private HijrahDate testHijrahDate;

    public HijrahDate getTestHijrahDate() {
      return testHijrahDate;
    }

    @Past private JapaneseDate testJapaneseDate;

    public JapaneseDate getTestJapaneseDate() {
      return testJapaneseDate;
    }

    @Past private MinguoDate testMinguoDate;

    public MinguoDate getTestMinguoDate() {
      return testMinguoDate;
    }

    @Past private ThaiBuddhistDate testThaiBuddhistDate;

    public ThaiBuddhistDate getTestThaiBuddhistDate() {
      return testThaiBuddhistDate;
    }
  }
}
