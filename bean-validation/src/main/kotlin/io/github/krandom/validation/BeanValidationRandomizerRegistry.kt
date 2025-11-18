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

import io.github.krandom.KRandomParameters
import io.github.krandom.annotation.Priority
import io.github.krandom.api.Randomizer
import io.github.krandom.api.RandomizerRegistry
import io.github.krandom.util.ReflectionUtils.isAnnotationPresent
import jakarta.validation.constraints.AssertFalse
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Negative
import jakarta.validation.constraints.NegativeOrZero
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Null
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import java.lang.reflect.Field

private const val PRIORITY = -2

/**
 * A registry of randomizers to support fields annotated with the
 * [JSR 349](http://beanvalidation.org/) annotations.
 *
 * @author Rémi Alvergnat (toilal.dev@gmail.com)
 */
@Priority(PRIORITY)
open class BeanValidationRandomizerRegistry : RandomizerRegistry {
  protected val annotationHandlers:
    MutableMap<Class<out Annotation>, BeanValidationAnnotationHandler> =
    mutableMapOf()

  internal fun getAnnotationHandlers() = annotationHandlers.toMutableMap()

  override fun init(parameters: KRandomParameters) {
    val seed = parameters.seed
    annotationHandlers[AssertFalse::class.java] = AssertFalseAnnotationHandler()
    annotationHandlers[AssertTrue::class.java] = AssertTrueAnnotationHandler()
    annotationHandlers[Null::class.java] = NullAnnotationHandler()
    annotationHandlers[Future::class.java] = FutureAnnotationHandler()
    annotationHandlers[FutureOrPresent::class.java] = FutureOrPresentAnnotationHandler()
    annotationHandlers[Past::class.java] = PastAnnotationHandler()
    annotationHandlers[PastOrPresent::class.java] = PastOrPresentAnnotationHandler()
    annotationHandlers[Min::class.java] = MinMaxAnnotationHandler(seed)
    annotationHandlers[Max::class.java] = MinMaxAnnotationHandler(seed)
    annotationHandlers[DecimalMin::class.java] = DecimalMinMaxAnnotationHandler(seed)
    annotationHandlers[DecimalMax::class.java] = DecimalMinMaxAnnotationHandler(seed)
    annotationHandlers[Pattern::class.java] = PatternAnnotationHandler(seed)
    annotationHandlers[Size::class.java] = SizeAnnotationHandler(parameters)
    annotationHandlers[Positive::class.java] = PositiveAnnotationHandler(seed)
    annotationHandlers[PositiveOrZero::class.java] = PositiveOrZeroAnnotationHandler(seed)
    annotationHandlers[Negative::class.java] = NegativeAnnotationHandler(seed)
    annotationHandlers[NegativeOrZero::class.java] = NegativeOrZeroAnnotationHandler(seed)
    annotationHandlers[NotBlank::class.java] = NotBlankAnnotationHandler(seed)
    annotationHandlers[Email::class.java] = EmailAnnotationHandler(seed)
  }

  override fun getRandomizer(field: Field): Randomizer<*>? {
    return annotationHandlers.entries
      .firstOrNull { (annotationType, _) -> isAnnotationPresent(field, annotationType) }
      ?.value
      ?.getRandomizer(field)
  }

  override fun getRandomizer(type: Class<*>): Randomizer<*>? {
    return null
  }
}
