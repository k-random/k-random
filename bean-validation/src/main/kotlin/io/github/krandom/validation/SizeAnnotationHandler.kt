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
package io.github.krandom.validation

import io.github.krandom.KRandom
import io.github.krandom.KRandomParameters
import io.github.krandom.api.Randomizer
import io.github.krandom.randomizers.text.StringRandomizer
import io.github.krandom.util.ReflectionUtils
import io.github.krandom.util.ReflectionUtils.createEmptyCollectionForType
import io.github.krandom.util.ReflectionUtils.getEmptyImplementationForCollectionInterface
import io.github.krandom.util.ReflectionUtils.getEmptyImplementationForMapInterface
import io.github.krandom.util.ReflectionUtils.isArrayType
import io.github.krandom.util.ReflectionUtils.isCollectionType
import io.github.krandom.util.ReflectionUtils.isInterface
import io.github.krandom.util.ReflectionUtils.isMapType
import io.github.krandom.util.ReflectionUtils.isParameterizedType
import io.github.krandom.util.ReflectionUtils.isPopulatable
import jakarta.validation.constraints.Size
import java.lang.reflect.Array
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.util.Random

@Suppress("UNCHECKED_CAST")
internal class SizeAnnotationHandler(parameters: KRandomParameters) :
  BeanValidationAnnotationHandler {
  // Keep a defensive copy of parameters but avoid creating KRandom here to prevent
  // recursive ServiceLoader initialization during registry setup
  private val parameters: KRandomParameters by lazy { parameters.copy() }
  private val random: Random by lazy { Random(parameters.seed) }

  override fun getRandomizer(field: Field): Randomizer<*>? {
    val fieldType = field.type
    val sizeAnnotation =
      ReflectionUtils.getAnnotation<Size?>(field, Size::class.java as Class<Size?>) ?: return null

    val min = sizeAnnotation.min
    val max =
      if (sizeAnnotation.max == Int.MAX_VALUE) UByte.MAX_VALUE.toInt() else sizeAnnotation.max

    return when {
      fieldType == String::class.java ->
        // Derive a per-field deterministic seed to avoid interfering with other string randomizers
        // using the same global seed (see NotBlankAnnotationHandler for the same pattern).
        StringRandomizer(parameters.charset, min, max, random.nextLong())
      isArrayType(fieldType) -> getArrayRandomizer(fieldType, min, max)
      isCollectionType(fieldType) -> getCollectionRandomizer(min, max, fieldType, field)
      isMapType(fieldType) -> getMapRandomizer(min, max, field, fieldType)
      else -> null
    }
  }

  private fun getMapRandomizer(
    min: Int,
    max: Int,
    field: Field,
    fieldType: Class<*>,
  ): Randomizer<MutableMap<Any?, Any?>> = Randomizer {
    val size = boundedSize(min, max)
    val fieldGenericType = field.genericType

    val map: MutableMap<Any?, Any?> =
      if (isInterface(fieldType)) {
        getEmptyImplementationForMapInterface(fieldType) as MutableMap<Any?, Any?>
      } else {
        try {
          fieldType.getDeclaredConstructor().newInstance() as MutableMap<Any?, Any?>
        } catch (_: ReflectiveOperationException) {
          // Fallback to a default mutable map implementation
          HashMap()
        }
      }

    if (isParameterizedType(fieldGenericType)) {
      val pType = fieldGenericType as ParameterizedType
      val keyType = pType.actualTypeArguments[0]
      val valueType = pType.actualTypeArguments[1]
      if (isPopulatable(keyType) && isPopulatable(valueType)) {
        val kr = KRandom(parameters)
        var inserted = 0
        var attempts = 0
        val maxAttempts = size * ATTEMPT_MULTIPLIER + ATTEMPT_OFFSET
        while (inserted < size && attempts < maxAttempts) {
          val key = kr.nextObject(keyType as Class<*>)
          val value = kr.nextObject(valueType as Class<*>)
          if (key != null && !map.containsKey(key)) {
            map[key] = value
            inserted++
          }
          attempts++
        }
      }
    }
    map
  }

  private fun getCollectionRandomizer(
    min: Int,
    max: Int,
    fieldType: Class<*>,
    field: Field,
  ): Randomizer<MutableCollection<out Any?>?> = Randomizer {
    val size = boundedSize(min, max)
    val collection =
      if (isInterface(fieldType)) getEmptyImplementationForCollectionInterface(fieldType)
      else createEmptyCollectionForType(fieldType, size)

    // populate only parameterized types, raw types will be empty
    val genericType = field.genericType
    if (collection != null && isParameterizedType(genericType)) {
      val pType = genericType as ParameterizedType
      val elementType = pType.actualTypeArguments[0]
      if (isPopulatable(elementType)) {
        val kr = KRandom(parameters)
        var i = 0
        while (i < size) {
          val item = kr.nextObject(elementType as Class<*>)
          (collection as MutableCollection<Any?>).add(item)
          i++
        }
      }
    }
    collection
  }

  private fun getArrayRandomizer(fieldType: Class<*>, min: Int, max: Int): Randomizer<in Any> =
    Randomizer {
      val componentType = fieldType.componentType
      val size = boundedSize(min, max)
      val array = Array.newInstance(componentType, size)
      val kRandom = KRandom(parameters)
      for (i in 0 until size) {
        val value = kRandom.nextObject(componentType)
        Array.set(array, i, value)
      }
      array
    }

  private fun boundedSize(min: Int, max: Int): Int {
    if (min >= max) return min
    val bound = max - min + 1
    val delta = random.nextInt(bound)
    return min + delta
  }
}

private const val ATTEMPT_MULTIPLIER = 3
private const val ATTEMPT_OFFSET = 10
