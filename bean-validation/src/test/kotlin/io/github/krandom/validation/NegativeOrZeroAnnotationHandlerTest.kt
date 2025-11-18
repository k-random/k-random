package io.github.krandom.validation

import io.github.krandom.KRandom
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.constraints.NegativeOrZero
import java.math.BigDecimal
import java.math.BigInteger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NegativeOrZeroAnnotationHandlerTest {
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

    testBean.testBigDecimal shouldBeLessThanOrEqualTo BigDecimal.ZERO
    testBean.testBigInteger shouldBeLessThanOrEqualTo BigInteger.ZERO
    testBean.testPrimitiveByte shouldBeLessThanOrEqualTo byteValue
    testBean.testWrapperByte shouldBeLessThanOrEqualTo byteValue
    testBean.testPrimitiveShort shouldBeLessThanOrEqualTo shortValue
    testBean.testWrapperShort shouldBeLessThanOrEqualTo shortValue
    testBean.testPrimitiveInt shouldBeLessThanOrEqualTo intValue
    testBean.testWrapperInteger shouldBeLessThanOrEqualTo intValue
    testBean.testPrimitiveLong shouldBeLessThanOrEqualTo longValue
    testBean.testWrapperLong shouldBeLessThanOrEqualTo longValue
    testBean.testPrimitiveFloat shouldBeLessThanOrEqualTo floatValue
    testBean.testWrapperFloat shouldBeLessThanOrEqualTo floatValue
    testBean.testPrimitiveDouble shouldBeLessThanOrEqualTo doubleValue
    testBean.testWrapperDouble shouldBeLessThanOrEqualTo doubleValue
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
    @field:NegativeOrZero val testBigDecimal: BigDecimal,
    @field:NegativeOrZero val testBigInteger: BigInteger,
    @field:NegativeOrZero val testPrimitiveByte: Byte,
    @field:NegativeOrZero val testWrapperByte: Byte,
    @field:NegativeOrZero val testPrimitiveShort: Short,
    @field:NegativeOrZero val testWrapperShort: Short,
    @field:NegativeOrZero val testPrimitiveInt: Int,
    @field:NegativeOrZero val testWrapperInteger: Int,
    @field:NegativeOrZero val testPrimitiveLong: Long,
    @field:NegativeOrZero val testWrapperLong: Long,
    @field:NegativeOrZero val testPrimitiveFloat: Float,
    @field:NegativeOrZero val testWrapperFloat: Float,
    @field:NegativeOrZero val testPrimitiveDouble: Double,
    @field:NegativeOrZero val testWrapperDouble: Double,
  )
}
