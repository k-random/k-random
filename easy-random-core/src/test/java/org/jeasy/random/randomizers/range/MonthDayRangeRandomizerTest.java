package org.jeasy.random.randomizers.range;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;

import java.time.MonthDay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MonthDayRangeRandomizerTest extends AbstractRangeRandomizerTest<MonthDay> {
  @BeforeEach
  void setUp() {
    min = MonthDay.of(1, 1);
    max = MonthDay.of(3, 31);
    randomizer = new MonthDayRangeRandomizer(min, max);
  }

  @Test
  void generatedValueShouldBeWithinSpecifiedRange() {
    MonthDay randomValue = randomizer.getRandomValue();
    assertThat(randomValue).isBetween(min, max);
  }

  @Test
  void whenSpecifiedMinValueIsAfterMaxValueThenThrowIllegalArgumentException() {
    assertThatThrownBy(() -> new MonthDayRangeRandomizer(max, min))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void whenSpecifiedMinValueIsNullThenShouldUseDefaultMinValue() {
    randomizer = new MonthDayRangeRandomizer(null, max);
    MonthDay randomMonthDay = randomizer.getRandomValue();
    assertThat(randomMonthDay).isLessThanOrEqualTo(max);
  }

  @Test
  void whenSpecifiedMaxvalueIsNullThenShouldUseDefaultMaxValue() {
    randomizer = new MonthDayRangeRandomizer(min, null);
    MonthDay randomMonthDay = randomizer.getRandomValue();
    assertThat(randomMonthDay).isGreaterThanOrEqualTo(min);
  }

  @Test
  void shouldAlwaysGenerateTheSameValueForTheSameSeed() {
    // given
    MonthDayRangeRandomizer monthDayRangeRandomizer = new MonthDayRangeRandomizer(min, max, SEED);

    // when
    MonthDay monthDay = monthDayRangeRandomizer.getRandomValue();

    then(monthDay).isEqualTo(MonthDay.of(3, 6));
  }
}
