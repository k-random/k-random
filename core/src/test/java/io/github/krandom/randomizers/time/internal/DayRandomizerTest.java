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
package io.github.krandom.randomizers.time.internal;

import static io.github.krandom.randomizers.time.DayRandomizer.MAX_DAY;
import static io.github.krandom.randomizers.time.DayRandomizer.MIN_DAY;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.krandom.randomizers.AbstractRandomizerTest;
import io.github.krandom.randomizers.time.DayRandomizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DayRandomizerTest extends AbstractRandomizerTest<Integer> {

  @BeforeEach
  void setUp() {
    randomizer = new DayRandomizer();
  }

  @Test
  void generatedValueShouldNotBeNull() {
    assertThat(randomizer.getRandomValue()).isNotNull();
  }

  @Test
  void generatedValueShouldBeWithinRange() {
    assertThat(randomizer.getRandomValue()).isBetween(MIN_DAY, MAX_DAY);
  }

  @Test
  void shouldGenerateTheSameValueForTheSameSeed() {
    // Given
    randomizer = new DayRandomizer(SEED);
    Integer expected = 20;

    // When
    Integer actual = randomizer.getRandomValue();

    // Then
    assertThat(actual).isEqualTo(expected);
  }
}
