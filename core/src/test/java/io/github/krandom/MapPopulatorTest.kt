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

import io.github.krandom.api.ObjectFactory
import io.github.krandom.beans.CompositeMapBean
import io.github.krandom.beans.CustomMap
import io.github.krandom.beans.EnumMapBean
import io.github.krandom.beans.MapBean
import io.github.krandom.beans.Person
import io.github.krandom.beans.WildCardMapBean
import io.kotest.inspectors.forAll
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
@Suppress("UNCHECKED_CAST")
internal class MapPopulatorTest {
  @MockK private lateinit var context: RandomizationContext
  @MockK private lateinit var kRandom: KRandom
  private lateinit var parameters: KRandomParameters
  private lateinit var mapPopulator: MapPopulator

  @BeforeEach
  fun setUp() {
    parameters = KRandomParameters().collectionSizeRange(SIZE, SIZE)
    val objectFactory: ObjectFactory = ObjenesisObjectFactory()
    mapPopulator = MapPopulator(kRandom, objectFactory)
  }

  /*
   * Unit tests for MapPopulator class
   */
  @Test
  @Throws(Exception::class)
  fun `raw interface map types must be generated empty`() {
    every { context.parameters } returns parameters
    val field = Foo::class.java.getDeclaredField("rawMap")

    val randomMap = mapPopulator.getRandomMap(field, context)

    randomMap.shouldNotBeNull()
    randomMap.shouldBeEmpty()
  }

  @Test
  @Throws(Exception::class)
  fun `raw concrete map types must be generated empty`() {
    every { context.parameters } returns parameters
    val field = Foo::class.java.getDeclaredField("concreteMap")

    val randomMap = mapPopulator.getRandomMap(field, context)

    randomMap.shouldNotBeNull()
    randomMap.shouldBeEmpty()
  }

  @Test
  @Throws(Exception::class)
  fun `typed interface map types might be populated`() {
    every { context.parameters } returns parameters
    every { kRandom.doPopulateBean(String::class.java, context) } returnsMany listOf(FOO, BAR)
    val field = Foo::class.java.getDeclaredField("typedMap")

    val randomMap = mapPopulator.getRandomMap(field, context) as MutableMap<String, String>?

    randomMap.shouldNotBeNull()
    randomMap shouldContainExactly mapOf(FOO to BAR)
  }

  @Test
  @Throws(Exception::class)
  fun `typed concrete map types might be populated`() {
    every { context.parameters } returns parameters
    every { kRandom.doPopulateBean(String::class.java, context) } returnsMany listOf(FOO, BAR)
    val field = Foo::class.java.getDeclaredField("typedConcreteMap")

    val randomMap = mapPopulator.getRandomMap(field, context) as MutableMap<String, String>?

    randomMap.shouldNotBeNull()
    randomMap shouldContainExactly mapOf(FOO to BAR)
  }

  @Test
  @Throws(NoSuchFieldException::class)
  fun `not add null keys to map`() {
    every { context.parameters } returns parameters
    every { kRandom.doPopulateBean(String::class.java, context) } returns null
    val field = Foo::class.java.getDeclaredField("typedConcreteMap")

    val randomMap = mapPopulator.getRandomMap(field, context) as MutableMap<String, String>?

    randomMap.shouldNotBeNull()
    randomMap.shouldBeEmpty()
  }

  @Suppress("unused")
  internal class Foo(
    val rawMap: MutableMap<*, *>,
    val concreteMap: HashMap<*, *>,
    val typedMap: MutableMap<String, String>,
    val typedConcreteMap: HashMap<String, String>,
  )

  /*
   * Integration tests for map types population
   */
  @Test
  fun `raw map interfaces should be empty`() {
    val kRandom = KRandom()

    val mapBean = kRandom.nextObject(MapBean::class.java)

    mapBean.shouldNotBeNull()
    mapBean.map!!.shouldBeEmpty()
    mapBean.sortedMap!!.shouldBeEmpty()
    mapBean.navigableMap!!.shouldBeEmpty()
    mapBean.concurrentMap!!.shouldBeEmpty()
    mapBean.concurrentNavigableMap!!.shouldBeEmpty()
  }

  @Test
  fun `typed map interfaces should not be empty`() {
    val kRandom = KRandom()

    val mapBean = kRandom.nextObject(MapBean::class.java)

    mapBean.shouldNotBeNull()
    assertContainsNonZeroIntegers(mapBean.typedMap!!.keys)
    assertContainsOnlyNonEmptyPersons(mapBean.typedMap!!.values)
    assertContainsNonZeroIntegers(mapBean.typedSortedMap!!.keys)
    assertContainsOnlyNonEmptyPersons(mapBean.typedSortedMap!!.values)
    assertContainsNonZeroIntegers(mapBean.typedNavigableMap!!.keys)
    assertContainsOnlyNonEmptyPersons(mapBean.typedNavigableMap!!.values)
    assertContainsNonZeroIntegers(mapBean.typedConcurrentMap!!.keys)
    assertContainsOnlyNonEmptyPersons(mapBean.typedConcurrentMap!!.values)
    assertContainsNonZeroIntegers(mapBean.typedConcurrentNavigableMap!!.keys)
    assertContainsOnlyNonEmptyPersons(mapBean.typedConcurrentNavigableMap!!.values)
  }

  @Test
  fun `raw map classes should be empty`() {
    val kRandom = KRandom()

    val mapBean = kRandom.nextObject(MapBean::class.java)

    mapBean.shouldNotBeNull()
    mapBean.hashMap!!.shouldBeEmpty()
    mapBean.hashtable!!.shouldBeEmpty()
    mapBean.linkedHashMap!!.shouldBeEmpty()
    mapBean.weakHashMap!!.shouldBeEmpty()
    mapBean.identityHashMap!!.shouldBeEmpty()
    mapBean.treeMap!!.shouldBeEmpty()
    mapBean.concurrentSkipListMap!!.shouldBeEmpty()
  }

  @Test
  fun `typed map classes should not be empty`() {
    val kRandom = KRandom()

    val mapBean = kRandom.nextObject(MapBean::class.java)

    mapBean.shouldNotBeNull()
    assertContainsNonZeroIntegers(mapBean.typedHashMap!!.keys)
    assertContainsOnlyNonEmptyPersons(mapBean.typedHashMap!!.values)
    assertContainsNonZeroIntegers(mapBean.typedHashtable!!.keys)
    assertContainsOnlyNonEmptyPersons(mapBean.typedHashtable!!.values)
    assertContainsNonZeroIntegers(mapBean.typedLinkedHashMap!!.keys)
    assertContainsOnlyNonEmptyPersons(mapBean.typedLinkedHashMap!!.values)
    assertContainsNonZeroIntegers(mapBean.typedWeakHashMap!!.keys)
    assertContainsOnlyNonEmptyPersons(mapBean.typedWeakHashMap!!.values)
    assertContainsNonZeroIntegers(mapBean.typedIdentityHashMap!!.keys)
    assertContainsOnlyNonEmptyPersons(mapBean.typedIdentityHashMap!!.values)
    assertContainsNonZeroIntegers(mapBean.typedTreeMap!!.keys)
    assertContainsOnlyNonEmptyPersons(mapBean.typedTreeMap!!.values)
    assertContainsNonZeroIntegers(mapBean.typedConcurrentSkipListMap!!.keys)
    assertContainsOnlyNonEmptyPersons(mapBean.typedConcurrentSkipListMap!!.values)
  }

  @Test
  fun `wildcard typed map interfaces should be empty`() {
    val kRandom = KRandom()

    val wildCardMapBean = kRandom.nextObject(WildCardMapBean::class.java)

    wildCardMapBean.shouldNotBeNull()
    wildCardMapBean.boundedWildCardTypedMap!!.shouldBeEmpty()
    wildCardMapBean.unboundedWildCardTypedMap!!.shouldBeEmpty()
    wildCardMapBean.boundedWildCardTypedSortedMap!!.shouldBeEmpty()
    wildCardMapBean.unboundedWildCardTypedSortedMap!!.shouldBeEmpty()
    wildCardMapBean.boundedWildCardTypedNavigableMap!!.shouldBeEmpty()
    wildCardMapBean.unboundedWildCardTypedNavigableMap!!.shouldBeEmpty()
    wildCardMapBean.boundedWildCardTypedConcurrentMap!!.shouldBeEmpty()
    wildCardMapBean.unboundedWildCardTypedConcurrentMap!!.shouldBeEmpty()
    wildCardMapBean.boundedWildCardTypedConcurrentNavigableMap!!.shouldBeEmpty()
    wildCardMapBean.unboundedWildCardTypedConcurrentNavigableMap!!.shouldBeEmpty()
  }

  @Test
  fun `wildcard typed map classes should be empty`() {
    val kRandom = KRandom()

    val wildCardMapBean = kRandom.nextObject(WildCardMapBean::class.java)

    wildCardMapBean.shouldNotBeNull()
    wildCardMapBean.boundedWildCardTypedHashMap!!.shouldBeEmpty()
    wildCardMapBean.unboundedWildCardTypedHashMap!!.shouldBeEmpty()
    wildCardMapBean.boundedWildCardTypedHashMap!!.shouldBeEmpty()
    wildCardMapBean.unboundedWildCardTypedHashMap!!.shouldBeEmpty()
    wildCardMapBean.boundedWildCardTypedLinkedHashMap!!.shouldBeEmpty()
    wildCardMapBean.unboundedWildCardTypedHinkedHashMap!!.shouldBeEmpty()
    wildCardMapBean.boundedWildCardTypedWeakHashMap!!.shouldBeEmpty()
    wildCardMapBean.unboundedWildCardTypedWeakHashMap!!.shouldBeEmpty()
    wildCardMapBean.boundedWildCardTypedIdentityHashMap!!.shouldBeEmpty()
    wildCardMapBean.unboundedWildCardTypedIdentityHashMap!!.shouldBeEmpty()
    wildCardMapBean.boundedWildCardTypedTreeMap!!.shouldBeEmpty()
    wildCardMapBean.unboundedWildCardTypedTreeMap!!.shouldBeEmpty()
    wildCardMapBean.boundedWildCardTypedConcurrentSkipListMap!!.shouldBeEmpty()
    wildCardMapBean.unboundedWildCardTypedConcurrentSkipListMap!!.shouldBeEmpty()
  }

  @Test
  fun `composite map types should be empty`() {
    val kRandom = KRandom()

    val compositeMapBean = kRandom.nextObject(CompositeMapBean::class.java)

    compositeMapBean.shouldNotBeNull()
    compositeMapBean.personToNicknames.shouldBeEmpty()
    compositeMapBean.personToAccounts.shouldBeEmpty()
    compositeMapBean.reallyStrangeCompositeDataStructure.shouldBeEmpty()
  }

  @Test
  fun `user defined map type should be populated`() {
    val kRandom = KRandom()

    val customMap = kRandom.nextObject(CustomMap::class.java)

    customMap.shouldNotBeNull()
    customMap.name.shouldNotBeNull()
  }

  @Test
  fun `enum map type should be populated`() {
    val kRandom = KRandom()

    val enumMapBean = kRandom.nextObject(EnumMapBean::class.java)

    enumMapBean.shouldNotBeNull()
    enumMapBean.typedEnumMap.shouldNotBeNull()
    enumMapBean.untypedEnumMap.shouldBeNull()
  }

  private fun assertContainsOnlyNonEmptyPersons(persons: MutableCollection<Person>) {
    persons.forAll {
      it.address!!.city.shouldNotBeEmpty()
      it.address!!.zipCode.shouldNotBeEmpty()
      it.name.shouldNotBeEmpty()
    }
  }

  private fun assertContainsNonZeroIntegers(collection: MutableCollection<*>) {
    collection.forAll { it.shouldBeInstanceOf<Int>() }
  }

  companion object {
    private const val SIZE = 1
    private const val FOO = "foo"
    private const val BAR = "bar"
  }
}
