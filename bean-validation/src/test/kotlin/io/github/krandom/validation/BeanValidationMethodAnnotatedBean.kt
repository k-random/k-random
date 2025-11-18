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
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.util.Date

@Suppress("ArrayInDataClass")
internal data class BeanValidationMethodAnnotatedBean(
  @get:AssertFalse val unsupported: Boolean,
  @get:AssertTrue val active: Boolean,
  @get:DecimalMax("30.00") val maxDiscount: BigDecimal,
  @get:DecimalMin("5.00") val minDiscount: BigDecimal,
  @get:DecimalMax("1.00") @get:DecimalMin("0.01") val discount: BigDecimal,
  @get:Positive val positive: Int,
  @get:PositiveOrZero val positiveOrZero: Int,
  @get:Negative val negative: Int,
  @get:NegativeOrZero val negativeOrZero: Int,
  @get:Positive val positiveLong: Long,
  @get:PositiveOrZero val positiveOrZeroLong: Long,
  @get:NegativeOrZero val negativeLong: Long,
  @get:NegativeOrZero val negativeOrZeroLong: Long,
  @get:NotBlank val notBlank: String,
  @get:Email val email: String,
  @get:Future val eventDate: Date,
  @get:Future val eventLocalDateTime: LocalDateTime,
  @get:Past val birthday: Date,
  @get:Past val birthdayLocalDateTime: LocalDateTime,
  @get:PastOrPresent val pastOrPresent: Date,
  @get:FutureOrPresent val futureOrPresent: Date,
  @get:Past val pastInstant: Instant,
  @get:Max(10) val maxQuantity: Int,
  @get:Min(5) val minQuantity: Int,
  @get:NotNull val username: String,
  @get:Null val unusedString: String,
  @get:Size(min = 2, max = 10) val briefMessage: String,
  @get:Size(min = 2, max = 10) val sizedCollection: MutableCollection<String>,
  @get:Size(min = 2, max = 10) val sizedList: MutableList<String>,
  @get:Size(min = 2, max = 10) val sizedListEmbeddedBean: MutableList<EmbeddedBean>,
  @get:Size(min = 2, max = 10) val sizedSet: MutableSet<String>,
  @get:Size(min = 2, max = 10) val sizedMap: MutableMap<String, Int>,
  @get:Size(min = 2, max = 10) val sizedArray: Array<String>,
  @get:Size(min = 2) val sizedString: String,
  @get:Pattern(regexp = "[a-z]{4}") val regexString: String,
)
