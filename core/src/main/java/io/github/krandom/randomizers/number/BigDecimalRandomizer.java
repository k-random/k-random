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
package io.github.krandom.randomizers.number;

import io.github.krandom.api.Randomizer;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Generate a random {@link BigDecimal}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class BigDecimalRandomizer implements Randomizer<BigDecimal> {

  private final DoubleRandomizer delegate;
  private Integer scale;
  private RoundingMode roundingMode = RoundingMode.HALF_UP;

  /** Create a new {@link BigDecimalRandomizer}. */
  public BigDecimalRandomizer() {
    delegate = new DoubleRandomizer();
  }

  /**
   * Create a new {@link BigDecimalRandomizer}.
   *
   * @param seed initial seed
   */
  public BigDecimalRandomizer(final long seed) {
    delegate = new DoubleRandomizer(seed);
  }

  /**
   * Create a new {@link BigDecimalRandomizer}. The default rounding mode is {@link
   * RoundingMode#HALF_UP}.
   *
   * @param scale of the {@code BigDecimal} value to be returned.
   */
  public BigDecimalRandomizer(final Integer scale) {
    delegate = new DoubleRandomizer();
    this.scale = scale;
  }

  /**
   * Create a new {@link BigDecimalRandomizer}.
   *
   * @param scale of the {@code BigDecimal} value to be returned.
   * @param roundingMode of the {@code BigDecimal} value to be returned.
   */
  public BigDecimalRandomizer(final Integer scale, final RoundingMode roundingMode) {
    this(scale);
    this.roundingMode = roundingMode;
  }

  /**
   * Create a new {@link BigDecimalRandomizer}.
   *
   * @param scale of the {@code BigDecimal} value to be returned.
   * @param roundingMode of the {@code BigDecimal} value to be returned.
   */
  public BigDecimalRandomizer(
      final long seed, final Integer scale, final RoundingMode roundingMode) {
    this(seed);
    this.scale = scale;
    this.roundingMode = roundingMode;
  }

  @Override
  public BigDecimal getRandomValue() {
    BigDecimal randomValue = new BigDecimal(delegate.getRandomValue());
    if (scale != null) {
      randomValue = randomValue.setScale(this.scale, this.roundingMode);
    }
    return randomValue;
  }
}
