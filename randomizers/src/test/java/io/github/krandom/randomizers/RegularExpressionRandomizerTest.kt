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

import io.kotest.matchers.string.shouldMatch
import org.junit.jupiter.api.Test

internal class RegularExpressionRandomizerTest {
  @Test
  fun `leading boundary matcher is removed`() {
    val regex = "^A"
    val randomizer = RegularExpressionRandomizer(regex)

    val actual = randomizer.getRandomValue()

    actual shouldMatch regex.toRegex()
  }

  @Test
  fun `tailing boundary matcher is removed`() {
    val regex = "A$"
    val randomizer = RegularExpressionRandomizer(regex)

    val actual = randomizer.getRandomValue()

    actual shouldMatch regex.toRegex()
  }

  @Test
  fun `leading and tailing boundary matcher is removed`() {
    val regex = "^A$"
    val randomizer = RegularExpressionRandomizer(regex)

    val actual = randomizer.getRandomValue()

    actual shouldMatch regex.toRegex()
  }

  @Test
  fun `more complicated regular expression works`() {
    val regex = "^[A-Z]{1,2}\\d[A-Z\\d]? ?\\d[A-Z]{2}$"
    val randomizer = RegularExpressionRandomizer(regex)

    val actual = randomizer.getRandomValue()

    actual shouldMatch regex.toRegex()
  }
}
