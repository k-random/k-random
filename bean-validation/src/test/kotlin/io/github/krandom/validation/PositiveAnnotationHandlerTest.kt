package io.github.krandom.validation

import io.github.krandom.KRandom
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.math.BigInteger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PositiveAnnotationHandlerTest {
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun generatedBeanShouldBeValidAccordingToValidationConstraints() {
    val byteValue: Byte = 0
    val shortValue: Short = 0
    val intValue = 0
    val longValue = 0L
    val floatValue = 0f
    val doubleValue = 0.0

    val testBean = kRandom.nextObject(TestBean::class.java)

    testBean.shouldNotBeNull()
    testBean.testBigDecimal shouldBeGreaterThan BigDecimal.ZERO
    testBean.testBigInteger shouldBeGreaterThan BigInteger.ZERO
    testBean.testByte shouldBeGreaterThan byteValue
    testBean.testShort shouldBeGreaterThan shortValue
    testBean.testInt shouldBeGreaterThan intValue
    testBean.testLong shouldBeGreaterThan longValue
    testBean.testFloat shouldBeGreaterThan floatValue
    testBean.testDouble shouldBeGreaterThan doubleValue
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

  internal data class TestBean(
    @field:Positive val testBigDecimal: BigDecimal,
    @field:Positive val testBigInteger: BigInteger,
    @field:Positive val testByte: Byte,
    @field:Positive val testShort: Short,
    @field:Positive val testInt: Int,
    @field:Positive val testLong: Long,
    @field:Positive val testFloat: Float,
    @field:Positive val testDouble: Double,
  )
}
