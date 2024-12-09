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
package io.github.krandom.util

import io.github.krandom.annotation.RandomizerArgument
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date as UtilDate
import kotlin.Boolean
import kotlin.Byte
import kotlin.Double
import kotlin.Float
import kotlin.Int
import kotlin.Long
import kotlin.Short

/**
 * Type conversion utility methods.
 *
 * **This class is intended for internal use only.**
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
object ConversionUtils {
  private const val DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"

  @JvmStatic
  fun convertArguments(declaredArguments: Array<RandomizerArgument>): Array<Any?> {
    val numberOfArguments = declaredArguments.size
    val arguments = arrayOfNulls<Any>(numberOfArguments)
    for (i in 0 until numberOfArguments) {
      val type: Class<*> = declaredArguments[i].type.java
      val value = declaredArguments[i].value
      // issue 299: if argument type is array, split values before conversion
      if (type.isArray) {
        val values = value.split(",".toRegex()).dropLastWhile { it.isEmpty() }.map { it.trim() }
        arguments[i] = convertArray(values, type)
      } else {
        arguments[i] = convertValue(value, type)
      }
    }
    return arguments
  }

  @JvmStatic
  fun convertDateToLocalDate(date: UtilDate): LocalDate =
    date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

  private fun convertArray(list: List<Any>, type: Class<*>): Array<Any?> {
    return Array(list.size) { i ->
      val value = list[i] as? String
      value?.let { convertValue(it, type) }
    }
  }

  @Suppress("CyclomaticComplexMethod")
  private fun convertValue(value: String, clazz: Class<*>): Any =
    when (clazz::class) {
      Boolean::class -> value.toBoolean()
      Byte::class -> value.toByte()
      Short::class -> value.toShort()
      Int::class -> value.toInt()
      Long::class -> value.toLong()
      Float::class -> value.toFloat()
      Double::class -> value.toDouble()
      BigInteger::class -> BigInteger(value)
      BigDecimal::class -> BigDecimal(value)
      UtilDate::class -> UtilDate.from(parseDate(value).toInstant(ZoneOffset.UTC))
      Date::class -> Date.valueOf(value)
      Time::class -> Time.valueOf(value)
      Timestamp::class -> Timestamp.valueOf(value)
      LocalDate::class -> LocalDate.parse(value)
      LocalTime::class -> LocalTime.parse(value)
      LocalDateTime::class -> LocalDateTime.parse(value)
      else -> value
    }

  private fun parseDate(value: String) =
    LocalDateTime.parse(value, DateTimeFormatter.ofPattern(DATE_PATTERN))
}
