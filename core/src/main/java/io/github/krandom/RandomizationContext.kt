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

import io.github.krandom.api.RandomizerContext
import java.util.IdentityHashMap
import java.util.Random
import java.util.Stack
import kotlin.random.asKotlinRandom

/**
 * Context object for a single call on [KRandom.nextObject]. It contains a map acting as a cache of
 * populated beans to avoid infinite recursion.
 *
 * @author Rémi Alvergnat (toilal.dev@gmail.com)
 */
class RandomizationContext
@JvmOverloads
constructor(
  override val targetType: Class<*>,
  override val parameters: KRandomParameters,
  private val populatedBeans: MutableMap<Class<*>, MutableList<Any>> = IdentityHashMap(),
  private val stack: Stack<RandomizationContextStackItem> = Stack<RandomizationContextStackItem>(),
  private val random: Random = Random(parameters.seed),
) : RandomizerContext {

  override lateinit var rootObject: Any

  fun addPopulatedBean(type: Class<*>, any: Any) {
    val objectPoolSize = parameters.getObjectPoolSize()
    var objects = populatedBeans[type]
    if (objects == null) {
      objects = ArrayList<Any>(objectPoolSize)
    }
    if (objects.size < objectPoolSize) {
      objects.add(any)
    }
    populatedBeans[type] = objects
  }

  fun getPopulatedBean(type: Class<*>): Any = populatedBeans[type]!!.random(random.asKotlinRandom())

  fun hasAlreadyRandomizedType(type: Class<*>): Boolean =
    populatedBeans.containsKey(type) &&
      populatedBeans[type]!!.size == parameters.getObjectPoolSize()

  fun pushStackItem(field: RandomizationContextStackItem) {
    stack.push(field)
  }

  fun popStackItem() {
    stack.pop()
  }

  fun hasExceededRandomizationDepth(): Boolean {
    val currentRandomizationDepth = stack.size
    return currentRandomizationDepth > parameters.getRandomizationDepth()
  }

  fun setRandomizedObject(randomizedObject: Any) {
    if (!::rootObject.isInitialized) {
      this.rootObject = randomizedObject
    }
  }

  override fun getCurrentObject(): Any =
    if (stack.empty()) {
      rootObject
    } else {
      stack.lastElement().any
    }

  override fun getCurrentField(): String = stack.joinToString(".") { it.field.name }

  override fun getCurrentRandomizationDepth(): Int = stack.size
}
