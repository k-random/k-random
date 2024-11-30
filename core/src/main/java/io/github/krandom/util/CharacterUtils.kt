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

import java.nio.charset.Charset

/**
 * Character utility methods.
 *
 * **This class is intended for internal use only.**
 *
 * @author [Pascal Schumacher](https://github.com/PascalSchumacher)
 */
object CharacterUtils {
  /**
   * Returns a list of all printable charaters of the given charset.
   *
   * @param charset Charset to use
   * @return list of printable characters
   */
  @JvmStatic
  fun collectPrintableCharactersOf(charset: Charset): List<Char> = buildList {
    for (character in Character.MIN_VALUE..Character.MAX_VALUE) {
      if (isPrintable(character)) {
        val characterAsString = character.toString()
        val encoded = characterAsString.toByteArray(charset)
        val decoded = String(encoded, charset)
        if (characterAsString == decoded) {
          add(character)
        }
      }
    }
  }

  /**
   * Keep only letters from a list of characters.
   *
   * @param characters to filter
   * @return only letters
   */
  @JvmStatic
  fun filterLetters(characters: List<Char>): List<Char> =
    characters.filter { ch: Char -> Character.isLetter(ch) }

  private fun isPrintable(character: Char): Boolean {
    val block = Character.UnicodeBlock.of(character)
    return (!character.isISOControl()) && block !== Character.UnicodeBlock.SPECIALS
  }
}
