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
package io.github.krandom.validation

import io.github.krandom.api.Randomizer
import io.github.krandom.randomizers.range.BigDecimalRangeRandomizer
import io.github.krandom.randomizers.range.BigIntegerRangeRandomizer
import io.github.krandom.randomizers.range.ByteRangeRandomizer
import io.github.krandom.randomizers.range.DoubleRangeRandomizer
import io.github.krandom.randomizers.range.FloatRangeRandomizer
import io.github.krandom.randomizers.range.IntegerRangeRandomizer
import io.github.krandom.randomizers.range.LongRangeRandomizer
import io.github.krandom.randomizers.range.ShortRangeRandomizer
import io.github.krandom.randomizers.text.StringDelegatingRandomizer
import java.lang.Byte as JavaByte
import java.lang.Double as JavaDouble
import java.lang.Float as JavaFloat
import java.lang.Long as JavaLong
import java.lang.Short as JavaShort
import java.math.BigDecimal
import java.math.BigInteger
import java.util.Random

/** @author dadiyang */
abstract class AbstractNumberBaseAnnotationHandler internal constructor(seed: Long) :
  BeanValidationAnnotationHandler {
  private val random: Random = Random(seed)

  protected fun getRandomizer(
    fieldType: Class<*>,
    minValue: BigDecimal?,
    maxValue: BigDecimal?,
  ): Randomizer<*>? =
    when (fieldType) {
      JavaByte.TYPE,
      JavaByte::class.java,
      kotlin.Byte::class.java ->
        ByteRangeRandomizer(minValue?.toByte(), maxValue?.toByte(), random.nextLong())

      JavaShort.TYPE,
      JavaShort::class.java,
      kotlin.Short::class.java ->
        ShortRangeRandomizer(minValue?.toShort(), maxValue?.toShort(), random.nextLong())

      Integer.TYPE,
      Integer::class.java,
      Int::class.java ->
        IntegerRangeRandomizer(minValue?.toInt(), maxValue?.toInt(), random.nextLong())

      JavaLong.TYPE,
      JavaLong::class.java,
      kotlin.Long::class.java ->
        LongRangeRandomizer(minValue?.toLong(), maxValue?.toLong(), random.nextLong())

      JavaFloat.TYPE,
      JavaFloat::class.java,
      kotlin.Float::class.java ->
        FloatRangeRandomizer(minValue?.toFloat(), maxValue?.toFloat(), random.nextLong())

      JavaDouble.TYPE,
      JavaDouble::class.java,
      kotlin.Double::class.java ->
        DoubleRangeRandomizer(minValue?.toDouble(), maxValue?.toDouble(), random.nextLong())

      BigInteger::class.java ->
        BigIntegerRangeRandomizer(minValue?.toInt(), maxValue?.toInt(), random.nextLong())

      BigDecimal::class.java ->
        BigDecimalRangeRandomizer(minValue?.toDouble(), maxValue?.toDouble(), random.nextLong())

      String::class.java -> {
        val delegate =
          BigDecimalRangeRandomizer(minValue?.toDouble(), maxValue?.toDouble(), random.nextLong())
        StringDelegatingRandomizer(delegate)
      }

      else -> null
    }
}
