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
import io.github.krandom.ObjectCreationException
import io.github.krandom.api.ObjectFactory
import io.github.krandom.api.RandomizerContext
import io.github.krandom.beans.Address
import io.github.krandom.beans.Person
import io.github.krandom.beans.Street
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class ObjectFactoryTests {
  @Test
  fun `test custom object factory`() {
    // given
    val parameters =
      KRandomParameters()
        .objectFactory(
          object : ObjectFactory {
            @Suppress("UNCHECKED_CAST")
            @Throws(ObjectCreationException::class)
            override fun <T> createInstance(type: Class<T>, context: RandomizerContext): T {
              try {
                // use custom logic for a specific type
                if (type.isAssignableFrom(Address::class.java)) {
                  return Address().apply {
                    city = "Brussels"
                    country = "Belgium"
                    zipCode = "1000"
                    street =
                      Street().apply {
                        name = "main street"
                        number = 1
                        streetType = 1
                      }
                  } as T
                }
                // use regular constructor for other types
                return type.getDeclaredConstructor().newInstance()
              } catch (e: Exception) {
                throw ObjectCreationException("Unable to create a new instance of " + type, e)
              }
            }
          }
        )
    val kRandom = KRandom(parameters)

    val person = kRandom.nextObject(Person::class.java)

    person.shouldNotBeNull()
    person.id.shouldNotBeNull()
    person.name.shouldNotBeNull()
    person.gender.shouldNotBeNull()
    person.email.shouldNotBeNull()
    person.phoneNumber.shouldNotBeNull()
    person.birthDate.shouldNotBeNull()
    person.nicknames.shouldNotBeNull()
    person.excluded.shouldBeNull()
    person.address.shouldNotBeNull()
    person.address.country shouldBe "Belgium"
    person.address.city shouldBe "Brussels"
    person.address.zipCode shouldBe "1000"
    person.address.street.shouldNotBeNull()
    person.address.street.name shouldBe "main street"
    person.address.street.number shouldBe 1
    person.address.street.streetType shouldBe 1.toByte()
  }
}
