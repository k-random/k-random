package io.github.krandom.randomizers.range;

import java.time.MonthDay;
import java.time.Year;

public class MonthDayRangeRandomizer extends AbstractRangeRandomizer<MonthDay> {
  private final LocalDateRangeRandomizer localDateRangeRandomizer;

  public MonthDayRangeRandomizer(final MonthDay min, final MonthDay max) {
    super(min, max);
    localDateRangeRandomizer =
        new LocalDateRangeRandomizer(
            this.min.atYear(Year.now().getValue()), this.max.atYear(Year.now().getValue()));
  }

  public MonthDayRangeRandomizer(final MonthDay min, final MonthDay max, final long seed) {
    super(min, max, seed);
    localDateRangeRandomizer =
        new LocalDateRangeRandomizer(
            this.min.atYear(Year.now().getValue()), this.max.atYear(Year.now().getValue()), seed);
  }

  @Override
  protected void checkValues() {
    if (min.isAfter(max)) {
      throw new IllegalArgumentException("max must be after min");
    }
  }

  @Override
  protected MonthDay getDefaultMinValue() {
    return MonthDay.of(1, 1);
  }

  @Override
  protected MonthDay getDefaultMaxValue() {
    return MonthDay.of(12, 31);
  }

  @Override
  public MonthDay getRandomValue() {
    return MonthDay.from(localDateRangeRandomizer.getRandomValue());
  }
}
