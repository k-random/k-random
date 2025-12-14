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
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.ranges.shouldBeIn
import org.junit.jupiter.api.Test

internal class CollectionSizeRangeParameterTests {
  @Test
  fun `should not allow negative min collection size`() {
    shouldThrow<IllegalArgumentException> { KRandomParameters().collectionSizeRange(-1, 10) }
  }

  @Test
  fun `should not allow min collection size greater than max collection size`() {
    shouldThrow<IllegalArgumentException> { KRandomParameters().collectionSizeRange(2, 1) }
  }

  @Test
  fun `generated collection size should be in specified range`() {
    val parameters = KRandomParameters().collectionSizeRange(0, 10)

    val list = KRandom(parameters).nextObject(ArrayList::class.java)

    list.shouldNotBeNull()
    list.size shouldBeIn 0..10
  }

  @Test
  fun `collection size range should work for arrays`() {
    val parameters = KRandomParameters().collectionSizeRange(0, 10)

    val strArr = KRandom(parameters).nextObject(Array<String>::class.java)

    strArr.shouldNotBeNull()
    strArr.size shouldBeIn 0..10
  }
}
