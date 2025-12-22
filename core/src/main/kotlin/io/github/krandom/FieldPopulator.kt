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

import io.github.krandom.api.ContextAwareRandomizer
import io.github.krandom.api.Randomizer
import io.github.krandom.api.RandomizerProvider
import io.github.krandom.randomizers.misc.SkipRandomizer
import io.github.krandom.util.ReflectionUtils.filterSameParameterizedTypes
import io.github.krandom.util.ReflectionUtils.getPublicConcreteSubTypesOf
import io.github.krandom.util.ReflectionUtils.isAbstract
import io.github.krandom.util.ReflectionUtils.isArrayType
import io.github.krandom.util.ReflectionUtils.isCollectionType
import io.github.krandom.util.ReflectionUtils.isEnumType
import io.github.krandom.util.ReflectionUtils.isMapType
import io.github.krandom.util.ReflectionUtils.isOptionalType
import io.github.krandom.util.ReflectionUtils.isTypeVariable
import io.github.krandom.util.ReflectionUtils.setFieldValue
import io.github.krandom.util.ReflectionUtils.setProperty
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import kotlin.random.asKotlinRandom

/**
 * Component that encapsulates the logic of generating a random value for a given field. It
 * collaborates with a:
 * * [KRandom] whenever the field is a user defined type.
 * * [ArrayPopulator] whenever the field is an array type.
 * * [CollectionPopulator] whenever the field is a collection type.
 * * [CollectionPopulator]whenever the field is a map type.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
@Suppress("SwallowedException", "UNCHECKED_CAST")
internal class FieldPopulator(
  private val kRandom: KRandom,
  private val randomizerProvider: RandomizerProvider,
  private val arrayPopulator: ArrayPopulator,
  private val collectionPopulator: CollectionPopulator,
  private val mapPopulator: MapPopulator,
  private val optionalPopulator: OptionalPopulator,
) {
  @Throws(IllegalAccessException::class)
  fun populateField(target: Any, field: Field, context: RandomizationContext) {
    val randomizer = getRandomizer(field, context)
    if (randomizer is SkipRandomizer) {
      return
    }
    context.pushStackItem(RandomizationContextStackItem(target, field))
    if (randomizer is ContextAwareRandomizer<*>) {
      randomizer.setRandomizerContext(context)
    }
    if (!context.hasExceededRandomizationDepth()) {
      val value: Any?
      if (randomizer != null) {
        value = randomizer.getRandomValue()
      } else {
        try {
          value = generateRandomValue(field, context)
        } catch (e: ObjectCreationException) {
          val exceptionMessage =
            "Unable to create type: ${field.type.getName()} " +
              "for field: ${field.name} " +
              "of class: ${target.javaClass.getName()}"
          // FIXME catch ObjectCreationException and throw ObjectCreationException ?
          throw ObjectCreationException(exceptionMessage, e)
        }
      }
      if (context.parameters.isBypassSetters) {
        setFieldValue(target, field, value)
      } else {
        try {
          setProperty(target, field, value)
        } catch (e: InvocationTargetException) {
          val exceptionMessage =
            "Unable to invoke setter for field ${field.name} of class ${target.javaClass.getName()}"
          throw ObjectCreationException(exceptionMessage, e.cause!!)
        }
      }
    }
    context.popStackItem()
  }

  private fun getRandomizer(field: Field, context: RandomizationContext): Randomizer<*>? {
    // issue 241: if there is no custom randomizer by field, then check by type
    var randomizer = randomizerProvider.getRandomizerByField(field, context)
    if (randomizer == null) {
      val genericType = field.genericType
      if (isTypeVariable(genericType)) {
        // if generic type, retrieve actual type from declaring class
        val type = getParametrizedType(field, context)
        randomizer = randomizerProvider.getRandomizerByType(type, context)
      } else {
        randomizer = randomizerProvider.getRandomizerByType(field.type, context)
      }
    }
    return randomizer
  }

  private fun generateRandomValue(field: Field, context: RandomizationContext): Any? {
    val fieldType = field.type
    val fieldGenericType = field.genericType

    return if (isArrayType(fieldType)) {
      arrayPopulator.getRandomArray(fieldType, context)
    } else if (isCollectionType(fieldType)) {
      collectionPopulator.getRandomCollection(field, context)
    } else if (isMapType(fieldType)) {
      mapPopulator.getRandomMap(field, context)
    } else if (isOptionalType(fieldType)) {
      optionalPopulator.getRandomOptional(field, context)
    } else {
      if (
        context.parameters.isScanClasspathForConcreteTypes &&
          isAbstract(fieldType) &&
          !isEnumType(fieldType) /*enums can be abstract, but cannot inherit*/
      ) {
        val parameterizedTypes: List<Class<*>> =
          filterSameParameterizedTypes(getPublicConcreteSubTypesOf(fieldType), fieldGenericType)
        if (parameterizedTypes.isEmpty()) {
          throw ObjectCreationException(
            "Unable to find a matching concrete subtype of type: $fieldType"
          )
        } else {
          val randomConcreteSubType = parameterizedTypes.random(kRandom.asKotlinRandom())
          kRandom.doPopulateBean(randomConcreteSubType as Class<Any>, context)
        }
      } else {
        val genericType = field.genericType
        if (isTypeVariable(genericType)) {
          // if generic type, try to retrieve actual type from hierarchy
          val type = getParametrizedType(field, context)
          kRandom.doPopulateBean(type as Class<Any>, context)
        } else {
          kRandom.doPopulateBean(fieldType as Class<Any>, context)
        }
      }
    }
  }

  private fun getParametrizedType(field: Field, context: RandomizationContext): Class<*> {
    val declaringClass = field.declaringClass
    val typeParameters: Array<out TypeVariable<out Class<*>?>?>? =
      declaringClass.getTypeParameters()
    val genericSuperclass = getGenericSuperClass(context)
    val parameterizedGenericSuperType = genericSuperclass as ParameterizedType
    val actualTypeArguments = parameterizedGenericSuperType.actualTypeArguments
    var actualTypeArgument: Type? = null
    for (i in typeParameters?.indices!!) {
      if (field.genericType == typeParameters[i]) {
        actualTypeArgument = actualTypeArguments[i]
      }
    }
    if (actualTypeArgument == null) {
      return field.javaClass
    }
    val aClass: Class<*>
    var typeName: String? = null
    try {
      typeName = actualTypeArgument.typeName
      aClass = Class.forName(typeName)
    } catch (e: ClassNotFoundException) {
      val message =
        String.format(
          "Unable to load class %s of generic field %s in class %s. Please refer to the" +
            " documentation as this generic type may not be supported for randomization.",
          typeName,
          field.name,
          field.declaringClass.getName(),
        )
      throw ObjectCreationException(message, e)
    }
    return aClass
  }

  // find the generic base class in the hierarchy (which might not be the first super type)
  private fun getGenericSuperClass(context: RandomizationContext): Type {
    var targetType: Class<*>? = context.targetType
    var genericSuperclass = targetType!!.getGenericSuperclass()
    while (targetType != null && genericSuperclass !is ParameterizedType) {
      targetType = targetType.getSuperclass()
      if (targetType != null) {
        genericSuperclass = targetType.getGenericSuperclass()
      }
    }
    return genericSuperclass
  }
}
