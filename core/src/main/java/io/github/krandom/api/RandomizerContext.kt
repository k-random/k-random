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

import io.github.krandom.KRandom
import io.github.krandom.KRandomParameters

/**
 * A context object for a [Randomizer]. This interface provides information about the randomization
 * context.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
interface RandomizerContext {
  /**
   * Return the target type (parameter of [KRandom.nextObject]).
   *
   * @return target type
   */
  val targetType: Class<*>

  /**
   * Return the root object being randomized (instance of [RandomizerContext.targetType].
   *
   * @return root object being randomized
   */
  val rootObject: Any

  /**
   * Return the currently randomized object in the object graph.
   *
   * @return currently randomized object
   */
  val currentObject: Any

  /**
   * Return the full path to the current field being randomized (starting from the first field in
   * the root type).
   *
   * @return full path to the current field being randomized
   */
  val currentField: String

  /**
   * Get the current level in the hierarchy of the object graph.
   *
   * @return current level in the hierarchy of the object graph.
   */
  val currentRandomizationDepth: Int

  /**
   * Return the currently used parameters by the enclosing [KRandom].
   *
   * @return currently used parameters
   */
  val parameters: KRandomParameters
}
