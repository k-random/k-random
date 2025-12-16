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

import io.github.krandom.FieldPredicates.isAnnotatedWith
import io.github.krandom.FieldPredicates.named
import io.github.krandom.api.ContextAwareRandomizer
import io.github.krandom.api.RandomizerContext
import io.github.krandom.beans.Person
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class RandomizationContextTest {
  @MockK private lateinit var bean1: Any
  @MockK private lateinit var bean2: Any
  @MockK private lateinit var parameters: KRandomParameters

  private lateinit var randomizationContext: RandomizationContext

  @BeforeEach
  fun setUp() {
    every { parameters.seed } returns 123L
    randomizationContext = RandomizationContext(Any::class.java, parameters)
  }

  @Test
  fun `when a type has been randomized then has populated bean should return true when the object pool is filled`() {
    every { parameters.getObjectPoolSize() } returns KRandomParameters.DEFAULT_OBJECT_POOL_SIZE

    // Only one instance has been randomized => should be considered as not randomized yet
    randomizationContext.addPopulatedBean(String::class.java, "bean0")
    randomizationContext.hasAlreadyRandomizedType(String::class.java).shouldBeFalse()

    // When the object pool size is filled => should be considered as already randomized
    for (i in 1..<KRandomParameters.DEFAULT_OBJECT_POOL_SIZE) {
      randomizationContext.addPopulatedBean(String::class.java, "bean$i")
    }
    randomizationContext.hasAlreadyRandomizedType(String::class.java).shouldBeTrue()
  }

  @Test
  fun `when a type has not been randomized yet then has populated bean should return false`() {
    every { parameters.getObjectPoolSize() } returns KRandomParameters.DEFAULT_OBJECT_POOL_SIZE
    randomizationContext.addPopulatedBean(String::class.java, bean1)

    val hasPopulatedBean = randomizationContext.hasAlreadyRandomizedType(Int::class.java)

    hasPopulatedBean.shouldBeFalse()
  }

  @Test
  fun `when a type has been randomized then the randomized bean should be retrieved from the object pool`() {
    every { parameters.getObjectPoolSize() } returns KRandomParameters.DEFAULT_OBJECT_POOL_SIZE
    randomizationContext.addPopulatedBean(String::class.java, bean1)
    randomizationContext.addPopulatedBean(String::class.java, bean2)

    val populatedBean = randomizationContext.getPopulatedBean(String::class.java)

    populatedBean shouldBeIn setOf(bean1, bean2)
  }

  @Test
  @Throws(NoSuchFieldException::class)
  fun `when current stack size over max randomization depth then should exceed randomization depth`() {
    every { parameters.getRandomizationDepth() } returns 1
    val customRandomizationContext = RandomizationContext(Any::class.java, parameters)
    val address = Person::class.java.getDeclaredField("address")
    customRandomizationContext.pushStackItem(RandomizationContextStackItem(bean1, address))
    customRandomizationContext.pushStackItem(RandomizationContextStackItem(bean2, address))

    val hasExceededRandomizationDepth = customRandomizationContext.hasExceededRandomizationDepth()

    hasExceededRandomizationDepth.shouldBeTrue()
  }

  @Test
  @Throws(NoSuchFieldException::class)
  fun `when current stack size less max randomization depth then should not exceed randomization depth`() {
    every { parameters.getRandomizationDepth() } returns 2
    val customRandomizationContext = RandomizationContext(Any::class.java, parameters)
    val address = Person::class.java.getDeclaredField("address")
    customRandomizationContext.pushStackItem(RandomizationContextStackItem(bean1, address))

    val hasExceededRandomizationDepth = customRandomizationContext.hasExceededRandomizationDepth()

    hasExceededRandomizationDepth.shouldBeFalse()
  }

  @Test
  @Throws(NoSuchFieldException::class)
  fun `when current stack size equal max randomization depth then should not exceed randomization depth`() {
    every { parameters.getRandomizationDepth() } returns 2
    val customRandomizationContext = RandomizationContext(Any::class.java, parameters)
    val address = Person::class.java.getDeclaredField("address")
    customRandomizationContext.pushStackItem(RandomizationContextStackItem(bean1, address))
    customRandomizationContext.pushStackItem(RandomizationContextStackItem(bean2, address))

    val hasExceededRandomizationDepth = customRandomizationContext.hasExceededRandomizationDepth()

    hasExceededRandomizationDepth.shouldBeFalse()
  }

  @Test
  fun `test randomizer context`() {
    val randomizer = MyRandomizer()
    val parameters: KRandomParameters =
      KRandomParameters()
        .randomize(D::class.java, randomizer)
        .randomize(isAnnotatedWith(ExampleAnnotation::class.java), ERandomizer())
        .excludeField(named("excluded"))
    val kRandom = KRandom(parameters)

    val a = kRandom.nextObject(A::class.java)

    a.shouldNotBeNull()
    a.excluded.shouldBeNull()
    a.b.shouldNotBeNull()
    a.b.c.shouldNotBeNull()
    a.b.e.shouldNotBeNull()
    a.b.c.d.shouldNotBeNull()
    a.b.c.d.name.shouldNotBeNull()
    a.b.e.name shouldBe "bar"
  }

  internal class MyRandomizer : ContextAwareRandomizer<D> {
    private lateinit var context: RandomizerContext

    override fun setRandomizerContext(context: RandomizerContext) {
      this.context = context
    }

    override fun getRandomValue(): D {
      // At this level, the context should be as follows:
      context.getCurrentField() shouldBe "b.c.d"
      context.getCurrentRandomizationDepth() shouldBe 3
      context.targetType shouldBe A::class.java
      context.rootObject.shouldBeInstanceOf<A>()
      context.getCurrentObject().shouldBeInstanceOf<C>()

      val d = D("foo")
      return d
    }
  }

  internal class ERandomizer : ContextAwareRandomizer<E?> {
    private lateinit var context: RandomizerContext

    override fun setRandomizerContext(context: RandomizerContext) {
      this.context = context
    }

    override fun getRandomValue(): E {
      // At this level, the context should be as follows:
      context.getCurrentField() shouldBe "b.e"
      context.getCurrentRandomizationDepth() shouldBe 2
      context.targetType shouldBe A::class.java
      context.rootObject.shouldBeInstanceOf<A>()
      context.getCurrentObject().shouldBeInstanceOf<B>()

      val e = E("")
      val currentField = context.getCurrentField()
      val currentObject = context.getCurrentObject()
      try {
        val substring = currentField.substring(currentField.lastIndexOf(".") + 1)
        e.name =
          currentObject.javaClass
            .getDeclaredField(substring)
            .getAnnotation(ExampleAnnotation::class.java)
            .value
      } catch (_: NoSuchFieldException) {
        e.name = "default"
      }
      return e
    }
  }

  internal data class A(val b: B, val excluded: String?)

  internal data class B(val c: C, @field:ExampleAnnotation("bar") val e: E)

  internal data class C(val d: D)

  internal data class D(var name: String)

  internal data class E(var name: String)

  @Target(AnnotationTarget.FIELD)
  @Retention(AnnotationRetention.RUNTIME)
  @MustBeDocumented
  internal annotation class ExampleAnnotation(val value: String)
}
