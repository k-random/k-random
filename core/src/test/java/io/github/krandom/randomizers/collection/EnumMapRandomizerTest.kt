package io.github.krandom.randomizers.collection

import io.github.krandom.randomizers.misc.EnumRandomizer
import io.github.krandom.randomizers.text.StringRandomizer
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
class EnumMapRandomizerTest {
  @MockK private lateinit var keyRandomizer: EnumRandomizer<TestEnum>
  @MockK private lateinit var valueRandomizer: StringRandomizer

  @Test
  fun `generated map should not be empty`() {
    every { keyRandomizer.getRandomValue() } answers { TestEnum.entries.random() }
    every { valueRandomizer.getRandomValue() } returns "k-random"
    val underTest = EnumMapRandomizer(keyRandomizer, valueRandomizer)

    val result = underTest.getRandomValue()

    result.shouldNotBeEmpty()
  }

  @Test
  fun `can generate empty map`() {
    every { keyRandomizer.getRandomValue() } answers { TestEnum.entries.random() }
    val underTest = EnumMapRandomizer(keyRandomizer, valueRandomizer, 0)

    val result = underTest.getRandomValue()

    result.shouldBeEmpty()
  }

  @Test
  fun `generated map size should be equal to the specified size`() {
    every { keyRandomizer.getRandomValue() } returnsMany listOf(TestEnum.A, TestEnum.B, TestEnum.C)
    every { valueRandomizer.getRandomValue() } returnsMany listOf("a", "b", "c")
    val underTest = EnumMapRandomizer(keyRandomizer, valueRandomizer, 2)

    underTest.getRandomValue() shouldHaveSize 2
  }

  @Test
  fun `randomizer should throw exception for invalid size`() {
    shouldThrow<IllegalArgumentException> { EnumMapRandomizer(keyRandomizer, valueRandomizer, -1) }
  }

  enum class TestEnum {
    A,
    B,
    C,
  }
}
