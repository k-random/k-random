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
package io.github.krandom

import io.github.krandom.randomizers.range.IntRangeRandomizer
import java.lang.reflect.Array

/**
 * Random array populator.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class ArrayPopulator
/**
 * Create a new array populator backed by the given [KRandom] instance.
 *
 * @param kRandom the random data generator used to populate array elements; must not be null
 */
(private val kRandom: KRandom) {
  /**
   * Generate a new array of random elements for the specified array `fieldType`.
   *
   * The array size is chosen uniformly at random within the collection size range defined by
   * [KRandomParameters.collectionSizeRange] in the provided `context`. Each element is created by
   * delegating to [KRandom.doPopulateBean] for the array component type.
   *
   * @param fieldType the expected array type to populate (for example, `String[].class`); must
   *   represent an array class
   * @param context the current randomization context providing parameters and state; must not be
   *   null
   * @return a newly created array of the requested component type with a random length and randomly
   *   populated elements
   * @throws NullPointerException if `fieldType` is not an array type (i.e., its component type is
   *   `null`) or if `context` is `null`
   * @throws NegativeArraySizeException if the configured size range yields a negative size
   */
  fun getRandomArray(fieldType: Class<*>, context: RandomizationContext): Any {
    val componentType = fieldType.componentType
    val randomSize = getRandomArraySize(context.parameters)
    val result = Array.newInstance(componentType, randomSize)
    for (i in 0..<randomSize) {
      Array.set(result, i, kRandom.doPopulateBean(componentType, context))
    }
    return result
  }

  private fun getRandomArraySize(parameters: KRandomParameters): Int {
    val collectionSizeRange: KRandomParameters.Range<Int> = parameters.collectionSizeRange
    return IntRangeRandomizer(collectionSizeRange.min, collectionSizeRange.max, kRandom.nextLong())
      .getRandomValue()
  }
}
