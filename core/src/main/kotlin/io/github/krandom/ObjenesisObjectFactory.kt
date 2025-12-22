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
import io.github.krandom.api.RandomizerContext
import io.github.krandom.util.ReflectionUtils.getPublicConcreteSubTypesOf
import io.github.krandom.util.ReflectionUtils.isAbstract
import kotlin.random.Random
import org.objenesis.Objenesis
import org.objenesis.ObjenesisStd

/**
 * Objenesis based factory to create "fancy" objects: immutable java beans, generic types, abstract
 * and interface types.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class ObjenesisObjectFactory : ObjectFactory {
  private val objenesis: Objenesis = ObjenesisStd()

  private lateinit var random: Random

  @Suppress("UNCHECKED_CAST", "TooGenericExceptionCaught")
  override fun <T> createInstance(type: Class<T>, context: RandomizerContext): T {
    if (!(::random.isInitialized)) {
      random = Random(context.parameters.seed)
    }
    if (context.parameters.isScanClasspathForConcreteTypes && isAbstract(type)) {
      val publicConcreteSubTypes: List<Class<*>> = getPublicConcreteSubTypesOf(type)
      if (publicConcreteSubTypes.isEmpty()) {
        throw InstantiationError(
          "Unable to find a matching concrete subtype of type: $type in the classpath"
        )
      } else {
        val randomConcreteSubType: Class<*> =
          publicConcreteSubTypes[random.nextInt(publicConcreteSubTypes.size)]
        return createNewInstance(randomConcreteSubType) as T
      }
    } else {
      try {
        return createNewInstance(type)
      } catch (e: Error) {
        throw ObjectCreationException("Unable to create an instance of type: $type", e)
      }
    }
  }

  private fun <T> createNewInstance(type: Class<T>): T {
    try {
      val noArgConstructor = type.getDeclaredConstructor()
      noArgConstructor.trySetAccessible()
      return noArgConstructor.newInstance()
    } catch (_: Exception) {
      return objenesis.newInstance<T>(type)
    }
  }
}
