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
package io.github.krandom.randomizers.misc

import io.github.krandom.randomizers.AbstractRandomizerTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class EnumRandomizerTest {
  enum class Gender {
    MALE,
    FEMALE,
    NON_BINARY,
  }

  @Nested
  inner class GenderRandomizerTest : AbstractRandomizerTest<Gender?>() {
    @Test
    fun `generated value should be of the specified enum`() {
      randomizer = EnumRandomizer(Gender::class)

      val gender = randomizer.getRandomValue()

      gender shouldBeIn Gender.entries
    }

    @Test
    fun `should always generate the same value for the same seed`() {
      randomizer = EnumRandomizer(Gender::class, SEED)

      val gender = randomizer.getRandomValue()

      gender shouldBe Gender.NON_BINARY
    }

    @Test
    fun `should return a value different from the excluded one`() {
      val valueToExclude = Gender.MALE
      randomizer = EnumRandomizer(Gender::class, SEED, valueToExclude)

      val gender = randomizer.getRandomValue()

      gender.shouldNotBeNull()
      gender shouldBeIn Gender.entries.filterNot { it == valueToExclude }
    }

    @Test
    fun `should throw an exception when all values are excluded`() {
      shouldThrow<IllegalArgumentException> {
        EnumRandomizer(Gender::class, SEED, *Gender.entries.toTypedArray())
      }
    }
  }

  // always keep three options here, as we want to exclude one and still select the same one
  // deterministically
  @Suppress("unused")
  enum class TriState {
    TRUE,
    FALSE,
    MAYBE,
  }

  @Nested
  inner class TriStateRandomizerTest : AbstractRandomizerTest<TriState?>() {
    @Test
    fun `should always generate the same value for the same seed with excluded values`() {
      randomizer = EnumRandomizer(TriState::class, SEED, TriState.MAYBE)

      val tristate = randomizer.getRandomValue()

      tristate shouldBe TriState.FALSE
    }
  }

  enum class Empty

  @Nested
  inner class EmptyEnumRandomizerTest : AbstractRandomizerTest<Empty?>() {
    @Test
    fun `should return null for empty enum`() {
      randomizer = EnumRandomizer(Empty::class)

      val empty = randomizer.getRandomValue()

      empty.shouldBeNull()
    }
  }
}
