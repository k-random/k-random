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

import java.lang.reflect.Field
import java.util.function.Predicate

/**
 * Common predicates to identify fields. Usually used in combination to define a field in an object
 * graph. For example:
 * <pre>
 * Predicate&lt;Field&gt; predicate = named("name").and(ofType(String.class)).and(inClass(Person.class));
 * </pre> *
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
object FieldPredicates {
  /**
   * Create a predicate to check that a field has a certain name pattern.
   *
   * @param name pattern of the field name to check
   * @return Predicate to check that a field has a certain name pattern
   */
  @JvmStatic
  fun named(name: String) = Predicate { field: Field -> name.toRegex().matches(field.name) }

  /**
   * Create a predicate to check that a field has a certain type.
   *
   * @param type of the field to check
   * @return Predicate to check that a field has a certain type
   */
  @JvmStatic fun ofType(type: Class<*>) = Predicate { field: Field -> field.type == type }

  /**
   * Create a predicate to check that a field is defined in a given class.
   *
   * @param clazz enclosing type of the field to check
   * @return Predicate to check that a field is defined in a given class.
   */
  @JvmStatic
  fun inClass(clazz: Class<*>) = Predicate { field: Field -> field.declaringClass == clazz }

  /**
   * Create a predicate to check that a field is annotated with one of the given annotations.
   *
   * @param annotations present on the field
   * @return Predicate to check that a field is annotated with one of the given annotations.
   */
  @JvmStatic
  fun isAnnotatedWith(vararg annotations: Class<out Annotation>) = Predicate { field: Field ->
    annotations.any { field.isAnnotationPresent(it) }
  }

  /**
   * Create a predicate to check that a field has a given set of modifiers.
   *
   * @param modifiers of the field to check
   * @return Predicate to check that a field has a given set of modifiers
   */
  @JvmStatic
  fun hasModifiers(modifiers: Int) = Predicate { field: Field ->
    (modifiers and field.modifiers) == modifiers
  }
}
