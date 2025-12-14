package io.github.krandom.validation

import io.github.krandom.KRandom
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.constraints.AssertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AssertTrueAnnotationHandlerTest {
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun `generated bean should be valid according to validation constraints`() {
    // given
    // when
    val testBean = kRandom.nextObject(TestBean::class.java)

    // then
    testBean.shouldNotBeNull()
    testBean.isTestBooleanFieldAnnotation.shouldBeTrue()
    testBean.testBooleanMethodAnnotation.shouldBeTrue()
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
    @field:AssertTrue val isTestBooleanFieldAnnotation: Boolean,
    @get:AssertTrue val testBooleanMethodAnnotation: Boolean,
  )
}
