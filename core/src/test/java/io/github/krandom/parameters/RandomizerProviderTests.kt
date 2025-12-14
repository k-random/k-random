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
package io.github.krandom.parameters

import io.github.krandom.KRandom
import io.github.krandom.KRandomParameters
import io.github.krandom.api.Randomizer
import io.github.krandom.api.RandomizerContext
import io.github.krandom.api.RandomizerProvider
import io.github.krandom.api.RandomizerRegistry
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import java.lang.reflect.Field
import org.junit.jupiter.api.Test

internal class RandomizerProviderTests {
  @Test
  @Suppress("UNCHECKED_CAST")
  fun `test custom randomizer provider`() {
    // given
    val parameters =
      KRandomParameters()
        .randomizerProvider(
          object : RandomizerProvider {
            private lateinit var randomizerRegistries: Set<RandomizerRegistry>

            override fun setRandomizerRegistries(randomizerRegistries: Set<RandomizerRegistry>) {
              this.randomizerRegistries = randomizerRegistries
              // may sort registries with a custom sort algorithm (ie, not necessarily with
              // `@Priority`)
            }

            override fun getRandomizerByField(
              field: Field,
              context: RandomizerContext,
            ): Randomizer<*>? {
              // return custom randomizer based on the context
              return when (field.name) {
                "name" if context.currentRandomizationDepth == 0 -> {
                  Randomizer { "foo" }
                }
                "name" if context.currentField == "bestFriend" -> {
                  Randomizer { "bar" }
                }
                else -> null
              }
            }

            override fun <T> getRandomizerByType(
              type: Class<T>,
              context: RandomizerContext,
            ): Randomizer<T>? {
              return randomizerRegistries.firstNotNullOfOrNull { it.getRandomizer(type) }
                as Randomizer<T>?
            }
          }
        )
        .randomizationDepth(2)
    val kRandom = KRandom(parameters)

    // when
    val foo = kRandom.nextObject(Foo::class.java)

    // then
    foo.shouldNotBeNull()
    foo.name shouldBe "foo"
    foo.bestFriend.shouldNotBeNull()
    foo.bestFriend?.name shouldBe "bar"
    foo.bestFriend?.bestFriend.shouldNotBeNull()
  }

  @Suppress("unused") internal data class Foo(var name: String, var bestFriend: Foo?)
}
