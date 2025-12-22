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
package io.github.krandom.randomizers.registry

import io.github.krandom.KRandomParameters
import io.github.krandom.annotation.Priority
import io.github.krandom.api.Randomizer
import io.github.krandom.api.RandomizerRegistry
import io.github.krandom.util.ReflectionUtils.getWrapperType
import java.lang.reflect.Field
import java.util.function.Predicate

/**
 * Registry of user defined randomizers.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
@Priority(-1)
class CustomRandomizerRegistry : RandomizerRegistry {
  private val customFieldRandomizersRegistry: MutableMap<Predicate<Field>, Randomizer<*>> =
    mutableMapOf()
  private val customTypeRandomizersRegistry: MutableMap<Class<*>, Randomizer<*>> = mutableMapOf()

  override fun init(parameters: KRandomParameters) {
    // no op
  }

  override fun getRandomizer(field: Field): Randomizer<*>? {
    return customFieldRandomizersRegistry.entries
      .firstOrNull { (predicate, _) -> predicate.test(field) }
      ?.value ?: getRandomizer(field.type)
  }

  override fun getRandomizer(type: Class<*>): Randomizer<*>? {
    // issue 241: primitive type were ignored: try to get randomizer by primitive type, if not, then
    // try by wrapper type
    var randomizer = customTypeRandomizersRegistry[type]
    if (randomizer == null) {
      val wrapperType = if (type.isPrimitive) getWrapperType(type) else type
      randomizer = customTypeRandomizersRegistry[wrapperType]
    }
    return randomizer
  }

  fun <T, R> registerRandomizer(type: Class<T>, randomizer: Randomizer<R>) {
    customTypeRandomizersRegistry[type] = randomizer
  }

  fun registerRandomizer(predicate: Predicate<Field>, randomizer: Randomizer<*>) {
    customFieldRandomizersRegistry[predicate] = randomizer
  }
}
