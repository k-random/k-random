package io.github.krandom.validation

import io.github.krandom.KRandom
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import io.kotest.matchers.nulls.shouldNotBeNull
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import java.math.BigDecimal
import java.math.BigInteger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MinMaxAnnotationHandlerTest {
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun generatedBeanShouldBeValidAccordingToValidationConstraints() {
    val shortValue: Short = 10
    val intValue = 10
    val longValue = 10L

    val testBean = kRandom.nextObject(TestBean::class.java)

    testBean.shouldNotBeNull()
    testBean.testMinBigDecimal shouldBeGreaterThanOrEqualTo BigDecimal.TEN
    testBean.testMaxBigDecimal shouldBeLessThanOrEqualTo BigDecimal.TEN
    testBean.testMinBigInteger shouldBeGreaterThanOrEqualTo BigInteger.TEN
    testBean.testMaxBigInteger shouldBeLessThanOrEqualTo BigInteger.TEN
    testBean.testMinShort shouldBeGreaterThanOrEqualTo shortValue
    testBean.testMaxShort shouldBeLessThanOrEqualTo shortValue
    testBean.testMinInt shouldBeGreaterThanOrEqualTo intValue
    testBean.testMaxInt shouldBeLessThanOrEqualTo intValue
    testBean.testMinLong shouldBeGreaterThanOrEqualTo longValue
    testBean.testMaxLong shouldBeLessThanOrEqualTo longValue
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
    @field:Min(10L) val testMinBigDecimal: BigDecimal,
    @field:Max(10L) val testMaxBigDecimal: BigDecimal,
    @field:Min(10L) val testMinBigInteger: BigInteger,
    @field:Max(10L) val testMaxBigInteger: BigInteger,
    @field:Min(10L) val testMinShort: Short,
    @field:Max(10L) val testMaxShort: Short,
    @field:Min(10L) val testMinInt: Int,
    @field:Max(10L) val testMaxInt: Int,
    @field:Min(10L) val testMinLong: Long,
    @field:Max(10L) val testMaxLong: Long,
  )
}
