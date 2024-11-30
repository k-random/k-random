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
package io.github.krandom.randomizers

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class PasswordRandomizerTest : FakerBasedRandomizerTest<String>() {
  private val specialCharacterRegex = "[^A-Za-z0-9]".toRegex()

  private val upperCharacterRegex = "[A-Z]".toRegex()

  @Test
  fun `test password not contains uppercase special`() {
    randomizer = PasswordRandomizer(123L, 8, 20)

    val randomValue = randomizer.getRandomValue()

    upperCharacterRegex.containsMatchIn(randomValue) shouldBe false
    specialCharacterRegex.containsMatchIn(randomValue) shouldBe false
  }

  @Test
  fun `test password contains uppercase`() {
    randomizer = PasswordRandomizer(123L, 8, 20, true)

    val randomValue = randomizer.getRandomValue()

    upperCharacterRegex.containsMatchIn(randomValue) shouldBe true
    specialCharacterRegex.containsMatchIn(randomValue) shouldBe false
  }

  @Test
  fun `test password contains uppercase special`() {
    randomizer = PasswordRandomizer(123L, 8, 20, true, true)

    val randomValue = randomizer.getRandomValue()

    upperCharacterRegex.containsMatchIn(randomValue) shouldBe true
    specialCharacterRegex.containsMatchIn(randomValue) shouldBe true
  }
}
