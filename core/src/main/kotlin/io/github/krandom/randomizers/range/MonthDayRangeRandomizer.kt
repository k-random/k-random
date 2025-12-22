package io.github.krandom.randomizers.range

import java.time.MonthDay
import java.time.Year
import kotlin.random.Random

private const val JANUARY = 1
private const val DECEMBER = 12
private const val FIRST = 1
private const val THIRTY_FIRST = 31

/** Generate a random [MonthDay] within the given range. */
class MonthDayRangeRandomizer
/**
 * Create a new [MonthDayRangeRandomizer].
 *
 * @param min min value (inclusive)
 * @param max max value (exclusive)
 * @param seed initial seed
 */
@JvmOverloads
constructor(min: MonthDay?, max: MonthDay?, seed: Long = Random.nextLong()) :
  AbstractRangeRandomizer<MonthDay>(min, max, seed) {
  override val defaultMinValue: MonthDay
    get() = MonthDay.of(JANUARY, FIRST)

  override val defaultMaxValue: MonthDay
    get() = MonthDay.of(DECEMBER, THIRTY_FIRST)

  private val localDateRangeRandomizer: LocalDateRangeRandomizer =
    LocalDateRangeRandomizer(
      this.min.atYear(Year.now().value),
      this.max.atYear(Year.now().value),
      seed,
    )

  override fun getRandomValue(): MonthDay = MonthDay.from(localDateRangeRandomizer.getRandomValue())
}
