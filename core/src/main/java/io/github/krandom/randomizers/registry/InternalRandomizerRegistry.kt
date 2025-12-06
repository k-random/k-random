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
package io.github.krandom.randomizers.registry

import io.github.krandom.KRandomParameters
import io.github.krandom.annotation.Priority
import io.github.krandom.api.Randomizer
import io.github.krandom.api.RandomizerRegistry
import io.github.krandom.randomizers.misc.BooleanRandomizer
import io.github.krandom.randomizers.misc.LocaleRandomizer
import io.github.krandom.randomizers.misc.SkipRandomizer
import io.github.krandom.randomizers.misc.UUIDRandomizer
import io.github.krandom.randomizers.net.UriRandomizer
import io.github.krandom.randomizers.net.UrlRandomizer
import io.github.krandom.randomizers.number.AtomicIntegerRandomizer
import io.github.krandom.randomizers.number.AtomicLongRandomizer
import io.github.krandom.randomizers.number.BigDecimalRandomizer
import io.github.krandom.randomizers.number.BigIntegerRandomizer
import io.github.krandom.randomizers.number.ByteRandomizer
import io.github.krandom.randomizers.number.DoubleRandomizer
import io.github.krandom.randomizers.number.FloatRandomizer
import io.github.krandom.randomizers.number.IntegerRandomizer
import io.github.krandom.randomizers.number.LongRandomizer
import io.github.krandom.randomizers.number.ShortRandomizer
import io.github.krandom.randomizers.range.DateRangeRandomizer
import io.github.krandom.randomizers.range.SqlDateRangeRandomizer
import io.github.krandom.randomizers.text.CharacterRandomizer
import io.github.krandom.randomizers.text.StringRandomizer
import io.github.krandom.randomizers.time.CalendarRandomizer
import io.github.krandom.randomizers.time.SqlTimeRandomizer
import io.github.krandom.randomizers.time.SqlTimestampRandomizer
import io.github.krandom.util.ConversionUtils.convertDateToLocalDate
import java.lang.reflect.Field
import java.math.BigDecimal
import java.math.BigInteger
import java.net.URI
import java.net.URL
import java.sql.Date as SqlDate
import java.sql.Time
import java.sql.Timestamp
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.collections.set
import kotlin.reflect.KClass

private const val PRIORITY = -4

/**
 * Registry for Java built-in types.
 *
 * @author Rémi Alvergnat (toilal.dev@gmail.com)
 */
@Priority(PRIORITY)
class InternalRandomizerRegistry : RandomizerRegistry {
  private val randomizers: MutableMap<KClass<*>, Randomizer<*>> = mutableMapOf()

  override fun init(parameters: KRandomParameters) {
    val seed = parameters.seed
    val charset = parameters.charset
    var minDate = Date(Long.MIN_VALUE)
    var maxDate = Date(Long.MAX_VALUE)
    minDate =
      if (convertDateToLocalDate(minDate).isAfter(parameters.dateRange.min)) minDate
      else SqlDate.valueOf(parameters.dateRange.min)
    maxDate =
      if (convertDateToLocalDate(maxDate).isBefore(parameters.dateRange.max)) maxDate
      else SqlDate.valueOf(parameters.dateRange.max)
    val stringRandomizer =
      StringRandomizer(
        charset,
        parameters.stringLengthRange.min,
        parameters.stringLengthRange.max,
        seed,
      )
    val sqlDateRangeRandomizer =
      SqlDateRangeRandomizer(SqlDate(minDate.time), SqlDate(maxDate.time), seed)
    // keep-sorted start
    randomizers[AtomicInteger::class] = AtomicIntegerRandomizer(seed)
    randomizers[AtomicLong::class] = AtomicLongRandomizer(seed)
    randomizers[BigDecimal::class] = BigDecimalRandomizer(seed)
    randomizers[BigInteger::class] = BigIntegerRandomizer(seed)
    randomizers[Boolean::class] = BooleanRandomizer(seed)
    randomizers[Byte::class] = ByteRandomizer(seed)
    randomizers[Calendar::class] = CalendarRandomizer(seed)
    randomizers[Char::class] = CharacterRandomizer(charset, seed)
    randomizers[Class::class] = SkipRandomizer()
    randomizers[Date::class] = DateRangeRandomizer(minDate, maxDate, seed)
    randomizers[Double::class] = DoubleRandomizer(seed)
    randomizers[Exception::class] = SkipRandomizer()
    randomizers[Float::class] = FloatRandomizer(seed)
    randomizers[Int::class] = IntegerRandomizer(seed)
    randomizers[Locale::class] = LocaleRandomizer(seed)
    randomizers[Long::class] = LongRandomizer(seed)
    randomizers[Short::class] = ShortRandomizer(seed)
    randomizers[SqlDate::class] = sqlDateRangeRandomizer
    randomizers[String::class] = stringRandomizer
    randomizers[Time::class] = SqlTimeRandomizer(seed)
    randomizers[Timestamp::class] = SqlTimestampRandomizer(seed)
    randomizers[URI::class] = UriRandomizer(seed)
    randomizers[URL::class] = UrlRandomizer(seed)
    randomizers[UUID::class] = UUIDRandomizer(seed)
    // keep-sorted end
  }

  override fun getRandomizer(field: Field): Randomizer<*>? {
    return getRandomizer(field.type)
  }

  /** {@inheritDoc} */
  override fun getRandomizer(type: Class<*>): Randomizer<*>? {
    return randomizers[type.kotlin]
  }
}
