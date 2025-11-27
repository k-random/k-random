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
package io.github.krandom.randomizers.text

import io.github.krandom.KRandomParameters
import io.github.krandom.randomizers.AbstractRandomizerTest
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import io.kotest.matchers.ranges.shouldBeIn
import io.kotest.matchers.shouldBe
import java.nio.charset.StandardCharsets
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class StringRandomizerTest : AbstractRandomizerTest<String>() {
  @BeforeEach
  fun setUp() {
    randomizer = StringRandomizer()
  }

  @Test
  fun `should generate the same value for the same seed`() {
    // Given
    randomizer =
      StringRandomizer(
        StandardCharsets.US_ASCII,
        KRandomParameters.DEFAULT_STRING_LENGTH_RANGE.min,
        KRandomParameters.DEFAULT_STRING_LENGTH_RANGE.max,
        SEED,
      )
    val expected = "eOMtThyhVNLWUZNRcBaQKxI"

    // When
    val actual = randomizer.getRandomValue()

    // Then
    actual shouldBe expected
  }

  @Test
  fun `the length of the generated value should be lower than the specified max length`() {
    // Given
    val maxLength = 10
    randomizer =
      StringRandomizer(
        StandardCharsets.US_ASCII,
        KRandomParameters.DEFAULT_STRING_LENGTH_RANGE.min,
        maxLength,
        SEED,
      )
    val expectedValue = "eOMtThy"

    // When
    val actual = randomizer.getRandomValue()

    // Then
    actual shouldBe expectedValue
    actual.length shouldBeLessThanOrEqualTo maxLength
  }

  @Test
  fun `the length of the generated value should be greater than the specified min length`() {
    // Given
    val minLength = 3
    val maxLength = 10
    randomizer = StringRandomizer(StandardCharsets.US_ASCII, minLength, maxLength, SEED)
    val expectedValue = "eOMtThyh"

    // When
    val actual = randomizer.getRandomValue()

    // Then
    actual shouldBe expectedValue
    actual.length shouldBeIn minLength..maxLength
  }
}
