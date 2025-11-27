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

import java.lang.reflect.Modifier
import java.util.function.Predicate

/**
 * Common predicates to identify types. Usually used in combination to define a collection of types.
 * For example:
 * <pre>
 * Predicate&lt;Class&lt;?&gt;&gt; predicate = inPackage("java.util").or(inPackage("com.sun"));
 * </pre> *
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
object TypePredicates {
  /**
   * Create a predicate to check that a type has a given name.
   *
   * @param name name on the type
   * @return Predicate to check that a type has a given name.
   */
  @JvmStatic fun named(name: String) = Predicate { clazz: Class<*> -> clazz.getName() == name }

  /**
   * Create a predicate to check that a class has a certain type.
   *
   * @param type of the class to check
   * @return Predicate to check that a class has a certain type
   */
  @JvmStatic fun ofType(type: Class<*>) = Predicate { clazz: Class<*> -> clazz == type }

  /**
   * Create a predicate to check that a type is defined in a given package.
   *
   * @param packageNamePrefix prefix of the package name
   * @return Predicate to check that a type is defined in a given package.
   */
  @JvmStatic
  fun inPackage(packageNamePrefix: String) = Predicate { clazz: Class<*> ->
    clazz.getPackage().name.startsWith(packageNamePrefix)
  }

  /**
   * Create a predicate to check that a type is annotated with one of the given annotations.
   *
   * @param annotations present on the type
   * @return Predicate to check that a type is annotated with one of the given annotations.
   */
  @SafeVarargs
  @JvmStatic
  fun isAnnotatedWith(vararg annotations: Class<out Annotation>) = Predicate { clazz: Class<*> ->
    annotations.any(clazz::isAnnotationPresent)
  }

  @JvmStatic fun isInterface() = Predicate { obj: Class<*> -> obj.isInterface }

  @JvmStatic fun isAbstract() = hasModifiers(Modifier.ABSTRACT)

  /**
   * Create a predicate to check that a type has a given set of modifiers.
   *
   * @param modifiers of the type to check
   * @return Predicate to check that a type has a given set of modifiers
   */
  @JvmStatic
  fun hasModifiers(modifiers: Int) = Predicate { clazz: Class<*> ->
    (modifiers and clazz.modifiers) == modifiers
  }

  @JvmStatic fun isEnum() = Predicate { obj: Class<*> -> obj.isEnum }

  @JvmStatic fun isArray() = Predicate { obj: Class<*> -> obj.isArray }

  /**
   * Create a predicate to check if a type is assignable from another type.
   *
   * @param type to check
   * @return a predicate to check if a type is assignable from another type.
   */
  fun isAssignableFrom(type: Class<*>) = Predicate { clazz: Class<*> ->
    clazz.isAssignableFrom(type)
  }
}
