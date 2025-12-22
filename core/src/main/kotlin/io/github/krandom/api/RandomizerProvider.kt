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
package io.github.krandom.api

import java.lang.reflect.Field

/**
 * Strategy interface to provide randomizers for field/type based on the current context.
 * Implementations may (or may not) use registries to provide randomizers.
 *
 * The added value of this interface compared to a simple [RandomizerRegistry] is that it gives
 * access to the current context and allows fine grained randomizer selection based on that context.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
interface RandomizerProvider {
  /**
   * Return a randomizer for the given field in the current context.
   *
   * @param field for which a randomizer should be returned
   * @param context current randomization context
   * @return a randomizer for the given field in the current context.
   */
  fun getRandomizerByField(field: Field, context: RandomizerContext): Randomizer<*>? {
    return null
  }

  /**
   * Return a randomizer for the given type in the current context.
   *
   * @param type for which a randomizer should be returned
   * @param context current randomization context
   * @param <T> generic type
   * @return a randomizer for the given type in the current context. </T>
   */
  fun <T> getRandomizerByType(type: Class<T>, context: RandomizerContext): Randomizer<T>? {
    return null
  }

  /**
   * Set randomizer registries.
   *
   * @param randomizerRegistries to set
   */
  fun setRandomizerRegistries(randomizerRegistries: Set<RandomizerRegistry>)
}
