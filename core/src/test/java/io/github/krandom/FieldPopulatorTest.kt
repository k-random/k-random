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

import io.github.krandom.api.ContextAwareRandomizer
import io.github.krandom.api.Randomizer
import io.github.krandom.beans.ArrayBean
import io.github.krandom.beans.CollectionBean
import io.github.krandom.beans.Human
import io.github.krandom.beans.MapBean
import io.github.krandom.beans.Person
import io.github.krandom.randomizers.misc.SkipRandomizer
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class FieldPopulatorTest {
  @MockK(relaxed = true) private lateinit var kRandom: KRandom
  @MockK(relaxed = true) private lateinit var randomizerProvider: RegistriesRandomizerProvider
  @MockK(relaxed = true) private lateinit var randomizer: Randomizer<*>
  @MockK(relaxed = true) private lateinit var contextAwareRandomizer: ContextAwareRandomizer<*>
  @MockK(relaxed = true) private lateinit var arrayPopulator: ArrayPopulator
  @MockK(relaxed = true) private lateinit var collectionPopulator: CollectionPopulator
  @MockK(relaxed = true) private lateinit var mapPopulator: MapPopulator
  @MockK(relaxed = true) private lateinit var optionalPopulator: OptionalPopulator

  private lateinit var fieldPopulator: FieldPopulator

  @BeforeEach
  fun setUp() {
    every { randomizerProvider.getRandomizerByField(any(), any()) } returns null
    every { randomizerProvider.getRandomizerByType(any<Class<*>>(), any()) } returns null
    fieldPopulator =
      FieldPopulator(
        kRandom,
        randomizerProvider,
        arrayPopulator,
        collectionPopulator,
        mapPopulator,
        optionalPopulator,
      )
  }

  @Test
  @Throws(Exception::class)
  fun `when skip randomizer is registered for the field then the field should be skipped`() {
    val name = Human::class.java.getDeclaredField("name")
    val human = Human()
    randomizer = SkipRandomizer()
    val context = RandomizationContext(Human::class.java, KRandomParameters())
    every { randomizerProvider.getRandomizerByField(name, context) } returns randomizer

    fieldPopulator.populateField(human, name, context)

    human.name.shouldBeNull()
  }

  @Test
  @Throws(Exception::class)
  fun `when custom randomizer is registered for the field then the field should be populated with the random value`() {
    val name = Human::class.java.getDeclaredField("name")
    val human = Human()
    val context = RandomizationContext(Human::class.java, KRandomParameters())
    every { randomizerProvider.getRandomizerByField(name, context) } returns randomizer
    every { randomizer.getRandomValue() } returns NAME

    fieldPopulator.populateField(human, name, context)

    human.name shouldBe NAME
  }

  @Test
  @Throws(Exception::class)
  fun `when context aware randomizer is registered for the field then should be on top of the supplied stack`() {
    val name = Human::class.java.getDeclaredField("name")
    val human = Human()
    val context = RandomizationContext(Human::class.java, KRandomParameters())
    val currentObjectFromContext = arrayOfNulls<Human>(1)
    every { randomizerProvider.getRandomizerByField(name, context) } returns contextAwareRandomizer
    every { contextAwareRandomizer.getRandomValue() } returns NAME
    every { contextAwareRandomizer.setRandomizerContext(context) } answers
      {
        currentObjectFromContext[0] = firstArg<RandomizationContext>().getCurrentObject() as Human
      }

    fieldPopulator.populateField(human, name, context)

    currentObjectFromContext[0] shouldBe human
  }

  @Test
  @Throws(Exception::class)
  fun `when the field is of type array then should delegate population to array populator`() {
    val strings = ArrayBean::class.java.getDeclaredField("strings")
    val arrayBean = ArrayBean()
    val array = arrayOfNulls<String>(0)
    val context = RandomizationContext(ArrayBean::class.java, KRandomParameters())
    every { arrayPopulator.getRandomArray(strings.type, context) } returns array

    fieldPopulator.populateField(arrayBean, strings, context)

    arrayBean.strings shouldBeEqual array
  }

  @Test
  @Throws(Exception::class)
  fun `when the field is of type collection then should delegate population to collection populator`() {
    val strings = CollectionBean::class.java.getDeclaredField("typedCollection")
    val collectionBean = CollectionBean()
    val persons: MutableCollection<Person> = mutableListOf()
    val context = RandomizationContext(CollectionBean::class.java, KRandomParameters())
    every { collectionPopulator.getRandomCollection(strings, context) } returns persons

    fieldPopulator.populateField(collectionBean, strings, context)

    collectionBean.typedCollection shouldBeEqual persons
  }

  @Test
  @Throws(Exception::class)
  fun `when the field is of type map then should delegate population to map populator`() {
    val strings = MapBean::class.java.getDeclaredField("typedMap")
    val mapBean = MapBean()
    val idToPerson: MutableMap<Int, Person> = mutableMapOf()
    val context = RandomizationContext(MapBean::class.java, KRandomParameters())

    fieldPopulator.populateField(mapBean, strings, context)

    mapBean.typedMap shouldBeEqual idToPerson
  }

  @Test
  @Throws(Exception::class)
  fun `when randomization depth is exceeded then fields are not initialized`() {
    val name = Human::class.java.getDeclaredField("name")
    val human = Human()
    val context = mockk<RandomizationContext>(relaxed = true)
    every { context.hasExceededRandomizationDepth() } returns true

    fieldPopulator.populateField(human, name, context)

    human.name.shouldBeNull()
  }

  companion object {
    private const val NAME = "foo"
  }
}
