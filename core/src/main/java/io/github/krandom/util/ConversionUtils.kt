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
import java.lang.reflect.Array as ReflectArray
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
  private const val COMMA_SEPARATOR = ","
  private const val DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"
  private val CONVERTERS: Map<Class<*>, (String) -> Any> =
    mapOf(
      Boolean::class.java to { it.toBoolean() },
      Byte::class.java to { it.toByte() },
      Short::class.java to { it.toShort() },
      Int::class.java to { it.toInt() },
      Long::class.java to { it.toLong() },
      Float::class.java to { it.toFloat() },
      Double::class.java to { it.toDouble() },
      BigInteger::class.java to { BigInteger(it) },
      BigDecimal::class.java to { BigDecimal(it) },
      UtilDate::class.java to { UtilDate.from(parseDate(it).toInstant(ZoneOffset.UTC)) },
      Date::class.java to { Date.valueOf(it) },
      Time::class.java to { Time.valueOf(it) },
      Timestamp::class.java to { Timestamp.valueOf(it) },
      LocalDate::class.java to { LocalDate.parse(it) },
      LocalTime::class.java to { LocalTime.parse(it) },
      LocalDateTime::class.java to { LocalDateTime.parse(it) }
    )

  @JvmStatic
  fun convertArguments(declaredArguments: Array<RandomizerArgument>): Array<Any?> {

    val arguments = arrayOfNulls<Any>(declaredArguments.size)

    declaredArguments.forEachIndexed { index, randomizerArgument ->
      arguments[index] =
        if (randomizerArgument.type.java.isArray) {
          handleArrayType(randomizerArgument.value, randomizerArgument.type.java)
        } else {
          convertValue(randomizerArgument.value, randomizerArgument.type.java)
        }
    }

    return arguments
  }

  @JvmStatic
  fun convertDateToLocalDate(date: UtilDate): LocalDate =
    date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

  private fun handleArrayType(value: String, type: Class<*>): Any? {
    val values =
      value.split(COMMA_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.map { it.trim() }
    return convertArray(values, type)
  }

  private fun convertArray(list: List<Any>, type: Class<*>): Any {
    val componentType = type.componentType
    val array = ReflectArray.newInstance(componentType, list.size)

    list.forEachIndexed { i, item ->
      val value = item as? String
      value?.let {
        val convertedValue = convertValue(it, componentType)
        ReflectArray.set(array, i, convertedValue)
      }
    }

    return array
  }

  private fun convertValue(value: String, clazz: Class<*>): Any {
    // Special handling for Integer class
    if (clazz == Integer::class.java) {
      return Integer.valueOf(value)
    }

    return CONVERTERS[clazz]?.invoke(value) ?: value
  }

  private fun parseDate(value: String) =
    LocalDateTime.parse(value, DateTimeFormatter.ofPattern(DATE_PATTERN))
}
