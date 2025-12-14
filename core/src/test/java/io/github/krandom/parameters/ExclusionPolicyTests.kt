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
import io.github.krandom.api.ExclusionPolicy
import io.github.krandom.api.RandomizerContext
import io.github.krandom.beans.Address
import io.github.krandom.beans.Person
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import java.lang.reflect.Field
import org.junit.jupiter.api.Test

internal class ExclusionPolicyTests {
  @Test
  fun `test custom exclusion policy`() {
    val parameters =
      KRandomParameters()
        .exclusionPolicy(
          object : ExclusionPolicy {
            override fun shouldBeExcluded(field: Field, context: RandomizerContext): Boolean {
              return field.name == "birthDate"
            }

            override fun shouldBeExcluded(type: Class<*>, context: RandomizerContext): Boolean {
              return type.isAssignableFrom(Address::class.java)
            }
          }
        )
    val kRandom = KRandom(parameters)

    val person = kRandom.nextObject<Person>(Person::class.java)

    person.shouldNotBeNull()
    person.name.shouldNotBeNull()
    person.email.shouldNotBeNull()
    person.phoneNumber.shouldNotBeNull()
    person.birthDate.shouldBeNull()
    person.address.shouldBeNull()
  }
}
