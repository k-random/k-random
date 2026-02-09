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
package io.github.krandom

import io.github.krandom.api.Randomizer
import io.github.krandom.api.RandomizerContext
import io.github.krandom.api.RandomizerProvider
import io.github.krandom.api.RandomizerRegistry
import java.lang.reflect.Field
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Central class to get registered randomizers by Field or by Type.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
@Suppress("UNCHECKED_CAST")
internal class RegistriesRandomizerProvider : RandomizerProvider {
  private val registries: MutableList<RandomizerRegistry> = CopyOnWriteArrayList()

  override fun getRandomizerByField(field: Field, context: RandomizerContext): Randomizer<*>? =
    getRandomizer(ByFieldProvider(field))

  override fun <T> getRandomizerByType(type: Class<T>, context: RandomizerContext): Randomizer<T>? =
    getRandomizer(ByTypeProvider(type)) as? Randomizer<T>?

  override fun setRandomizerRegistries(randomizerRegistries: Set<RandomizerRegistry>) =
    registries.apply { addAll(randomizerRegistries) }.sortWith(PriorityComparator)

  private fun getRandomizer(provider: Provider): Randomizer<*>? {
    val randomizer =
      registries.mapNotNull(provider::getRandomizer).minWithOrNull(PriorityComparator)
    return randomizer
  }

  private fun interface Provider {
    fun getRandomizer(registry: RandomizerRegistry): Randomizer<*>?
  }

  private class ByTypeProvider(private val type: Class<*>) : Provider {
    override fun getRandomizer(registry: RandomizerRegistry): Randomizer<*>? =
      registry.getRandomizer(type)
  }

  private class ByFieldProvider(private val field: Field) : Provider {
    override fun getRandomizer(registry: RandomizerRegistry): Randomizer<*>? =
      registry.getRandomizer(field)
  }
}
