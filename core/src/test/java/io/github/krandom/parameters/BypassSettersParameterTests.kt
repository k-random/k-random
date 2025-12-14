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
package io.github.krandom.parameters

import io.github.krandom.FieldPredicates.inClass
import io.github.krandom.FieldPredicates.named
import io.github.krandom.FieldPredicates.ofType
import io.github.krandom.KRandom
import io.github.krandom.KRandomParameters
import io.github.krandom.beans.Salary
import io.github.krandom.randomizers.range.IntRangeRandomizer
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.ranges.shouldBeIn
import org.junit.jupiter.api.Test

class BypassSettersParameterTests {
  @Test
  fun `when bypass setters is activated then should not invoke setters`() {
    val parameters: KRandomParameters =
      KRandomParameters()
        .bypassSetters(true)
        .randomize(
          named("amount")
            .and(ofType(Int::class.javaPrimitiveType!!))
            .and(inClass(Salary::class.java)),
          IntRangeRandomizer(-10, -1),
        )
        .excludeField(
          named("setterInvoked")
            .and(ofType(Boolean::class.javaPrimitiveType!!))
            .and(inClass(Salary::class.java))
        )
    val kRandom = KRandom(parameters)

    val salary = kRandom.nextObject(Salary::class.java)

    salary.shouldNotBeNull()
    salary.amount shouldBeIn -10..-1
    salary.isSetterInvoked.shouldNotBeNull()
    salary.isSetterInvoked.shouldBeFalse()
  }

  @Test
  fun `when bypass setters is not activated then should invoke setters`() {
    // given
    val parameters: KRandomParameters =
      KRandomParameters()
        .bypassSetters(true)
        .randomize(
          named("amount")
            .and(ofType(Int::class.javaPrimitiveType!!))
            .and(inClass(Salary::class.java)),
          IntRangeRandomizer(-10, -1),
        )
    val kRandom = KRandom(parameters)

    // when
    val salary = kRandom.nextObject(Salary::class.java)

    // then
    salary.shouldNotBeNull()
    salary.amount shouldBeIn -10..-1
    salary.isSetterInvoked.shouldBeTrue()
  }
}
