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
package io.github.krandom.util

import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import io.github.classgraph.ScanResult
import java.util.concurrent.ConcurrentHashMap

/**
 * Facade for [ClassGraph]. It is a separate object from [ReflectionUtils], so that the classpath
 * scanning - which can take a few seconds - is only done when necessary.
 *
 * @author [Pascal Schumacher](https://github.com/PascalSchumacher)
 */
internal object ClassGraphFacade {
  private val typeToConcreteSubTypes = ConcurrentHashMap<Class<*>, List<Class<*>>>()
  private val scanResult: ScanResult =
    ClassGraph().enableSystemJarsAndModules().enableClassInfo().scan()

  /**
   * Searches the classpath for all public concrete subtypes of the given interface or abstract
   * class.
   *
   * @param type to search concrete subtypes of
   * @return a list of all concrete subtypes found
   */
  @JvmStatic
  fun <T> getPublicConcreteSubTypesOf(type: Class<T>): List<Class<*>> =
    typeToConcreteSubTypes.computeIfAbsent(type) { obj: Class<*> ->
      searchForPublicConcreteSubTypesOf(obj)
    }

  private fun <T> searchForPublicConcreteSubTypesOf(type: Class<T>): List<Class<*>> {
    return if (type.isInterface)
      scanResult
        .getClassesImplementing(type.name)
        .filter { subType: ClassInfo -> subType.isPublic && !subType.isAbstract }
        .loadClasses(true)
        .toList()
    else
      scanResult
        .getSubclasses(type.name)
        .filter { subType: ClassInfo -> subType.isPublic && !subType.isAbstract }
        .loadClasses(true)
        .toList()
  }
}
