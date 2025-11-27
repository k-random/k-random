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
 * A [Randomizer] that generates a set of random values using a delegate [Randomizer].
 *
 * @param <T> the type of elements to generate
 * @author Eric Taix (eric.taix@gmail.com) </T>
 */
class SetRandomizer<T>
/**
 * Create a new [SetRandomizer] that will generate a [Set] with a fixed number of elements.
 *
 * @param delegate The [Randomizer] used to generate each element
 * @param nbElements The number of elements to generate
 */
@JvmOverloads
constructor(
  delegate: Randomizer<T>,
  nbElements: Int = abs(ByteRandomizer().getRandomValue().toInt()),
) : CollectionRandomizer<T>(delegate, nbElements) {

  override fun getRandomValue() = List(nbElements) { randomElement }.toMutableSet()

  override fun toString() = "SetRandomizer [delegate=$delegate, nbElements=$nbElements]"
}
