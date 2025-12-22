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
package io.github.krandom.context

import io.github.krandom.api.ContextAwareRandomizer
import io.github.krandom.api.RandomizerContext

/**
 * A city randomizer that depends on the country of the currently randomized object. The currently
 * randomized object can be retrieved from the randomization context.
 */
class CityRandomizer : ContextAwareRandomizer<City?> {
  private lateinit var context: RandomizerContext

  override fun setRandomizerContext(context: RandomizerContext) {
    this.context = context
  }

  override fun getRandomValue(): City? {
    val person = context.rootObject as Person
    val country = person.country ?: return null
    val countryName = country.name
    return if (countryName != null && countryName.equals("france", ignoreCase = true)) {
      City("paris")
    } else if (countryName != null && countryName.equals("germany", ignoreCase = true)) {
      City("berlin")
    } else if (countryName != null && countryName.equals("belgium", ignoreCase = true)) {
      City("brussels")
    } else null
  }
}
