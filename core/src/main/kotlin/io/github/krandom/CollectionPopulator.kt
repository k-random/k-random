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
import io.github.krandom.util.ReflectionUtils.createEmptyCollectionForType
import io.github.krandom.util.ReflectionUtils.getEmptyImplementationForCollectionInterface
import io.github.krandom.util.ReflectionUtils.isInterface
import io.github.krandom.util.ReflectionUtils.isParameterizedType
import io.github.krandom.util.ReflectionUtils.isPopulatable
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType

/**
 * Random collection populator.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class CollectionPopulator
/**
 * Create a new collection populator backed by the given [KRandom] instance.
 *
 * The provided `KRandom` is used to determine the collection size (according to [ ]) and to
 * populate elements of the target collection when the field is parameterized with a populatable
 * element type.
 *
 * @param kRandom the random data generator and engine used for population (must not be `null`)
 */
(private val kRandom: KRandom) {
  /**
   * Create and populate a random [Collection] for the given field.
   *
   * The resulting collection type depends on the field declaration:
   * * If the field type is a collection interface, a default concrete implementation is
   *   instantiated.
   * * If the field type is a concrete collection class, an empty instance of that class is created.
   *
   * The size of the collection is chosen randomly within the range defined by
   * [ ][KRandomParameters.collectionSizeRange] in the provided [RandomizationContext]. If the field
   * is parameterized (for example `List<Person>`), and the element type is considered populatable,
   * each element is populated using [KRandom.doPopulateBean]. For raw types (for example `List`) or
   * non-populatable element types, the collection will remain empty.
   *
   * @param field the collection-typed field that should be populated
   * @param context the randomization context providing parameters and state
   * @return a new collection instance with a random size and, when applicable, randomly populated
   *   elements
   */
  @Suppress("UNCHECKED_CAST")
  fun getRandomCollection(field: Field, context: RandomizationContext): MutableCollection<*> {
    val randomSize = getRandomCollectionSize(context.parameters)
    val fieldType = field.type
    val fieldGenericType = field.genericType

    val collection: MutableCollection<Any> =
      (if (isInterface(fieldType)) {
        getEmptyImplementationForCollectionInterface(fieldType)
      } else {
        createEmptyCollectionForType(fieldType, randomSize)
      })
        as MutableCollection<Any>

    if (
      isParameterizedType(fieldGenericType)
    ) { // populate only parametrized types, raw types will be empty
      val parameterizedType = fieldGenericType as ParameterizedType
      val type = parameterizedType.actualTypeArguments[0]
      if (isPopulatable(type)) {
        repeat(randomSize) {
          kRandom.doPopulateBean(type as Class<*>, context)?.let { collection.add(it) }
        }
      }
    }
    return collection
  }

  private fun getRandomCollectionSize(parameters: KRandomParameters): Int {
    val collectionSizeRange: KRandomParameters.Range<Int> = parameters.collectionSizeRange
    return IntRangeRandomizer(collectionSizeRange.min, collectionSizeRange.max, kRandom.nextLong())
      .getRandomValue()
  }
}
