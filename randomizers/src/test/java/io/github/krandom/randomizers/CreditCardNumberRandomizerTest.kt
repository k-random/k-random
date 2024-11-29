package io.github.krandom.randomizers

import com.oneeyedmen.okeydoke.Approver
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test

@Suppress("JUnitMalformedDeclaration")
internal class CreditCardNumberRandomizerTest :
  AbstractRandomizerTest<CreditCardNumberRandomizer>() {
  @Test
  fun `generated number should not be null`() {
    val randomizer = CreditCardNumberRandomizer()

    randomizer.getRandomValue().shouldNotBeNull()
  }

  @Test
  fun `should generate the same value for the same seed`(approver: Approver) {
    val randomizer = CreditCardNumberRandomizer(SEED)

    approver.assertApproved(randomizer.getRandomValue())
  }

  @Test
  fun `should generate the same value for the same seed for same locale`(approver: Approver) {
    val randomizer = CreditCardNumberRandomizer(SEED, LOCALE)

    approver.assertApproved(randomizer.getRandomValue())
  }
}
