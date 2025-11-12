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
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe
import java.math.BigDecimal
import java.util.Locale
import org.junit.jupiter.api.Test

internal class LocaleRandomizerTest : AbstractRandomizerTest<Locale>() {
  @Test
  fun shouldGenerateRandomLocale() {
    LocaleRandomizer().getRandomValue() shouldBeIn Locale.getAvailableLocales()
  }

  @Test
  fun shouldGenerateTheSameValueForTheSameSeed() {
    val javaVersion = BigDecimal(System.getProperty("java.specification.version"))
    val locale = LocaleRandomizer(SEED).getRandomValue()
    val (language, country) =
      when {
        javaVersion >= BigDecimal("21") -> "pl" to "PL"
        javaVersion >= BigDecimal("17") -> "mni" to ""
        javaVersion >= BigDecimal("14") -> "rn" to "BI"
        javaVersion >= BigDecimal("13") -> "zh" to "CN"
        javaVersion >= BigDecimal("11") -> "en" to "CK"
        javaVersion >= BigDecimal("9") -> "sw" to "KE"
        else -> "nl" to "BE"
      }
    locale.language shouldBe language
    locale.country shouldBe country
  }
}
