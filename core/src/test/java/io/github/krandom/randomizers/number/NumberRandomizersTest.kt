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
package io.github.krandom.randomizers.number

import io.github.krandom.api.Randomizer
import io.github.krandom.randomizers.AbstractRandomizerTest
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import java.math.BigDecimal
import java.math.BigInteger
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class NumberRandomizersTest : AbstractRandomizerTest<Any?>() {
  @ParameterizedTest
  @MethodSource("generateRandomizers")
  fun `generated number should not be null`(randomizer: Randomizer<*>) {
    // when
    val randomNumber: Any? = randomizer.getRandomValue()

    randomNumber.shouldNotBeNull()
  }

  @ParameterizedTest
  @MethodSource("generateSeededRandomizersAndTheirExpectedValues")
  fun `should generate the same value for the same seed`(
    randomizer: Randomizer<*>,
    expected: Any?,
  ) {
    // when
    val actual: Any? = randomizer.getRandomValue()

    actual shouldBe expected
  }

  companion object {
    @JvmStatic
    fun generateRandomizers() =
      listOf(
        Arguments.of(ByteRandomizer()),
        Arguments.of(ShortRandomizer()),
        Arguments.of(IntegerRandomizer()),
        Arguments.of(NumberRandomizer()),
        Arguments.of(LongRandomizer()),
        Arguments.of(FloatRandomizer()),
        Arguments.of(DoubleRandomizer()),
        Arguments.of(BigDecimalRandomizer()),
        Arguments.of(BigIntegerRandomizer()),
      )

    @JvmStatic
    fun generateSeededRandomizersAndTheirExpectedValues() =
      listOf(
        Arguments.of(ByteRandomizer(SEED), (-35).toByte()),
        Arguments.of(ShortRandomizer(SEED), (-3619).toShort()),
        Arguments.of(IntegerRandomizer(SEED), -1188957731),
        Arguments.of(NumberRandomizer(SEED), -1188957731),
        Arguments.of(LongRandomizer(SEED), -5106534569952410475L),
        Arguments.of(FloatRandomizer(SEED), 0.72317415f),
        Arguments.of(DoubleRandomizer(SEED), 0.7231742029971469),
        Arguments.of(
          BigDecimalRandomizer(SEED),
          BigDecimal("0.723174202997146853277854461339302361011505126953125"),
        ),
        Arguments.of(
          BigIntegerRandomizer(SEED),
          BigInteger("295011414634219278107705585431435293517"),
        ),
      )
  }
}
