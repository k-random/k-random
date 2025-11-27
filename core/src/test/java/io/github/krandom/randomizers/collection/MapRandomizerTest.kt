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
package io.github.krandom.randomizers.collection

import io.github.krandom.api.Randomizer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.maps.shouldNotBeEmpty
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class MapRandomizerTest {
  @MockK private lateinit var keyRandomizer: Randomizer<Int>
  @MockK private lateinit var valueRandomizer: Randomizer<String>

  @Test
  fun `generated map should not be empty`() {
    var counter = 0
    every { keyRandomizer.getRandomValue() } answers
      {
        counter++
        counter
      }
    every { valueRandomizer.getRandomValue() } returns "k-random"

    MapRandomizer(keyRandomizer, valueRandomizer).getRandomValue().shouldNotBeEmpty()
  }

  @Test
  fun `generated map size should be equal to the specified size`() {
    every { keyRandomizer.getRandomValue() } returnsMany listOf(1, 2, 3)
    every { valueRandomizer.getRandomValue() } returnsMany listOf("a", "b", "c")

    MapRandomizer(keyRandomizer, valueRandomizer, 3).getRandomValue() shouldHaveSize 3
  }

  @Test
  fun `specified size can be zero`() {
    MapRandomizer(keyRandomizer, valueRandomizer, 0).getRandomValue().shouldBeEmpty()
  }

  @Test
  fun `specified size should be positive`() {
    shouldThrow<IllegalArgumentException> { MapRandomizer(keyRandomizer, valueRandomizer, -3) }
  }
}
