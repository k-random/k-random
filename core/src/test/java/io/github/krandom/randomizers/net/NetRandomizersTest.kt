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
package io.github.krandom.randomizers.net

import io.github.krandom.KRandom
import io.github.krandom.api.Randomizer
import io.github.krandom.beans.Website
import io.github.krandom.randomizers.AbstractRandomizerTest
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import java.net.URI
import java.net.URL
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class NetRandomizersTest : AbstractRandomizerTest<Randomizer<*>?>() {
  @ParameterizedTest
  @MethodSource("generateRandomizers")
  fun `generated value should not be null`(randomizer: Randomizer<*>) {
    // when
    val value: Any? = randomizer.getRandomValue()

    value.shouldNotBeNull()
  }

  @ParameterizedTest
  @MethodSource("generateSeededRandomizersAndTheirExpectedValues")
  fun `should generate the same value for the same seed`(
    randomizer: Randomizer<*>,
    expected: Any?,
  ) {
    // when
    val actual: Any? = randomizer.getRandomValue()

    actual.shouldNotBeNull()
    actual shouldBe expected
  }

  @Test
  fun `java net types should be populated`() {
    // when
    val website = KRandom().nextObject(Website::class.java)

    website.shouldNotBeNull()
    website.url.shouldNotBeNull()
    website.uri.shouldNotBeNull()
  }

  companion object {
    @JvmStatic
    fun generateRandomizers() = listOf(Arguments.of(UriRandomizer()), Arguments.of(UrlRandomizer()))

    @JvmStatic
    @Throws(Exception::class)
    fun generateSeededRandomizersAndTheirExpectedValues() =
      listOf(
        Arguments.of(UriRandomizer(SEED), URI("telnet://192.0.2.16:80/")),
        Arguments.of(UrlRandomizer(SEED), URL.of(URI("http://www.google.com"), null)),
      )
  }
}
