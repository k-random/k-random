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
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class CollectionRandomizersTest {
  @ParameterizedTest
  @MethodSource("generateCollectionRandomizers")
  fun <T> `generated collection should not be null`(
    collectionRandomizer: Randomizer<MutableCollection<T>>
  ) {
    // when
    val randomCollection = collectionRandomizer.getRandomValue()

    randomCollection.shouldNotBeEmpty()
  }

  @ParameterizedTest
  @MethodSource("generateCollectionRandomizersWithSpecificSize")
  fun <T> `generated collection size should be equal to the specified size`(
    collectionRandomizer: Randomizer<MutableCollection<T>>
  ) {
    // when
    val randomCollection = collectionRandomizer.getRandomValue()

    randomCollection shouldHaveSize COLLECTION_SIZE
  }

  @ParameterizedTest
  @MethodSource("generateCollectionRandomizersForEmptyCollections")
  fun <T> `should allow generating empty collections`(
    collectionRandomizer: Randomizer<MutableCollection<T>>
  ) {
    // when
    val randomCollection = collectionRandomizer.getRandomValue()

    randomCollection.shouldBeEmpty()
  }

  @ParameterizedTest
  @MethodSource("generateCollectionRandomizersWithIllegalSize")
  fun `specified size should be positive`(invokable: () -> Randomizer<MutableCollection<String>>) {
    // then
    shouldThrow<IllegalArgumentException> { invokable.invoke() }
  }

  companion object {
    private const val COLLECTION_SIZE = 3

    @JvmStatic
    fun generateCollectionRandomizers() =
      listOf(
        Arguments.of(ListRandomizer(elementRandomizer())),
        Arguments.of(QueueRandomizer(elementRandomizer())),
        Arguments.of(SetRandomizer(elementRandomizer())),
      )

    @JvmStatic
    fun generateCollectionRandomizersWithSpecificSize() =
      listOf(
        Arguments.of(ListRandomizer(elementRandomizer(), COLLECTION_SIZE)),
        Arguments.of(QueueRandomizer(elementRandomizer(), COLLECTION_SIZE)),
        Arguments.of(SetRandomizer(elementRandomizer(), COLLECTION_SIZE)),
      )

    @JvmStatic
    fun generateCollectionRandomizersForEmptyCollections() =
      listOf(
        Arguments.of(ListRandomizer(elementRandomizer(), 0)),
        Arguments.of(QueueRandomizer(elementRandomizer(), 0)),
        Arguments.of(SetRandomizer(elementRandomizer(), 0)),
      )

    @JvmStatic
    fun generateCollectionRandomizersWithIllegalSize() =
      listOf(
        Arguments.of({ ListRandomizer(elementRandomizer(), -1) }),
        Arguments.of({ QueueRandomizer(elementRandomizer(), -1) }),
        Arguments.of({ SetRandomizer(elementRandomizer(), -1) }),
      )

    @JvmStatic
    fun elementRandomizer(): Randomizer<String> {
      val elementRandomizer: Randomizer<String> = mockk<Randomizer<String>>()
      every { elementRandomizer.getRandomValue() } returnsMany listOf("a", "b", "c")
      return elementRandomizer
    }
  }
}
