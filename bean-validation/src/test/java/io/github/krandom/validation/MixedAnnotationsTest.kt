package io.github.krandom.validation

import io.github.krandom.KRandom
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MixedAnnotationsTest {
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun `generated bean should be valid according to validation constraints`() {
    val testBean = kRandom.nextObject(TestBean::class.java)

    testBean.year.length shouldBe 4
    testBean.year shouldMatch """\d{4}"""
  }

  data class TestBean(
    @field:Pattern(regexp = """\d{4}""") @field:Size(min = 4, max = 4) val year: String
  )
}
