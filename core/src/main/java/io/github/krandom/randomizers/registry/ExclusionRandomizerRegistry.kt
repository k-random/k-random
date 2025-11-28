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

import io.github.krandom.FieldPredicates
import io.github.krandom.KRandomParameters
import io.github.krandom.TypePredicates
import io.github.krandom.annotation.Exclude
import io.github.krandom.annotation.Priority
import io.github.krandom.api.Randomizer
import io.github.krandom.api.RandomizerRegistry
import io.github.krandom.randomizers.misc.SkipRandomizer
import java.lang.reflect.Field
import java.util.function.Predicate

/**
 * A [RandomizerRegistry] to exclude fields using a [Predicate].
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
@Priority(0)
class ExclusionRandomizerRegistry : RandomizerRegistry {
  private val fieldPredicates: MutableSet<Predicate<Field>> = mutableSetOf()
  private val typePredicates: MutableSet<Predicate<Class<*>>> = mutableSetOf()

  /** {@inheritDoc} */
  override fun init(parameters: KRandomParameters) {
    fieldPredicates.add(FieldPredicates.isAnnotatedWith(Exclude::class.java))
    typePredicates.add(TypePredicates.isAnnotatedWith(Exclude::class.java))
  }

  /** {@inheritDoc} */
  override fun getRandomizer(field: Field): Randomizer<*>? =
    if (fieldPredicates.any { it.test(field) }) SkipRandomizer() else null

  /** {@inheritDoc} */
  override fun getRandomizer(type: Class<*>): Randomizer<*>? =
    if (typePredicates.any { it.test(type) }) SkipRandomizer() else null

  /**
   * Add a field predicate.
   *
   * @param predicate to add
   */
  fun addFieldPredicate(predicate: Predicate<Field>) {
    fieldPredicates.add(predicate)
  }

  /**
   * Add a type predicate.
   *
   * @param predicate to add
   */
  fun addTypePredicate(predicate: Predicate<Class<*>>) {
    typePredicates.add(predicate)
  }
}
