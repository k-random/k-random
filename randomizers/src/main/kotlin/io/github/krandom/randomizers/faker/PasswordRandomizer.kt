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
package io.github.krandom.randomizers.faker

import java.util.*

/**
 * A [io.github.krandom.annotation.Randomizer] that generates random passwords.
 *
 * @author [JJ1216](https://github.com/JJ1216)
 */
@Suppress("unused", "KDocUnresolvedReference")
class PasswordRandomizer
/**
 * Create a new [PasswordRandomizer].
 *
 * @param seed the initial seed
 * @param min the minimum number of characters in generated passwords
 * @param max the maximum number of characters in generated passwords
 * @param includeUppercase true to generate passwords containing Uppercase Characters , false
 *   otherwise
 * @param includeSpecial true to generate passwords containing Special Characters, false otherwise
 */
@JvmOverloads
constructor(
  seed: Long = Random().nextLong(),
  private val min: Int = 1,
  private val max: Int = 20,
  private val includeUppercase: Boolean = false,
  private val includeSpecial: Boolean = false,
) : FakerBasedRandomizer<String>(seed) {

  override fun getRandomValue(): String {
    return faker.credentials().password(min, max, includeUppercase, includeSpecial)
  }
}
