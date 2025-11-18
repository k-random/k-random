package io.github.krandom.validation

import io.github.krandom.KRandom
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.string.shouldMatch
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.constraints.Pattern
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val REGEX: String = """^[A-Z]{1,2}[0-9][0-9A-Z]? ?[0-9][A-Z]{2}$"""

internal class PatternAnnotationHandlerTest {
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun generatedBeanShouldBeValidAccordingToValidationConstraints() {
    val testBean = kRandom.nextObject(TestBean::class.java)

    testBean.testString shouldMatch REGEX
  }

  @Test
  fun generatedBeanShouldBeValidUsingBeanValidationApi() {
    val testBean = kRandom.nextObject(TestBean::class.java)

    val validator: Validator
    Validation.buildDefaultValidatorFactory().use { validatorFactory ->
      validator = validatorFactory.validator
    }
    val violations = validator.validate(testBean)

    violations.shouldBeEmpty()
  }

  internal data class TestBean(@field:Pattern(regexp = REGEX) val testString: String)
}
