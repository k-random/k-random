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
package io.github.krandom.beans

import io.github.krandom.annotation.Exclude
import java.util.Date

@Suppress("unused")
open class Person : Human(), Comparable<Person> {
  @Transient var email: String? = null

  var gender: Gender? = null

  var address: Address? = null

  var birthDate: Date? = null

  var phoneNumber: String? = null

  var nicknames: MutableList<String?>? = null

  var parent: Person? = null

  @Exclude var excluded: String? = null

  override fun compareTo(other: Person): Int {
    return this.name!!.compareTo(other.name!!)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false

    val person = other as Person

    if (email != person.email) return false
    if (gender != person.gender) return false
    if (address != person.address) return false
    return phoneNumber == person.phoneNumber
  }

  override fun hashCode(): Int {
    var result = email?.hashCode() ?: 0
    result = 31 * result + (gender?.hashCode() ?: 0)
    result = 31 * result + (address?.hashCode() ?: 0)
    result = 31 * result + (phoneNumber?.hashCode() ?: 0)
    return result
  }
}
