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

import io.github.krandom.beans.CollectionBean
import io.github.krandom.beans.CompositeCollectionBean
import io.github.krandom.beans.CustomList
import io.github.krandom.beans.DelayedQueueBean
import io.github.krandom.beans.Person
import io.github.krandom.beans.SynchronousQueueBean
import io.github.krandom.beans.TypeVariableCollectionBean
import io.github.krandom.beans.WildCardCollectionBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeEmpty
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlin.random.Random
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
@Suppress("UNCHECKED_CAST")
internal class CollectionPopulatorTest {
  @MockK private lateinit var context: RandomizationContext
  @MockK private lateinit var kRandom: KRandom
  private lateinit var parameters: KRandomParameters

  private lateinit var collectionPopulator: CollectionPopulator

  @BeforeEach
  fun setUp() {
    parameters = KRandomParameters().collectionSizeRange(SIZE, SIZE)
    collectionPopulator = CollectionPopulator(kRandom)
    every { kRandom.nextLong() } returns Random.nextLong()
  }

  /*
   * Unit tests for CollectionPopulator class
   */
  @Test
  @Throws(Exception::class)
  fun `raw interface collection types must be returned empty`() {
    every { context.parameters } returns parameters
    val field = Foo::class.java.getDeclaredField("rawInterfaceList")

    val collection = collectionPopulator.getRandomCollection(field, context)

    collection.shouldBeEmpty()
  }

  @Test
  @Throws(Exception::class)
  fun `raw concrete collection types must be returned empty`() {
    every { context.parameters } returns parameters
    val field = Foo::class.java.getDeclaredField("rawConcreteList")

    val collection = collectionPopulator.getRandomCollection(field, context)

    collection.shouldBeEmpty()
  }

  @Test
  @Throws(Exception::class)
  fun `typed interface collection types might be populated`() {
    every { context.parameters } returns parameters
    every { kRandom.doPopulateBean(String::class.java, context) } returns STRING
    val field = Foo::class.java.getDeclaredField("typedInterfaceList")

    val collection =
      collectionPopulator.getRandomCollection(field, context) as MutableCollection<String>

    collection shouldContainExactly listOf(STRING, STRING)
  }

  @Test
  @Throws(Exception::class)
  fun `typed concrete collection types might be populated`() {
    every { context.parameters } returns parameters
    every { kRandom.doPopulateBean(String::class.java, context) } returns STRING
    val field = Foo::class.java.getDeclaredField("typedConcreteList")

    val collection =
      collectionPopulator.getRandomCollection(field, context) as MutableCollection<String>

    collection shouldContainExactly listOf(STRING, STRING)
  }

  @Suppress("unused")
  internal data class Foo(
    val rawInterfaceList: MutableList<*>,
    val typedInterfaceList: MutableList<String>,
    val rawConcreteList: ArrayList<*>,
    val typedConcreteList: ArrayList<String>,
  )

  /*
   * Integration tests for Collection types population
   */
  @Test
  fun `raw collection interfaces should be empty`() {
    val kRandom = KRandom()

    val collectionsBean = kRandom.nextObject(CollectionBean::class.java)

    collectionsBean.shouldNotBeNull()
    collectionsBean.collection.shouldBeEmpty()
    collectionsBean.set.shouldBeEmpty()
    collectionsBean.sortedSet.shouldBeEmpty()
    collectionsBean.navigableSet.shouldBeEmpty()
    collectionsBean.list.shouldBeEmpty()
    collectionsBean.queue.shouldBeEmpty()
    collectionsBean.blockingQueue.shouldBeEmpty()
    collectionsBean.transferQueue.shouldBeEmpty()
    collectionsBean.deque.shouldBeEmpty()
    collectionsBean.blockingDeque.shouldBeEmpty()
  }

  @Test
  fun `unbounded wild card typed collection interfaces should be empty`() {
    val kRandom = KRandom()

    val collectionsBean = kRandom.nextObject(WildCardCollectionBean::class.java)

    collectionsBean.shouldNotBeNull()
    collectionsBean.unboundedWildCardTypedCollection.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedSet.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedSortedSet.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedNavigableSet.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedList.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedQueue.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedBlockingQueue.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedTransferQueue.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedDeque.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedBlockingDeque.shouldBeEmpty()
  }

  @Test
  fun `bounded wild card typed collection interfaces should be empty`() {
    val kRandom = KRandom()

    val collectionsBean = kRandom.nextObject(WildCardCollectionBean::class.java)

    collectionsBean.shouldNotBeNull()
    collectionsBean.boundedWildCardTypedCollection.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedSet.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedSortedSet.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedNavigableSet.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedList.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedQueue.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedBlockingQueue.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedTransferQueue.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedDeque.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedBlockingDeque.shouldBeEmpty()
  }

  @Test
  fun `typed collection interfaces should not be empty`() {
    val kRandom = KRandom()

    val collectionsBean = kRandom.nextObject(CollectionBean::class.java)

    collectionsBean.shouldNotBeNull()
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedCollection)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedSet)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedSortedSet)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedNavigableSet)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedList)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedQueue)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedBlockingQueue)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedTransferQueue)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedDeque)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedBlockingDeque)
  }

  @Test
  fun `raw collection classes should be empty`() {
    val kRandom = KRandom()

    val collectionsBean = kRandom.nextObject(CollectionBean::class.java)

    collectionsBean.shouldNotBeNull()
    collectionsBean.arrayList.shouldBeEmpty()
    collectionsBean.linkedList.shouldBeEmpty()
    collectionsBean.vector.shouldBeEmpty()
    collectionsBean.stack.shouldBeEmpty()
    collectionsBean.hashSet.shouldBeEmpty()
    collectionsBean.linkedHashSet.shouldBeEmpty()
    collectionsBean.treeSet.shouldBeEmpty()
    collectionsBean.concurrentSkipListSet.shouldBeEmpty()
    collectionsBean.arrayBlockingQueue.shouldBeEmpty()
    collectionsBean.linkedBlockingQueue.shouldBeEmpty()
    collectionsBean.concurrentLinkedQueue.shouldBeEmpty()
    collectionsBean.linkedTransferQueue.shouldBeEmpty()
    collectionsBean.priorityQueue.shouldBeEmpty()
    collectionsBean.priorityBlockingQueue.shouldBeEmpty()
    collectionsBean.arrayDeque.shouldBeEmpty()
    collectionsBean.linkedBlockingDeque.shouldBeEmpty()
    collectionsBean.concurrentLinkedDeque.shouldBeEmpty()
  }

  @Test
  fun `unbounded wild card typed collection classes should be empty`() {
    val kRandom = KRandom()

    val collectionsBean = kRandom.nextObject(WildCardCollectionBean::class.java)

    collectionsBean.shouldNotBeNull()
    collectionsBean.unboundedWildCardTypedArrayList.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedLinkedList.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedVector.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedStack.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedHashSet.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedLinkedHashSet.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedTreeSet.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedConcurrentSkipListSet.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedArrayBlockingQueue.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedLinkedBlockingQueue.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedConcurrentLinkedQueue.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedLinkedTransferQueue.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedPriorityQueue.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedPriorityBlockingQueue.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedArrayDeque.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedLinkedBlockingDeque.shouldBeEmpty()
    collectionsBean.unboundedWildCardTypedConcurrentLinkedDeque.shouldBeEmpty()
  }

  @Test
  fun `bounded wild card typed collection classes should be empty`() {
    val kRandom = KRandom()

    val collectionsBean = kRandom.nextObject(WildCardCollectionBean::class.java)

    collectionsBean.shouldNotBeNull()
    collectionsBean.boundedWildCardTypedArrayList.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedLinkedList.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedVector.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedStack.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedHashSet.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedLinkedHashSet.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedTreeSet.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedConcurrentSkipListSet.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedArrayBlockingQueue.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedLinkedBlockingQueue.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedConcurrentLinkedQueue.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedLinkedTransferQueue.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedPriorityQueue.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedPriorityBlockingQueue.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedArrayDeque.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedLinkedBlockingDeque.shouldBeEmpty()
    collectionsBean.boundedWildCardTypedConcurrentLinkedDeque.shouldBeEmpty()
  }

  @Test
  fun `typed collection classes should no be empty`() {
    val kRandom = KRandom()

    val collectionsBean = kRandom.nextObject(CollectionBean::class.java)

    collectionsBean.shouldNotBeNull()
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedArrayList)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedLinkedList)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedVector)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedStack)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedHashSet)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedLinkedHashSet)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedTreeSet)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedConcurrentSkipListSet)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedArrayBlockingQueue)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedLinkedBlockingQueue)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedConcurrentLinkedQueue)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedLinkedTransferQueue)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedPriorityQueue)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedPriorityBlockingQueue)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedArrayDeque)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedLinkedBlockingDeque)
    assertContainsOnlyNonEmptyPersons(collectionsBean.typedConcurrentLinkedDeque)
  }

  @Test
  fun `composite collection types should be empty`() {
    val kRandom = KRandom()

    val compositeCollectionBean = kRandom.nextObject(CompositeCollectionBean::class.java)

    compositeCollectionBean.shouldNotBeNull()
    compositeCollectionBean.listOfLists.shouldBeEmpty()
    compositeCollectionBean.typedListOfLists.shouldBeEmpty()
    compositeCollectionBean.setOfSets.shouldBeEmpty()
    compositeCollectionBean.typedSetOfSets.shouldBeEmpty()
    compositeCollectionBean.queueOfQueues.shouldBeEmpty()
    compositeCollectionBean.typedQueueOdQueues.shouldBeEmpty()
  }

  @Test
  fun `synchronous queue type must be rejected`() {
    val kRandom = KRandom()

    shouldThrow<ObjectCreationException> { kRandom.nextObject(SynchronousQueueBean::class.java) }
  }

  @Test
  fun `delayed queue type must be rejected`() {
    val kRandom = KRandom()

    shouldThrow<ObjectCreationException> { kRandom.nextObject(DelayedQueueBean::class.java) }
  }

  @Test
  fun `raw interface collection types must be generated empty`() {
    val parameters = KRandomParameters().scanClasspathForConcreteTypes(true)
    kRandom = KRandom(parameters)

    val list = kRandom.nextObject(MutableList::class.java)

    list.shouldNotBeNull()
    list.shouldBeEmpty()
  }

  @Test
  fun `raw concrete collection types must be generated empty`() {
    val parameters = KRandomParameters().scanClasspathForConcreteTypes(true)
    kRandom = KRandom(parameters)

    val list = kRandom.nextObject(ArrayList::class.java)

    list.shouldNotBeNull()
    list.shouldBeEmpty()
  }

  @Test
  fun `raw interface map types must be generated empty`() {
    val parameters = KRandomParameters().scanClasspathForConcreteTypes(true)
    kRandom = KRandom(parameters)

    val map = kRandom.nextObject(MutableMap::class.java)

    map.shouldNotBeNull()
    map.shouldBeEmpty()
  }

  @Test
  fun `raw concrete map types must be generated empty`() {
    val parameters = KRandomParameters().scanClasspathForConcreteTypes(true)
    kRandom = KRandom(parameters)

    val map = kRandom.nextObject(HashMap::class.java)

    map.shouldNotBeNull()
    map.shouldBeEmpty()
  }

  @Test
  fun `user defined collection type should be populated`() {
    val kRandom = KRandom()

    val customList = kRandom.nextObject(CustomList::class.java)

    customList.shouldNotBeNull()
    customList.name.shouldNotBeNull()
  }

  @Test
  fun `type variable collection types must be generated empty`() {
    val kRandom = KRandom()

    val bean =
      kRandom.nextObject(TypeVariableCollectionBean::class.java)
        as TypeVariableCollectionBean<String, String>?

    bean.shouldNotBeNull()
    bean.collection.shouldBeEmpty()
    bean.list.shouldBeEmpty()
    bean.set.shouldBeEmpty()
    bean.map.shouldBeEmpty()
  }

  companion object {
    private const val SIZE = 2
    private const val STRING = "foo"

    @JvmStatic
    private fun assertContainsOnlyNonEmptyPersons(persons: MutableCollection<Person?>) {
      persons.forAll { person ->
        person.shouldNotBeNull()
        person.address!!.city.shouldNotBeEmpty()
        person.address!!.zipCode.shouldNotBeEmpty()
        person.name.shouldNotBeEmpty()
      }
    }
  }
}
