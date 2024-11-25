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

/**
 * Wrapper for primitive TYPE values and their classes.
 *
 * @author Sam Van Overmeire
 */
internal enum class PrimitiveEnum(@JvmField val type: Class<*>, @JvmField val clazz: Class<*>) {
  BYTE(java.lang.Byte.TYPE, Byte::class.java),
  SHORT(java.lang.Short.TYPE, Short::class.java),
  INTEGER(Integer.TYPE, Int::class.java),
  LONG(java.lang.Long.TYPE, Long::class.java),
  FLOAT(java.lang.Float.TYPE, Float::class.java),
  DOUBLE(java.lang.Double.TYPE, Double::class.java),
  BOOLEAN(java.lang.Boolean.TYPE, Boolean::class.java),
  CHARACTER(Character.TYPE, Char::class.java),
}
