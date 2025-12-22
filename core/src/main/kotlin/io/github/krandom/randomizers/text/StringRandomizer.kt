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
package io.github.krandom.randomizers.text

import io.github.krandom.KRandomParameters
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import kotlin.random.Random

/**
 * Generate a random [String].
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class StringRandomizer
/**
 * Create a new [StringRandomizer].
 *
 * @param charset to use
 * @param maxLength of the String to generate
 * @param minLength of the String to generate
 * @param seed initial seed
 */
@JvmOverloads
constructor(
  charset: Charset = StandardCharsets.US_ASCII,
  private var minLength: Int = KRandomParameters.DEFAULT_STRING_LENGTH_RANGE.min,
  private var maxLength: Int = KRandomParameters.DEFAULT_STRING_LENGTH_RANGE.max,
  seed: Long = Random.nextLong(),
  private val characterRandomizer: CharacterRandomizer = CharacterRandomizer(charset, seed),
) : CharSequenceRandomizer<String>(seed) {

  init {
    require(minLength <= maxLength) { "minLength should be less than or equal to maxLength" }
  }

  override fun getRandomValue() =
    CharArray(nextDouble(minLength.toDouble(), maxLength.toDouble()).toInt()) {
        characterRandomizer.getRandomValue()
      }
      .concatToString()
}
