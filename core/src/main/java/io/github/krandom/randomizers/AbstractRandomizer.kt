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
package io.github.krandom.randomizers

import io.github.krandom.api.Randomizer
import java.util.*

/**
 * Base class for [Randomizer] implementations.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
abstract class AbstractRandomizer<T> : Randomizer<T> {
  @JvmField protected val random: Random

  protected constructor() {
    random = Random()
  }

  protected constructor(seed: Long) {
    random = Random(seed)
  }

  protected fun getPredefinedValuesOf(key: String): Array<String> {
    return ResourceBundle.getBundle("krandom-data")
      .getString(key)
      .split(",".toRegex())
      .dropLastWhile { it.isEmpty() }
      .toTypedArray()
  }

  override fun toString(): String = javaClass.simpleName

  /**
   * Return a random double in the given range.
   *
   * @param min value (inclusive)
   * @param max value (exclusive)
   * @return random double in the given range
   */
  protected fun nextDouble(min: Double, max: Double): Double =
    (min + (random.nextDouble() * (max - min))).coerceIn(min, max)
  // NB: ThreadLocalRandom.current().nextDouble(min, max)) cannot be used
  // because the seed is not configurable and is created per thread (see Javadoc)
}
