package io.github.krandom.validation;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.krandom.KRandom;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Future;
import java.time.*;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FutureAnnotationHandlerTest {

  private KRandom kRandom;

  @BeforeEach
  void setUp() {
    kRandom = new KRandom();
  }

  @Test
  void generatedBeanShouldBeValidAccordingToValidationConstraints() {
    TestBean testBean = kRandom.nextObject(TestBean.class);

    assertThat(testBean.getTestDate()).isAfter(Date.from(Instant.now()));
    assertThat(testBean.getTestCalendar()).isGreaterThan(Calendar.getInstance());
    assertThat(testBean.getTestInstant()).isAfter(Instant.now());
    assertThat(testBean.getTestLocalDate()).isAfter(LocalDate.now());
    assertThat(testBean.getTestLocalDateTime()).isAfter(LocalDateTime.now());
    assertThat(testBean.getTestLocalTime()).isAfter(LocalTime.now(ZoneId.systemDefault()));
    assertThat(testBean.getTestMonthDay()).isGreaterThan(MonthDay.now());
    assertThat(testBean.getTestOffsetDateTime()).isAfter(OffsetDateTime.now());
    assertThat(testBean.getTestOffsetTime()).isAfter(OffsetTime.now());
    assertThat(testBean.getTestYear()).isGreaterThan(Year.now());
    assertThat(testBean.getTestYearMonth()).isAfter(YearMonth.now());
    assertThat(testBean.getTestZonedDateTime()).isAfter(ZonedDateTime.now());
    assertThat(testBean.getTestHijrahDate()).isGreaterThan(HijrahDate.now());
    assertThat(testBean.getTestJapaneseDate()).isGreaterThan(JapaneseDate.now());
    assertThat(testBean.getTestMinguoDate()).isGreaterThan(MinguoDate.now());
    assertThat(testBean.getTestThaiBuddhistDate()).isGreaterThan(ThaiBuddhistDate.now());
  }

  @Test
  void generatedBeanShouldBeValidUsingBeanValidationApi() {
    TestBean testBean = kRandom.nextObject(TestBean.class);

    Validator validator;
    try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      validator = validatorFactory.getValidator();
    }
    Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);

    assertThat(violations).isEmpty();
  }

  static class TestBean {
    @Future private Date testDate;

    public Date getTestDate() {
      return testDate;
    }

    @Future private Calendar testCalendar;

    public Calendar getTestCalendar() {
      return testCalendar;
    }

    @Future private Instant testInstant;

    public Instant getTestInstant() {
      return testInstant;
    }

    @Future private LocalDate testLocalDate;

    public LocalDate getTestLocalDate() {
      return testLocalDate;
    }

    @Future private LocalDateTime testLocalDateTime;

    public LocalDateTime getTestLocalDateTime() {
      return testLocalDateTime;
    }

    @Future private LocalTime testLocalTime;

    public LocalTime getTestLocalTime() {
      return testLocalTime;
    }

    @Future private MonthDay testMonthDay;

    public MonthDay getTestMonthDay() {
      return testMonthDay;
    }

    @Future private OffsetDateTime testOffsetDateTime;

    public OffsetDateTime getTestOffsetDateTime() {
      return testOffsetDateTime;
    }

    @Future private OffsetTime testOffsetTime;

    public OffsetTime getTestOffsetTime() {
      return testOffsetTime;
    }

    @Future private Year testYear;

    public Year getTestYear() {
      return testYear;
    }

    @Future private YearMonth testYearMonth;

    public YearMonth getTestYearMonth() {
      return testYearMonth;
    }

    @Future private ZonedDateTime testZonedDateTime;

    public ZonedDateTime getTestZonedDateTime() {
      return testZonedDateTime;
    }

    @Future private HijrahDate testHijrahDate;

    public HijrahDate getTestHijrahDate() {
      return testHijrahDate;
    }

    @Future private JapaneseDate testJapaneseDate;

    public JapaneseDate getTestJapaneseDate() {
      return testJapaneseDate;
    }

    @Future private MinguoDate testMinguoDate;

    public MinguoDate getTestMinguoDate() {
      return testMinguoDate;
    }

    @Future private ThaiBuddhistDate testThaiBuddhistDate;

    public ThaiBuddhistDate getTestThaiBuddhistDate() {
      return testThaiBuddhistDate;
    }
  }
}
