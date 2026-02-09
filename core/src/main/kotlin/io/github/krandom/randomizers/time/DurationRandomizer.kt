package io.github.krandom.randomizers.time

import io.github.krandom.api.Randomizer
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

/** A [Randomizer] that generates random [Duration]. */
class DurationRandomizer

/**
 * Create a new [DurationRandomizer].
 *
 * @param seed initial seed
 * @param delegate the delegate randomizer for the duration amount
 * @see JavaDurationRandomizer
 */
@JvmOverloads
constructor(
  seed: Long = Random.nextLong(),
  private val delegate: JavaDurationRandomizer = JavaDurationRandomizer(seed),
) : Randomizer<Duration> {
  override fun getRandomValue(): Duration = delegate.getRandomValue().toKotlinDuration()
}
