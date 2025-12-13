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

import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import java.util.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class OptionalPopulatorTest {
  @MockK private lateinit var kRandom: KRandom
  @MockK private lateinit var context: RandomizationContext

  private lateinit var optionalPopulator: OptionalPopulator

  @BeforeEach
  fun setUp() {
    optionalPopulator = OptionalPopulator(kRandom)
  }

  @Test
  @Throws(Exception::class)
  fun testOptionalRandomization() {
    // given
    val field = Foo::class.java.getDeclaredField("name")
    every { kRandom.doPopulateBean(String::class.java, context) } returns "foobar"

    // when
    val randomOptional = optionalPopulator.getRandomOptional(field, context)

    // then
    randomOptional.isPresent.shouldBeTrue()
    randomOptional.get() shouldBe "foobar"
    verify { kRandom.doPopulateBean(String::class.java, context) }
  }

  @Test
  @Throws(Exception::class)
  fun rawOptionalShouldBeGeneratedAsEmpty() {
    // given
    val field = Bar::class.java.getDeclaredField("name")

    // when
    val randomOptional = optionalPopulator.getRandomOptional(field, context)

    // then
    randomOptional.isEmpty.shouldBeTrue()
  }

  internal data class Foo(val name: Optional<String>)

  internal data class Bar(val name: Optional<*>)
}
