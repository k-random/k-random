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

import io.github.krandom.api.ObjectFactory
import io.github.krandom.randomizers.range.IntRangeRandomizer
import io.github.krandom.util.ReflectionUtils.getEmptyImplementationForMapInterface
import io.github.krandom.util.ReflectionUtils.isInterface
import io.github.krandom.util.ReflectionUtils.isParameterizedType
import io.github.krandom.util.ReflectionUtils.isPopulatable
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.EnumMap

/**
 * Populates [Map]-typed fields with random entries.
 *
 * This helper creates an appropriate [Map] instance for the target field (choosing a concrete
 * implementation for interfaces, and handling [EnumMap] specially), then fills it with randomly
 * generated key/value pairs using [KRandom]. Only parameterized map types are populated; raw map
 * types are left empty. If the key type cannot be determined (for example, a raw `EnumMap`), this
 * method may return `null`.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
@Suppress("UNCHECKED_CAST")
class MapPopulator
/**
 * Create a new `MapPopulator`.
 *
 * @param kRandom the engine used to generate random keys and values
 * @param objectFactory factory used to instantiate concrete map implementations when needed
 */
constructor(private val kRandom: KRandom, private val objectFactory: ObjectFactory) {
  /**
   * Generate a random [Map] for the given map-typed [Field].
   *
   * The size of the map is chosen randomly within the configured
   * [KRandomParameters.collectionSizeRange]. Only parameterized map types are populated; raw maps
   * are returned empty. If the target type is an [EnumMap] without a resolvable key type, `null` is
   * returned.
   *
   * @param field a field whose type implements [Map]
   * @param context the current randomization context
   * @return a concrete map instance filled with random entries, empty, or `null` when the key type
   *   cannot be resolved
   */
  fun getRandomMap(field: Field, context: RandomizationContext): MutableMap<*, *>? {
    val randomSize = getRandomMapSize(context.parameters)
    val fieldType = field.type
    val fieldGenericType = field.genericType

    val map = instantiateMap(fieldType, fieldGenericType, context)

    if (map != null && isParameterizedType(fieldGenericType)) {
      populateMap(map, fieldGenericType as ParameterizedType, context, randomSize)
    }
    return map
  }

  private fun instantiateMap(
    fieldType: Class<*>,
    fieldGenericType: Type,
    context: RandomizationContext,
  ): MutableMap<Any, Any>? {
    return if (isInterface(fieldType)) {
      getEmptyImplementationForMapInterface(fieldType) as MutableMap<Any, Any>
    } else {
      try {
        fieldType.getDeclaredConstructor().newInstance() as MutableMap<Any, Any>
      } catch (_: InstantiationException) {
        createEmptyMap(fieldType, fieldGenericType, context)
      } catch (_: IllegalAccessException) {
        createEmptyMap(fieldType, fieldGenericType, context)
      } catch (_: NoSuchMethodException) {
        createEmptyMap(fieldType, fieldGenericType, context)
      } catch (_: InvocationTargetException) {
        createEmptyMap(fieldType, fieldGenericType, context)
      }
    }
  }

  private fun populateMap(
    map: MutableMap<Any, Any>,
    fieldGenericType: ParameterizedType,
    context: RandomizationContext,
    randomSize: Int,
  ) {
    val keyType = fieldGenericType.actualTypeArguments[0]
    val valueType = fieldGenericType.actualTypeArguments[1]
    if (isPopulatable(keyType) && isPopulatable(valueType)) {
      repeat(randomSize) {
        val randomKey: Any? = kRandom.doPopulateBean(keyType as Class<Any>, context)
        val randomValue: Any? = kRandom.doPopulateBean(valueType as Class<Any>, context)
        if (randomKey != null && randomValue != null) {
          map[randomKey] = randomValue
        }
      }
    }
  }

  private fun createEmptyMap(
    fieldType: Class<*>,
    fieldGenericType: Type,
    context: RandomizationContext,
  ): MutableMap<Any, Any>? {
    return if (fieldType.isAssignableFrom(EnumMap::class.java)) {
      if (isParameterizedType(fieldGenericType)) {
        EnumMap::class
          .java
          .getDeclaredConstructor(Class::class.java)
          .newInstance((fieldGenericType as ParameterizedType).actualTypeArguments[0] as Class<*>)
          as MutableMap<Any, Any>
      } else {
        null
      }
    } else {
      objectFactory.createInstance(fieldType, context) as MutableMap<Any, Any>
    }
  }

  private fun getRandomMapSize(parameters: KRandomParameters): Int {
    val collectionSizeRange: KRandomParameters.Range<Int> = parameters.collectionSizeRange
    return IntRangeRandomizer(collectionSizeRange.min, collectionSizeRange.max, parameters.seed)
      .getRandomValue()
  }
}
