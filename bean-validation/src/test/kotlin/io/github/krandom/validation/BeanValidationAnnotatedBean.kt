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
internal data class BeanValidationAnnotatedBean(
  @field:AssertFalse val unsupported: Boolean,
  @field:AssertTrue val active: Boolean,
  @field:DecimalMax("30.00") val maxDiscount: BigDecimal,
  @field:DecimalMin("5.00") val minDiscount: BigDecimal,
  @field:DecimalMax("1.00") @field:DecimalMin("0.01") val discount: BigDecimal,
  @field:Future val eventDate: Date,
  @field:Future val eventLocalDateTime: LocalDateTime,
  @field:FutureOrPresent val futureOrPresent: Date,
  @field:Past val birthday: Date,
  @field:Past val birthdayLocalDateTime: LocalDateTime,
  @field:Past val pastInstant: Instant,
  @field:PastOrPresent val pastOrPresent: Date,
  @field:Max(10) val maxQuantity: Int,
  @field:Min(5) val minQuantity: Int,
  @field:Positive val positive: Int,
  @field:PositiveOrZero val positiveOrZero: Int,
  @field:Negative val negative: Int,
  @field:NegativeOrZero val negativeOrZero: Int,
  @field:Positive val positiveLong: Long,
  @field:PositiveOrZero val positiveOrZeroLong: Long,
  @field:Negative val negativeLong: Long,
  @field:NegativeOrZero val negativeOrZeroLong: Long,
  @field:NotBlank val notBlank: String,
  @field:Email val email: String,
  @field:NotNull val username: String,
  @field:Null val unusedString: String?,
  @field:Size(min = 2, max = 10) val briefMessage: String,
  @field:Size(min = 2, max = 10) val sizedCollection: Collection<String>,
  @field:Size(min = 2, max = 10) val sizedList: List<String>,
  @field:Size(min = 2, max = 10) val sizedListEmbeddedBean: List<EmbeddedBean>,
  @field:Size(min = 2, max = 10) val sizedSet: Set<String>,
  @field:Size(min = 2, max = 10) val sizedMap: Map<String, Int>,
  @field:Size(min = 2, max = 10) val sizedArray: Array<String>,
  @field:Size(min = 2) val sizedString: String,
  @field:Pattern(regexp = "[a-z]{4}") val regexString: String,
)
