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

import io.github.krandom.randomizers.AbstractRandomizer
import java.util.Locale
import kotlin.random.Random
import net.datafaker.Faker

/**
 * Abstract [io.github.krandom.api.Randomizer] based on
 * [Data Faker](https://github.com/datafaker-net/datafaker).
 *
 * @param T the element type
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
abstract class FakerBasedRandomizer<T>
@JvmOverloads
protected constructor(seed: Long = Random.nextLong(), locale: Locale = Locale.ENGLISH) :
  AbstractRandomizer<T>(seed) {
  @JvmField protected val faker: Faker = Faker(locale, random)
}
