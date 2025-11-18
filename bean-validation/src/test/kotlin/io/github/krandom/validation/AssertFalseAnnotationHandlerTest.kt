package io.github.krandom.validation

import io.github.krandom.KRandom
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldBeEmpty
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.constraints.AssertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AssertFalseAnnotationHandlerTest {
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun `generated bean should be valid according to validation constraints`() {
    val testBean = kRandom.nextObject(TestBean::class.java)

    testBean.testBooleanFieldAnnotation.shouldBeFalse()
    testBean.testBooleanMethodAnnotation.shouldBeFalse()
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

  internal data class TestBean(
    @field:AssertFalse val testBooleanFieldAnnotation: Boolean,
    @get:AssertFalse val testBooleanMethodAnnotation: Boolean,
  )
}
