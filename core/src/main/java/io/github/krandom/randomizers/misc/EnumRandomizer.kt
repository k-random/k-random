/*
 * The MIT License
 *
 *   Copyright (c) 2023, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package io.github.krandom.randomizers.misc

import io.github.krandom.randomizers.AbstractRandomizer
import kotlin.random.Random
import kotlin.random.asKotlinRandom

/**
 * A [io.github.krandom.api.Randomizer] that generates a random value from a given [Enum].
 *
 * @param <E> the type of elements in the enumeration
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com) </E>
 */
@Suppress("SpreadOperator")
class EnumRandomizer<E : Enum<E>>
/**
 * Create a new [EnumRandomizer].
 *
 * @param enumeration the enumeration from which this randomizer will generate random values
 * @param seed the initial seed
 * @param excludedValues the values to exclude from random picking
 * @throws IllegalArgumentException when excludedValues contains all enumeration values, ie all
 *   elements from the enumeration are excluded
 */
@SafeVarargs
@JvmOverloads
constructor(enumeration: Class<E>, seed: Long = Random.nextLong(), vararg excludedValues: E) :
  AbstractRandomizer<E?>(seed) {

  private val enumConstants: List<E>

  init {
    checkExcludedValues(enumeration, excludedValues)
    this.enumConstants = getFilteredList(enumeration, *excludedValues)
  }

  /**
   * Get a random value within an enumeration or an enumeration subset (when values are excluded)
   *
   * @return a random value within the enumeration
   */
  override fun getRandomValue(): E? = enumConstants.randomOrNull(random.asKotlinRandom())

  private fun checkExcludedValues(enumeration: Class<E>, excludedValues: Array<out E>) {
    enumeration.enumConstants.asList().let { enums ->
      require(enums.isEmpty() || enums.any { it !in excludedValues.toSet() }) {
        "No enum element available for random picking."
      }
    }
  }

  @SafeVarargs
  private fun getFilteredList(enumeration: Class<E>, vararg excludedValues: E) =
    enumeration.enumConstants.asList().filterNot { it in excludedValues.toSet() }
}
