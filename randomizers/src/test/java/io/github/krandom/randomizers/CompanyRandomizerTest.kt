package io.github.krandom.randomizers

import com.oneeyedmen.okeydoke.Approver
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test

@Suppress("JUnitMalformedDeclaration")
internal class CompanyRandomizerTest : AbstractRandomizerTest<CompanyRandomizer>() {
  @Test
  fun `generated number should not be null`() {
    val randomizer = CompanyRandomizer()

    randomizer.getRandomValue().shouldNotBeNull()
  }

  @Test
  fun `should generate the same value for the same seed`(approver: Approver) {
    val randomizer = CompanyRandomizer(SEED)

    approver.assertApproved(randomizer.getRandomValue())
  }

  @Test
  fun `should generate the same value for the same seed for same locale`(approver: Approver) {
    val randomizer = CompanyRandomizer(SEED, LOCALE)

    approver.assertApproved(randomizer.getRandomValue())
  }
}
