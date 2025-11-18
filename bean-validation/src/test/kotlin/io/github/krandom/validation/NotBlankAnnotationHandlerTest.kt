package io.github.krandom.validation

import io.github.krandom.KRandom
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.string.shouldNotBeBlank
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.constraints.NotBlank
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NotBlankAnnotationHandlerTest {
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun `generated bean should be valid according to validation constraints`() {
    val testBean = kRandom.nextObject(TestBean::class.java)

    testBean.testString.shouldNotBeBlank()
  }

  @Test
  fun `generated bean should be valid using bean validation api`() {
    val testBean = kRandom.nextObject(TestBean::class.java)

    val validator: Validator
    Validation.buildDefaultValidatorFactory().use { validatorFactory ->
      validator = validatorFactory.validator
    }
    val violations = validator.validate(testBean)

    violations.shouldBeEmpty()
  }

  internal data class TestBean(@field:NotBlank val testString: String)
}
