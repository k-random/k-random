package io.github.krandom.validation

import io.github.krandom.KRandom
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.ints.shouldBeInRange
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.constraints.Size
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SizeAnnotationHandlerTest {
  private lateinit var kRandom: KRandom

  @BeforeEach
  fun setUp() {
    kRandom = KRandom()
  }

  @Test
  fun `generated bean should be valid according to validation constraints`() {
    val testBean = kRandom.nextObject(TestBean::class.java)

    testBean.testString.length shouldBeInRange 1..10
    testBean.testList.size shouldBeInRange 1..10
    testBean.testArray.size shouldBeInRange 1..10
    testBean.testMap.size shouldBeInRange 1..10
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

  @Suppress("ArrayInDataClass")
  internal data class TestBean(
    @field:Size(min = 1, max = 10) val testString: String,
    @field:Size(min = 1, max = 10) val testList: List<String>,
    @field:Size(min = 1, max = 10) val testArray: Array<String>,
    @field:Size(min = 1, max = 10) val testMap: Map<String, String>,
  )
}
