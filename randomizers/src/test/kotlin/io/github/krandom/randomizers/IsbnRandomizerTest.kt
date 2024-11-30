package io.github.krandom.randomizers

import com.oneeyedmen.okeydoke.Approver
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test

@Suppress("JUnitMalformedDeclaration")
internal class IsbnRandomizerTest : FakerBasedRandomizerTest<String>() {
  @Test
  fun `generated number should not be null`() {
    randomizer = IsbnRandomizer()

    randomizer.getRandomValue().shouldNotBeNull()
  }

  @Test
  fun `should generate the same value for the same seed`(approver: Approver) {
    randomizer = IsbnRandomizer(SEED)

    approver.assertApproved(randomizer.getRandomValue())
  }

  @Test
  fun `should generate the same value for the same seed for same locale`(approver: Approver) {
    randomizer = IsbnRandomizer(SEED, LOCALE)

    approver.assertApproved(randomizer.getRandomValue())
  }
}
