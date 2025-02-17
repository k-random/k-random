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
package io.github.krandom.randomizers.range;

import java.time.LocalTime;

/**
 * Generate a random {@link LocalTime} in the given range.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class LocalTimeRangeRandomizer extends AbstractRangeRandomizer<LocalTime> {

  /**
   * Create a new {@link LocalTimeRangeRandomizer}.
   *
   * @param min min value (inclusive)
   * @param max max value (exclusive)
   */
  public LocalTimeRangeRandomizer(final LocalTime min, final LocalTime max) {
    super(min, max);
  }

  /**
   * Create a new {@link LocalTimeRangeRandomizer}.
   *
   * @param min min value (inclusive)
   * @param max max value (exclusive)
   * @param seed initial seed
   */
  public LocalTimeRangeRandomizer(final LocalTime min, final LocalTime max, final long seed) {
    super(min, max, seed);
  }

  @Override
  protected void checkValues() {
    if (min.isAfter(max)) {
      throw new IllegalArgumentException("max must be after min");
    }
  }

  @Override
  protected LocalTime getDefaultMinValue() {
    return LocalTime.MIN;
  }

  @Override
  protected LocalTime getDefaultMaxValue() {
    return LocalTime.MAX;
  }

  @Override
  public LocalTime getRandomValue() {
    int minNanoSecond = min.getNano();
    int minSecond = min.getSecond();
    int minMinute = min.getMinute();
    int minHour = min.getHour();

    int maxNanoSecond = max.getNano();
    int maxSecond = max.getSecond();
    int maxMinute = max.getMinute();
    int maxHour = max.getHour();

    int randomNanoSecond = (int) nextDouble(minNanoSecond, maxNanoSecond);
    int randomSecond = (int) nextDouble(minSecond, maxSecond);
    int randomMinute = (int) nextDouble(minMinute, maxMinute);
    int randomHour = (int) nextDouble(minHour, maxHour);
    return LocalTime.of(randomHour, randomMinute, randomSecond, randomNanoSecond);
  }
}
