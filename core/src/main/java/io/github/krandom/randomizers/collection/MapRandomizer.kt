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
package io.github.krandom.randomizers.collection

import io.github.krandom.api.Randomizer
import io.github.krandom.randomizers.number.ByteRandomizer
import kotlin.math.abs

/**
 * A [Randomizer] that generates a [Map] with random entries.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com) </V></K>
 */
class MapRandomizer<K, V>
@JvmOverloads
constructor(
  keyRandomizer: Randomizer<K>,
  valueRandomizer: Randomizer<V>,
  nbEntries: Int = randomSize,
) : Randomizer<MutableMap<K, V>> {
  private val nbElements: Int

  private val keyRandomizer: Randomizer<K>

  private val valueRandomizer: Randomizer<V>

  /**
   * Create a new [MapRandomizer] with a fixed number of entries.
   *
   * @param keyRandomizer the randomizer for keys
   * @param valueRandomizer the randomizer for values
   * @param nbEntries the number of entries to generate
   */
  /**
   * Create a new [MapRandomizer] with a random number of entries.
   *
   * @param keyRandomizer the randomizer for keys
   * @param valueRandomizer the randomizer for values
   */
  init {
    checkArguments(nbEntries)
    this.keyRandomizer = keyRandomizer
    this.valueRandomizer = valueRandomizer
    this.nbElements = nbEntries
  }

  override fun getRandomValue(): MutableMap<K, V> {
    val result: MutableMap<K, V> = HashMap()
    repeat(nbElements) { result[keyRandomizer.getRandomValue()] = valueRandomizer.getRandomValue() }
    return result
  }

  private fun checkArguments(nbEntries: Int) {
    require(nbEntries >= 0) { "The number of entries to generate must be >= 0" }
  }

  companion object {
    private val randomSize: Int
      get() = abs(ByteRandomizer().getRandomValue().toInt()) + 1
  }
}
