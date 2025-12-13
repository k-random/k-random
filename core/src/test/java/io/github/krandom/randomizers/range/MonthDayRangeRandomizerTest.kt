package io.github.krandom.randomizers.range

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import io.kotest.matchers.ranges.shouldBeIn
import io.kotest.matchers.shouldBe
import java.time.MonthDay
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MonthDayRangeRandomizerTest : AbstractRangeRandomizerTest<MonthDay>() {
  override val min: MonthDay = MonthDay.of(1, 1)
  override val max: MonthDay = MonthDay.of(3, 31)

  @BeforeEach
  fun setUp() {
    randomizer = MonthDayRangeRandomizer(min, max)
  }

  @Test
  fun `generated value should be within specified range`() {
    val randomValue: MonthDay = randomizer.getRandomValue()

    randomValue shouldBeIn min..max
  }

  @Test
  fun `when specified min value is after max value then throw illegal argument exception`() {
    shouldThrow<IllegalArgumentException> { MonthDayRangeRandomizer(max, min) }
  }

  @Test
  fun `when specified min value is null then should use default min value`() {
    randomizer = MonthDayRangeRandomizer(null, max)

    val randomMonthDay = randomizer.getRandomValue()

    randomMonthDay shouldBeLessThanOrEqualTo max
  }

  @Test
  fun `when specified maxvalue is null then should use default max value`() {
    randomizer = MonthDayRangeRandomizer(min, null)

    val randomMonthDay = randomizer.getRandomValue()

    randomMonthDay shouldBeGreaterThanOrEqualTo min
  }

  @Test
  fun shouldAlwaysGenerateTheSameValueForTheSameSeed() {
    val monthDayRangeRandomizer = MonthDayRangeRandomizer(min, max, SEED)

    val monthDay = monthDayRangeRandomizer.getRandomValue()

    monthDay shouldBe MonthDay.of(3, 6)
  }
}
