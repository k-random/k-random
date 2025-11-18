package io.github.krandom.validation

import io.github.krandom.KRandom
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal
import java.math.BigInteger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PositiveOrZeroAnnotationHandlerTest {
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun `generated bean should be valid according to validation constraints`() {
    val byteValue: Byte = 0
    val shortValue: Short = 0
    val intValue = 0
    val longValue = 0L
    val floatValue = 0F

    val testBean = kRandom.nextObject(TestBean::class.java)

    testBean.testBigDecimal shouldBeGreaterThanOrEqualTo BigDecimal.ZERO
    testBean.testBigInteger shouldBeGreaterThanOrEqualTo BigInteger.ZERO
    testBean.testByte shouldBeGreaterThanOrEqualTo byteValue
    testBean.testShort shouldBeGreaterThanOrEqualTo shortValue
    testBean.testInt shouldBeGreaterThanOrEqualTo intValue
    testBean.testLong shouldBeGreaterThanOrEqualTo longValue
    testBean.testFloat shouldBeGreaterThanOrEqualTo floatValue
    testBean.testDouble shouldBeGreaterThanOrEqualTo 0.0
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
    @field:PositiveOrZero val testBigDecimal: BigDecimal,
    @field:PositiveOrZero val testBigInteger: BigInteger,
    @field:PositiveOrZero val testByte: Byte,
    @field:PositiveOrZero val testShort: Short,
    @field:PositiveOrZero val testInt: Int,
    @field:PositiveOrZero val testLong: Long,
    @field:PositiveOrZero val testFloat: Float,
    @field:PositiveOrZero val testDouble: Double,
  )
}
