package io.github.krandom.validation

import io.github.krandom.KRandom
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.constraints.Pattern
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

const val UK_POST_CODE_REGEX = """^[A-Z]{1,2}\d[A-Z\d]? ?\d[A-Z]{2}$"""
const val YEAR_REGEX = """\d{4}"""

internal class PatternAnnotationHandlerTest {
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun generatedBeanShouldBeValidAccordingToValidationConstraints() {
    val testBean = kRandom.nextObject(TestBean::class.java)

    testBean.postCode shouldMatch UK_POST_CODE_REGEX.toRegex()
    testBean.year shouldMatch YEAR_REGEX.toRegex()
  }

  @Test
  fun generatedBeanShouldBeValidUsingBeanValidationApi() {
    val testBean = kRandom.nextObject(TestBean::class.java)

    val validator: Validator
    Validation.buildDefaultValidatorFactory().use { validatorFactory ->
      validator = validatorFactory.validator
    }
    val violations = validator.validate(testBean)

    violations.size shouldBe 0
  }

  data class TestBean(
    @field:Pattern(regexp = UK_POST_CODE_REGEX) val postCode: String,
    @field:Pattern(regexp = YEAR_REGEX) val year: String,
  )
}
