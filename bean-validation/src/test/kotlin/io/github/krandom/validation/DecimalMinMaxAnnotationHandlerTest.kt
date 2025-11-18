package io.github.krandom.validation

import io.github.krandom.KRandom
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import java.math.BigDecimal
import java.math.BigInteger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DecimalMinMaxAnnotationHandlerTest {
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun `generated bean should be valid according to validation constraints`() {
    val bigDecimal = BigDecimal("10.0")
    val byte = 10.toByte()
    val short = 10.toShort()

    val testBean = kRandom.nextObject(TestBean::class.java)

    testBean.testMinBigDecimal shouldBeGreaterThanOrEqualTo bigDecimal
    testBean.testMaxBigDecimal shouldBeLessThanOrEqualTo bigDecimal
    testBean.testMinBigInteger shouldBeGreaterThanOrEqualTo BigInteger.TEN
    testBean.testMaxBigInteger shouldBeLessThanOrEqualTo BigInteger.TEN
    BigDecimal(testBean.testMinString) shouldBeGreaterThanOrEqualTo bigDecimal
    BigDecimal(testBean.testMaxString) shouldBeLessThanOrEqualTo bigDecimal
    testBean.testMinPrimitiveByte shouldBeGreaterThanOrEqualTo byte
    testBean.testMaxPrimitiveByte shouldBeLessThanOrEqualTo byte
    testBean.testMinWrapperByte shouldBeGreaterThanOrEqualTo byte
    testBean.testMaxWrapperByte shouldBeLessThanOrEqualTo byte
    testBean.testMinPrimitiveShort shouldBeGreaterThanOrEqualTo short
    testBean.testMaxPrimitiveShort shouldBeLessThanOrEqualTo short
    testBean.testMinWrapperShort shouldBeGreaterThanOrEqualTo short
    testBean.testMaxWrapperShort shouldBeLessThanOrEqualTo short
    testBean.testMinPrimitiveInt shouldBeGreaterThanOrEqualTo 10
    testBean.testMaxPrimitiveInt shouldBeLessThanOrEqualTo 10
    testBean.testMinWrapperInt shouldBeGreaterThanOrEqualTo 10
    testBean.testMaxWrapperInt shouldBeLessThanOrEqualTo 10
    testBean.testMinPrimitiveLong shouldBeGreaterThanOrEqualTo 10L
    testBean.testMaxPrimitiveLong shouldBeLessThanOrEqualTo 10L
    testBean.testMinWrapperLong shouldBeGreaterThanOrEqualTo 10L
    testBean.testMaxWrapperLong shouldBeLessThanOrEqualTo 10L
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
    @field:DecimalMin("10.0") val testMinBigDecimal: BigDecimal,
    @field:DecimalMax("10.0") val testMaxBigDecimal: BigDecimal,
    @field:DecimalMin("10.0") val testMinBigInteger: BigInteger,
    @field:DecimalMax("10.0") val testMaxBigInteger: BigInteger,
    @field:DecimalMin("10.0") val testMinString: String,
    @field:DecimalMax("10.0") val testMaxString: String,
    @field:DecimalMin("10.0") val testMinPrimitiveByte: Byte,
    @field:DecimalMax("10.0") val testMaxPrimitiveByte: Byte,
    @field:DecimalMin("10.0") val testMinWrapperByte: Byte,
    @field:DecimalMax("10.0") val testMaxWrapperByte: Byte,
    @field:DecimalMin("10.0") val testMinPrimitiveShort: Short,
    @field:DecimalMax("10.0") val testMaxPrimitiveShort: Short,
    @field:DecimalMin("10.0") val testMinWrapperShort: Short,
    @field:DecimalMax("10.0") val testMaxWrapperShort: Short,
    @field:DecimalMin("10.0") val testMinPrimitiveInt: Int,
    @field:DecimalMax("10.0") val testMaxPrimitiveInt: Int,
    @field:DecimalMin("10.0") val testMinWrapperInt: Int,
    @field:DecimalMax("10.0") val testMaxWrapperInt: Int,
    @field:DecimalMin("10.0") val testMinPrimitiveLong: Long,
    @field:DecimalMax("10.0") val testMaxPrimitiveLong: Long,
    @field:DecimalMin("10.0") val testMinWrapperLong: Long,
    @field:DecimalMax("10.0") val testMaxWrapperLong: Long,
  )
}
