package io.github.krandom.randomizers

import com.oneeyedmen.okeydoke.Approver
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test

@Suppress("JUnitMalformedDeclaration")
internal class FullNameRandomizerTest : FakerBasedRandomizerTest<String>() {
  @Test
  fun `generated number should not be null`() {
    randomizer = FullNameRandomizer()

    randomizer.getRandomValue().shouldNotBeNull()
  }

  @Test
  fun `should generate the same value for the same seed`(approver: Approver) {
    randomizer = FullNameRandomizer(SEED)

    approver.assertApproved(randomizer.getRandomValue())
  }

  @Test
  fun `should generate the same value for the same seed for same locale`(approver: Approver) {
    randomizer = FullNameRandomizer(SEED, LOCALE)

    approver.assertApproved(randomizer.getRandomValue())
  }
}
