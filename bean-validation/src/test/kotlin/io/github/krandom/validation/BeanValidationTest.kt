/*
 * The MIT License
 *
 *   Copyright (c) 2023, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package io.github.krandom.validation

import io.github.krandom.KRandom
import io.github.krandom.KRandomParameters
import io.github.krandom.randomizers.range.BigDecimalRangeRandomizer
import io.github.krandom.randomizers.range.IntRangeRandomizer
import io.github.krandom.randomizers.registry.CustomRandomizerRegistry
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotBeBlank
import jakarta.validation.Valid
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.util.Date
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val MAX_DISCOUNT =
  @Suppress("MaxLineLength")
  "-172107030609593198957981128531960672880453250866065167379115653134715264186471404756317889349573734185935122377325065840297131101787906167574232689596204343415487947721140413473316093972316043446890413561003263715454944152781837116212717062617660104030633130749901949846721903127026505409604629859094889496576"

private const val MIN_DISCOUNT =
  @Suppress("MaxLineLength")
  "7662282876638370609146101740543801632384371011755725427644785896281033154465107481014236865090602870006608143292003443098160947481248487711461114361337135608579588927391230902925850523644737673724379044725003237691291118781433336121334962263919251188630152674215174880065707256545268445171714648124229156864"

internal class BeanValidationTest {
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun `size constraint should not be propagated to embedded beans`() {
    val parameters = KRandomParameters().collectionSizeRange(11, 15).stringLengthRange(16, 20)
    kRandom = KRandom(parameters)
    val bean = kRandom.nextObject(BeanValidationAnnotatedBean::class.java)

    bean.shouldNotBeNull()
    bean.sizedListEmbeddedBean.size shouldBeGreaterThanOrEqualTo 2
    bean.sizedListEmbeddedBean.size shouldBeLessThanOrEqualTo 10
    bean.sizedListEmbeddedBean.forEach { embeddedBean: EmbeddedBean? ->
      embeddedBean.shouldNotBeNull()
      embeddedBean.items.size shouldBeGreaterThanOrEqualTo 11
      embeddedBean.items.size shouldBeLessThanOrEqualTo 15
      embeddedBean.items.forEach { stringItem: String? ->
        stringItem.shouldNotBeNull()
        stringItem.length shouldBeGreaterThanOrEqualTo 16
        stringItem.length shouldBeLessThanOrEqualTo 20
      }
    }
  }

  @Test
  fun `size constraint should take precedence over collection size range in embedded beans`() {
    val parameters = KRandomParameters().collectionSizeRange(11, 15).stringLengthRange(16, 20)
    kRandom = KRandom(parameters)
    val bean = kRandom.nextObject(BeanValidationAnnotatedBean::class.java)

    bean.shouldNotBeNull()
    bean.sizedListEmbeddedBean.size shouldBeGreaterThanOrEqualTo 2
    bean.sizedListEmbeddedBean.size shouldBeLessThanOrEqualTo 10
    bean.sizedListEmbeddedBean.forEach { embeddedBean: EmbeddedBean? ->
      embeddedBean.shouldNotBeNull()
      embeddedBean.items.size shouldBeGreaterThanOrEqualTo 11
      embeddedBean.items.size shouldBeLessThanOrEqualTo 15
      embeddedBean.otherItems.size shouldBeGreaterThanOrEqualTo 3
      embeddedBean.otherItems.size shouldBeLessThanOrEqualTo 5
      embeddedBean.items.forEach { stringItem: String? ->
        stringItem.shouldNotBeNull()
        stringItem.length shouldBeGreaterThanOrEqualTo 16
        stringItem.length shouldBeLessThanOrEqualTo 20
      }
      embeddedBean.otherItems.forEach { stringItem: String? ->
        stringItem.shouldNotBeNull()
        stringItem.length shouldBeGreaterThanOrEqualTo 16
        stringItem.length shouldBeLessThanOrEqualTo 20
      }
    }
  }

  @Test
  fun `generated values should be valid according to validation constraints`() {
    val bean = kRandom.nextObject(BeanValidationAnnotatedBean::class.java)

    bean.shouldNotBeNull()
    bean.unsupported.shouldBeFalse()
    bean.active.shouldBeTrue()
    bean.unusedString.shouldBeNull()
    bean.username.shouldNotBeNull()
    bean.birthday.before(Date()).shouldBeTrue()
    bean.birthdayLocalDateTime.isBefore(LocalDateTime.now()).shouldBeTrue()
    bean.pastInstant.isBefore(Instant.now()).shouldBeTrue()
    (bean.pastOrPresent.time <= Date().time).shouldBeTrue()
    bean.eventDate.after(Date()).shouldBeTrue()
    bean.eventLocalDateTime.isAfter(LocalDateTime.now()).shouldBeTrue()
    (bean.futureOrPresent.time >= Date().time).shouldBeTrue()
    bean.positive shouldBeGreaterThan 0
    bean.positiveOrZero shouldBeGreaterThanOrEqualTo 0
    bean.negative shouldBeLessThan 0
    bean.negativeOrZero shouldBeLessThanOrEqualTo 0
    bean.positiveLong shouldBeGreaterThan 0
    bean.positiveOrZeroLong shouldBeGreaterThanOrEqualTo 0
    bean.negativeLong shouldBeLessThan 0
    bean.negativeOrZeroLong shouldBeLessThanOrEqualTo 0
    bean.notBlank.shouldNotBeBlank()
    bean.email.shouldNotBeBlank()
    bean.email shouldContain "."
    bean.email shouldContain "@"
    bean.maxQuantity shouldBeLessThanOrEqualTo 10
    bean.minQuantity shouldBeGreaterThanOrEqualTo 5
    bean.maxDiscount shouldBeLessThanOrEqualTo BigDecimal("30.00")
    bean.minDiscount shouldBeGreaterThanOrEqualTo BigDecimal("5.00")
    bean.discount shouldBeLessThanOrEqualTo BigDecimal("1.00")
    bean.discount shouldBeGreaterThanOrEqualTo BigDecimal("0.01")
    bean.minQuantity shouldBeGreaterThanOrEqualTo 5
    bean.briefMessage.length shouldBeGreaterThanOrEqualTo 2
    bean.briefMessage.length shouldBeLessThanOrEqualTo 10
    bean.sizedCollection.size shouldBeGreaterThanOrEqualTo 2
    bean.sizedCollection.size shouldBeLessThanOrEqualTo 10
    bean.sizedList.size shouldBeGreaterThanOrEqualTo 2
    bean.sizedList.size shouldBeLessThanOrEqualTo 10
    bean.sizedListEmbeddedBean.size shouldBeGreaterThanOrEqualTo 2
    bean.sizedListEmbeddedBean.size shouldBeLessThanOrEqualTo 10
    bean.sizedSet.size shouldBeGreaterThanOrEqualTo 2
    bean.sizedSet.size shouldBeLessThanOrEqualTo 10
    bean.sizedMap.size shouldBeGreaterThanOrEqualTo 2
    bean.sizedMap.size shouldBeLessThanOrEqualTo 10
    bean.sizedArray.size shouldBeGreaterThanOrEqualTo 2
    bean.sizedArray.size shouldBeLessThanOrEqualTo 10
    bean.sizedString.length shouldBeGreaterThanOrEqualTo 2
    bean.sizedString.length shouldBeLessThanOrEqualTo 255
    bean.regexString shouldMatch "[a-z]{4}"
  }

  @Test
  fun `generated values should be valid according to validation constraints on method`() {
    val bean = kRandom.nextObject(BeanValidationMethodAnnotatedBean::class.java)

    bean.shouldNotBeNull()
    bean.unsupported.shouldBeFalse()
    bean.active.shouldBeTrue()
    bean.unusedString.shouldBeNull()
    bean.username.shouldNotBeNull()
    bean.birthday.before(Date()).shouldBeTrue()
    bean.birthdayLocalDateTime.isBefore(LocalDateTime.now()).shouldBeTrue()
    bean.pastInstant.isBefore(Instant.now()).shouldBeTrue()
    (bean.pastOrPresent.time <= Date().time).shouldBeTrue()
    bean.eventDate.after(Date()).shouldBeTrue()
    bean.eventLocalDateTime.isAfter(LocalDateTime.now()).shouldBeTrue()
    (bean.futureOrPresent.time >= Date().time).shouldBeTrue()
    bean.positive shouldBeGreaterThan 0
    bean.positiveOrZero shouldBeGreaterThanOrEqualTo 0
    bean.negative shouldBeLessThan 0
    bean.negativeOrZero shouldBeLessThanOrEqualTo 0
    bean.positiveLong shouldBeGreaterThan 0
    bean.positiveOrZeroLong shouldBeGreaterThanOrEqualTo 0
    bean.negativeLong shouldBeLessThan 0
    bean.negativeOrZeroLong shouldBeLessThanOrEqualTo 0
    bean.notBlank.shouldNotBeBlank()
    bean.email.shouldNotBeBlank()
    bean.email shouldContain "."
    bean.email shouldContain "@"
    bean.maxQuantity shouldBeLessThanOrEqualTo 10
    bean.minQuantity shouldBeGreaterThanOrEqualTo 5
    bean.maxDiscount shouldBeLessThanOrEqualTo BigDecimal("30.00")
    bean.minDiscount shouldBeGreaterThanOrEqualTo BigDecimal("5.00")
    bean.discount shouldBeLessThanOrEqualTo BigDecimal("1.00")
    bean.discount shouldBeGreaterThanOrEqualTo BigDecimal("0.01")
    bean.minQuantity shouldBeGreaterThanOrEqualTo 5
    bean.briefMessage.length shouldBeGreaterThanOrEqualTo 2
    bean.briefMessage.length shouldBeLessThanOrEqualTo 10
    bean.sizedCollection.size shouldBeGreaterThanOrEqualTo 2
    bean.sizedCollection.size shouldBeLessThanOrEqualTo 10
    bean.sizedList.size shouldBeGreaterThanOrEqualTo 2
    bean.sizedList.size shouldBeLessThanOrEqualTo 10
    bean.sizedListEmbeddedBean.size shouldBeGreaterThanOrEqualTo 2
    bean.sizedListEmbeddedBean.size shouldBeLessThanOrEqualTo 10
    bean.sizedSet.size shouldBeGreaterThanOrEqualTo 2
    bean.sizedSet.size shouldBeLessThanOrEqualTo 10
    bean.sizedMap.size shouldBeGreaterThanOrEqualTo 2
    bean.sizedMap.size shouldBeLessThanOrEqualTo 10
    bean.sizedArray.size shouldBeGreaterThanOrEqualTo 2
    bean.sizedArray.size shouldBeLessThanOrEqualTo 10
    bean.sizedString.length shouldBeGreaterThanOrEqualTo 2
    bean.sizedString.length shouldBeLessThanOrEqualTo 255
    bean.regexString shouldMatch "[a-z]{4}"
  }

  @Test
  fun `generated values for bean without read method`() {
    val bean = kRandom.nextObject(BeanValidationWithoutReadMethodBean::class.java)

    bean.shouldNotBeNull()
    val allNonNull =
      bean.javaClass.declaredFields.all { field ->
        field.isAccessible = true
        field.get(bean) != null
      }
    allNonNull.shouldBeTrue()
  }

  @Test
  fun `should generate the same value for the same seed`() {
    val parameters = KRandomParameters().seed(123L)
    val random = KRandom(parameters)

    val bean = random.nextObject(BeanValidationAnnotatedBean::class.java)

    bean.shouldNotBeNull()
    bean.username shouldBe "eOMtThyhVNLWUZNRcBaQKxI"
    bean.maxQuantity shouldBe -2055951745
    bean.minQuantity shouldBe 91531906
    bean.maxDiscount shouldBe BigDecimal(MAX_DISCOUNT)
    bean.minDiscount shouldBe BigDecimal(MIN_DISCOUNT)
    bean.discount shouldBe BigDecimal("0.182723708049134681008496272625052370131015777587890625")
    bean.minQuantity shouldBe 91531906
    bean.briefMessage shouldBe "tg"
    bean.regexString shouldBe "tguu"
    bean.positive shouldBe 91531902
    bean.positiveOrZero shouldBe 91531901
    bean.negative shouldBe -2055951746
    bean.negativeOrZero shouldBe -2055951746
    bean.email shouldBe "celine.schoen@hotmail.com"
    bean.notBlank shouldBe "tg"
  }

  @Test
  fun `generated bean should be valid using bean validation api`() {
    val bean = kRandom.nextObject(BeanValidationAnnotatedBean::class.java)

    val validator: Validator
    Validation.buildDefaultValidatorFactory().use { validatorFactory ->
      validator = validatorFactory.validator
    }
    val violations = validator.validate(bean)

    violations.shouldBeEmpty()
  }

  @Test
  fun `custom bean validation registry test`() {
    // given
    val parameters =
      KRandomParameters().randomizerRegistry(MyCustomBeanValidationRandomizerRegistry())
    val kRandom = KRandom(parameters)

    // when
    val salary = kRandom.nextObject(Salary::class.java)

    // then
    salary.shouldNotBeNull()
    salary.amount shouldBeLessThanOrEqualTo BigDecimal("99.99")
  }

  @Test
  fun `custom registry test`() {
    // given
    val registry = CustomRandomizerRegistry()
    val scale = 3
    registry.registerRandomizer(
      BigDecimal::class.java,
      BigDecimalRangeRandomizer(5.0, 10.0, scale = scale),
    )
    registry.registerRandomizer(Int::class.java, IntRangeRandomizer(5, 10))
    val parameters = KRandomParameters().randomizerRegistry(registry)
    val kRandom = KRandom(parameters)

    // when
    val discount = kRandom.nextObject(Discount::class.java)

    // then
    discount.shouldNotBeNull()
    discount.discountEffects.shouldNotBeNull()
    discount.discountEffects.shouldNotBeEmpty()
    discount.discountEffects.forEach { discountEffect: DiscountEffect ->
      discountEffect.percentage shouldBeGreaterThanOrEqualTo BigDecimal("5.000")
      discountEffect.percentage shouldBeLessThanOrEqualTo BigDecimal("10.000")
      discountEffect.quantity shouldBeGreaterThanOrEqualTo BigDecimal("5.000")
      discountEffect.quantity shouldBeLessThanOrEqualTo BigDecimal("10.000")
      discountEffect.amount.amount.shouldNotBeNull()
      discountEffect.amount.amount shouldBeGreaterThanOrEqualTo BigDecimal("5.000")
      discountEffect.amount.amount shouldBeLessThanOrEqualTo BigDecimal("10.000")
      discountEffect.size.shouldNotBeNull()
      discountEffect.size shouldBeGreaterThanOrEqualTo 1
      discountEffect.size shouldBeLessThanOrEqualTo 65535
    }
  }

  @Suppress("unused")
  internal data class Salary(@field:Digits(integer = 2, fraction = 2) val amount: BigDecimal)

  internal data class Amount(
    @field:NotNull @field:Digits(integer = 12, fraction = 3) val amount: BigDecimal?
  )

  internal data class DiscountEffect(
    @field:Digits(integer = 6, fraction = 4) val percentage: BigDecimal,
    val amount: Amount,
    @field:Digits(integer = 12, fraction = 3) val quantity: BigDecimal,
    @field:NotNull @field:DecimalMax("65535") @field:DecimalMin("1") val size: Int?,
  )

  internal data class Discount(
    @field:NotNull
    @field:Size(min = 1)
    @field:Valid
    val discountEffects: MutableList<DiscountEffect>?
  )
}
