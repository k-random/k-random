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
package io.github.krandom.parameters

import io.github.krandom.KRandom
import io.github.krandom.KRandomParameters
import io.github.krandom.beans.Address
import io.github.krandom.beans.Gender
import io.github.krandom.beans.Person
import io.github.krandom.beans.Street
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SeedParameterTests {
  @Test
  fun `generated object should be always the same for the same seed`() {
    val parameters = KRandomParameters().seed(SEED)
    val kRandom = KRandom(parameters)
    val expectedString = "eOMtThyhVNLWUZNRcBaQKxI"
    val expectedPerson = buildExpectedPerson()
    val expectedInts = buildExpectedInts()

    // When
    val actualString = kRandom.nextObject(String::class.java)
    val actualPerson = kRandom.nextObject(Person::class.java)
    val actualInts = kRandom.nextObject(IntArray::class.java)

    // Then
    actualString shouldBe expectedString
    actualPerson shouldBe expectedPerson
    actualInts shouldBe expectedInts
  }

  companion object {
    private const val SEED = 123L

    @JvmStatic
    @Suppress("LongMethod")
    private fun buildExpectedInts(): IntArray {
      return intArrayOf(
        -535098017,
        -1935747844,
        -1219562352,
        696711130,
        308881275,
        -1366603797,
        -875052456,
        1149563170,
        -1809396988,
        1041944832,
        -394597452,
        -1708209621,
        639583273,
        930399700,
        -106429739,
        1967925707,
        281732816,
        382363784,
        298577043,
        525072488,
        389778123,
        1452179944,
        1823070661,
        -292028230,
        -539486391,
        -1383466546,
        -1824914989,
        8083668,
        1702941070,
        2146898372,
        1109455496,
        -82323612,
        656237286,
        -851237395,
        1118538028,
        -924378823,
        1982908886,
        61937700,
        1885923537,
        1007147781,
        907979413,
        2048182629,
        -1656946195,
        610315108,
        143700666,
        1887585643,
        -1336180951,
        481114396,
        -1356725194,
        -648969061,
        323234679,
        672907686,
        -228467837,
        1719789600,
        1876370794,
        -260807699,
        -1315052259,
        1788269654,
        -1389857855,
        -736339116,
        -1594362319,
        -1447490197,
        -1826631868,
        132343550,
        1666325652,
        -964773309,
        812299731,
        1789518152,
        114768374,
        796275100,
        135535291,
        -1663939686,
        -728392106,
        1705899379,
        -1116321717,
      )
    }

    @JvmStatic
    private fun buildExpectedPerson() =
      Person().apply {
        name = "lGnpmpDuLcCWwviSUKnVxGJJtsu"
        email = "yedUsFwdkelQbxeTeQOvaScfqIOOmaa"
        phoneNumber = "dpHYZGhtgdntugzvvKAXLhM"
        gender = Gender.FEMALE
        address =
          Address().apply {
            city = "VLhpfQGTMDYpsBZxvfBoeygjb"
            country = "UMaAIKKIkknjWEXJUfPxxQHeWKEJ"
            zipCode = "RYtGKbgicZaHCBRQDSx"
            street =
              Street().apply {
                name = "JxkyvRnL"
                number = -1188957731
                streetType = -35
              }
          }
      }
  }
}
