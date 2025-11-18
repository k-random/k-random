package io.github.krandom.validation

import io.github.krandom.KRandom
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.comparables.shouldBeLessThan
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.constraints.Negative
import java.math.BigDecimal
import java.math.BigInteger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NegativeAnnotationHandlerTest {
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun generatedBeanShouldBeValidAccordingToValidationConstraints() {
    val byteValue: Byte = 0.toByte()
    val shortValue: Short = 0.toShort()
    val intValue = 0
    val longValue = 0L
    val floatValue = 0f
    val doubleValue = 0.0

    val testBean = kRandom.nextObject(TestBean::class.java)

    testBean.testBigDecimal shouldBeLessThan BigDecimal.ZERO
    testBean.testBigInteger shouldBeLessThan BigInteger.ZERO
    testBean.testByte shouldBeLessThan byteValue
    testBean.testShort shouldBeLessThan shortValue
    testBean.testInt shouldBeLessThan intValue
    testBean.testLong shouldBeLessThan longValue
    testBean.testFloat shouldBeLessThan floatValue
    testBean.testDouble shouldBeLessThan doubleValue
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
    @field:Negative val testBigDecimal: BigDecimal,
    @field:Negative val testBigInteger: BigInteger,
    @field:Negative val testByte: Byte,
    @field:Negative val testShort: Short,
    @field:Negative val testInt: Int,
    @field:Negative val testLong: Long,
    @field:Negative val testFloat: Float,
    @field:Negative val testDouble: Double,
  )
}
