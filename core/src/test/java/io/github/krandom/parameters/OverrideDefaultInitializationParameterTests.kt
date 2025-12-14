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

import io.github.krandom.KRandom
import io.github.krandom.KRandomParameters
import io.github.krandom.beans.BeanWithDefaultFieldValues
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

internal class OverrideDefaultInitializationParameterTests {
  @Test
  fun `when override default initialization parameter is false then should keep default field values`() {
    val parameters = KRandomParameters().overrideDefaultInitialization(false)
    val kRandom = KRandom(parameters)

    val bean = kRandom.nextObject(BeanWithDefaultFieldValues::class.java)

    bean.shouldNotBeNull()
    bean.defaultNonNullValue shouldBe "default"
    bean.defaultNonNullValueSetByConstructor shouldBe "defaultSetByConstructor"
  }

  @Test
  fun `when override default initialization parameter is true then should randomize fields`() {
    val parameters = KRandomParameters().overrideDefaultInitialization(true)
    val kRandom = KRandom(parameters)

    val bean = kRandom.nextObject(BeanWithDefaultFieldValues::class.java)

    // Then
    bean.shouldNotBeNull()
    bean.defaultNonNullValue.shouldNotBeNull()
    bean.defaultNonNullValue shouldNotBe "default"
    bean.defaultNonNullValueSetByConstructor.shouldNotBeNull()
    bean.defaultNonNullValueSetByConstructor shouldNotBe "defaultSetByConstructor"
  }

  @Test
  fun `should not override default field values by default`() {
    val bean = KRandom().nextObject(BeanWithDefaultFieldValues::class.java)

    bean.shouldNotBeNull()
    bean.defaultNonNullValue.shouldNotBeNull()
    bean.defaultNonNullValue shouldBe "default"
    bean.defaultNonNullValueSetByConstructor shouldBe "defaultSetByConstructor"
  }
}
