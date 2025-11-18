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
package io.github.krandom.validation

import io.github.krandom.annotation.Priority
import io.github.krandom.api.Randomizer
import io.github.krandom.randomizers.faker.RegularExpressionRandomizer
import io.github.krandom.util.ReflectionUtils
import jakarta.validation.constraints.Pattern
import java.lang.reflect.Field
import java.util.*

private const val PATTERN_HANDLER_PRIORITY = -5

@Priority(PATTERN_HANDLER_PRIORITY)
internal class PatternAnnotationHandler(seed: Long) : BeanValidationAnnotationHandler {
  private val random: Random = Random(seed)

  @Suppress("UNCHECKED_CAST")
  override fun getRandomizer(field: Field): Randomizer<*>? {
    val fieldType = field.type
    val patternAnnotation =
      ReflectionUtils.getAnnotation<Pattern?>(field, Pattern::class.java as Class<Pattern?>)
        ?: return null

    val regex: String = patternAnnotation.regexp
    return if (fieldType == String::class.java) {
      RegularExpressionRandomizer(regex, random.nextLong())
    } else null
  }
}
