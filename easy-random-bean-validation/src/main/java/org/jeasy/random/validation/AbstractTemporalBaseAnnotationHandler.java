package org.jeasy.random.validation;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.api.Randomizer;
import org.jeasy.random.randomizers.range.*;

import java.lang.reflect.Field;
import java.time.*;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.Calendar;
import java.util.Date;

public abstract class AbstractTemporalBaseAnnotationHandler
    implements BeanValidationAnnotationHandler {
  protected final Date minDate;
  protected final Date maxDate;
  protected final Instant minInstant;
  protected final Instant maxInstant;
  protected final LocalDate minLocalDate;
  protected final LocalDate maxLocalDate;
  protected final LocalDateTime minLocalDateTime;
  protected final LocalDateTime maxLocalDateTime;
  protected final LocalTime minLocalTime;
  protected final LocalTime maxLocalTime;
  protected final MonthDay minMonthDay;
  protected final MonthDay maxMonthDay;
  protected final OffsetDateTime minOffsetDateTime;
  protected final OffsetDateTime maxOffsetDateTime;
  protected final OffsetTime minOffsetTime;
  protected final OffsetTime maxOffsetTime;
  protected final Year minYear;
  protected final Year maxYear;
  protected final YearMonth minYearMonth;
  protected final YearMonth maxYearMonth;
  protected final ZonedDateTime minZonedDateTime;
  protected final ZonedDateTime maxZonedDateTime;

  protected AbstractTemporalBaseAnnotationHandler(
      Date minDate,
      Date maxDate,
      Instant minInstant,
      Instant maxInstant,
      LocalDate minLocalDate,
      LocalDate maxLocalDate,
      LocalDateTime minLocalDateTime,
      LocalDateTime maxLocalDateTime,
      LocalTime minLocalTime,
      LocalTime maxLocalTime,
      MonthDay minMonthDay,
      MonthDay maxMonthDay,
      OffsetDateTime minOffsetDateTime,
      OffsetDateTime maxOffsetDateTime,
      OffsetTime minOffsetTime,
      OffsetTime maxOffsetTime,
      Year minYear,
      Year maxYear,
      YearMonth minYearMonth,
      YearMonth maxYearMonth,
      ZonedDateTime minZonedDateTime,
      ZonedDateTime maxZonedDateTime) {
    this.minDate = minDate;
    this.maxDate = maxDate;
    this.minInstant = minInstant;
    this.maxInstant = maxInstant;
    this.minLocalDate = minLocalDate;
    this.maxLocalDate = maxLocalDate;
    this.minLocalDateTime = minLocalDateTime;
    this.maxLocalDateTime = maxLocalDateTime;
    this.minLocalTime = minLocalTime;
    this.maxLocalTime = maxLocalTime;
    this.minMonthDay = minMonthDay;
    this.maxMonthDay = maxMonthDay;
    this.minOffsetDateTime = minOffsetDateTime;
    this.maxOffsetDateTime = maxOffsetDateTime;
    this.minOffsetTime = minOffsetTime;
    this.maxOffsetTime = maxOffsetTime;
    this.minYear = minYear;
    this.maxYear = maxYear;
    this.minYearMonth = minYearMonth;
    this.maxYearMonth = maxYearMonth;
    this.minZonedDateTime = minZonedDateTime;
    this.maxZonedDateTime = maxZonedDateTime;
  }

  @Override
  public Randomizer<?> getRandomizer(Field field) {
    if (field.getType().equals(Date.class)) {
      return () -> new DateRangeRandomizer(minDate, maxDate).getRandomValue();
    } else if (field.getType().equals(Calendar.class)) {
      return () -> {
        Calendar calendar = Calendar.getInstance();
        Date date = new DateRangeRandomizer(minDate, maxDate).getRandomValue();
        calendar.setTime(date);
        return calendar;
      };
    } else if (field.getType().equals(Instant.class)) {
      return () -> new InstantRangeRandomizer(minInstant, maxInstant).getRandomValue();
    } else if (field.getType().equals(LocalDate.class)) {
      return () -> new LocalDateRangeRandomizer(minLocalDate, maxLocalDate).getRandomValue();
    } else if (field.getType().equals(LocalDateTime.class)) {
      return () ->
          new LocalDateTimeRangeRandomizer(minLocalDateTime, maxLocalDateTime).getRandomValue();
    } else if (field.getType().equals(LocalTime.class)) {
      return () -> new LocalTimeRangeRandomizer(minLocalTime, maxLocalTime).getRandomValue();
    } else if (field.getType().equals(MonthDay.class)) {
      return () -> new MonthDayRangeRandomizer(minMonthDay, maxMonthDay).getRandomValue();
    } else if (field.getType().equals(OffsetDateTime.class)) {
      return () ->
          new OffsetDateTimeRangeRandomizer(minOffsetDateTime, maxOffsetDateTime).getRandomValue();
    } else if (field.getType().equals(OffsetTime.class)) {
      return () -> new OffsetTimeRangeRandomizer(minOffsetTime, maxOffsetTime).getRandomValue();
    } else if (field.getType().equals(Year.class)) {
      return () -> new YearRangeRandomizer(minYear, maxYear).getRandomValue();
    } else if (field.getType().equals(YearMonth.class)) {
      return () -> new YearMonthRangeRandomizer(minYearMonth, maxYearMonth).getRandomValue();
    } else if (field.getType().equals(ZonedDateTime.class)) {
      return () ->
          new ZonedDateTimeRangeRandomizer(minZonedDateTime, maxZonedDateTime).getRandomValue();
    } else if (field.getType().equals(HijrahDate.class)) {
      return () ->
          HijrahDate.from(
              new LocalDateRangeRandomizer(minLocalDate, maxLocalDate).getRandomValue());
    } else if (field.getType().equals(JapaneseDate.class)) {
      return () ->
          JapaneseDate.from(
              new LocalDateRangeRandomizer(minLocalDate, maxLocalDate).getRandomValue());
    } else if (field.getType().equals(MinguoDate.class)) {
      return () ->
          MinguoDate.from(
              new LocalDateRangeRandomizer(minLocalDate, maxLocalDate).getRandomValue());
    } else if (field.getType().equals(ThaiBuddhistDate.class)) {
      return () ->
          ThaiBuddhistDate.from(
              new LocalDateRangeRandomizer(minLocalDate, maxLocalDate).getRandomValue());
    } else {
      return () -> new EasyRandom().nextObject(field.getType());
    }
  }
}
