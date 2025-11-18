package io.github.krandom.randomizers.faker

import com.oneeyedmen.okeydoke.Approver
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test

@Suppress("JUnitMalformedDeclaration")
internal class StateRandomizerTest : FakerBasedRandomizerTest<String>() {
  @Test
  fun `generated number should not be null`() {
    randomizer = StateRandomizer()

    randomizer.getRandomValue().shouldNotBeNull()
  }

  @Test
  fun `should generate the same value for the same seed`(approver: Approver) {
    randomizer = StateRandomizer(SEED)

    approver.assertApproved(randomizer.getRandomValue())
  }

  @Test
  fun `should generate the same value for the same seed for same locale`(approver: Approver) {
    randomizer = StateRandomizer(SEED, LOCALE)

    approver.assertApproved(randomizer.getRandomValue())
  }
}
