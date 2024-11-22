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
package io.github.krandom.randomizers.misc;

import static io.github.krandom.randomizers.misc.EnumRandomizerTest.Gender.FEMALE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.krandom.randomizers.AbstractRandomizerTest;
import org.junit.jupiter.api.Test;

class EnumRandomizerTest extends AbstractRandomizerTest<EnumRandomizerTest.Gender> {

  @Test
  void generatedValueShouldBeOfTheSpecifiedEnum() {
    assertThat(new EnumRandomizer(Gender.class).getRandomValue()).isIn(Gender.values());
  }

  @Test
  void shouldAlwaysGenerateTheSameValueForTheSameSeed() {
    assertThat(new EnumRandomizer(Gender.class, SEED).getRandomValue()).isEqualTo(FEMALE);
  }

  @Test
  void shouldAlwaysGenerateTheSameValueForTheSameSeedWithExcludedValues() {
    assertThat(new EnumRandomizer<>(TriState.class, SEED, TriState.Maybe).getRandomValue())
        .isEqualTo(TriState.False);
  }

  public enum Gender {
    MALE,
    FEMALE
  }

  @Test
  void should_return_a_value_different_from_the_excluded_one() {
    Gender valueToExclude = Gender.MALE;
    Gender randomElement = new EnumRandomizer<>(Gender.class, valueToExclude).getRandomValue();
    assertThat(randomElement).isNotNull();
    assertThat(randomElement).isNotEqualTo(valueToExclude);
  }

  @Test
  void should_throw_an_exception_when_all_values_are_excluded() {
    assertThatThrownBy(() -> new EnumRandomizer<>(Gender.class, Gender.values()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  public enum Empty {}

  @Test
  public void should_return_null_for_empty_enum() {
    Empty randomElement = new EnumRandomizer<>(Empty.class).getRandomValue();
    assertThat(randomElement).isNull();
  }

  // always keep three options here, as we want to exclude one and still select the same one
  // deterministically
  @SuppressWarnings("unused")
  private enum TriState {
    True,
    False,
    Maybe
  }
}
