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
package io.github.krandom;

import io.github.krandom.randomizers.range.IntegerRangeRandomizer;
import java.lang.reflect.Array;

/**
 * Random array populator.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class ArrayPopulator {

  private final KRandom kRandom;

  /**
   * Create a new array populator backed by the given {@link KRandom} instance.
   *
   * @param kRandom the random data generator used to populate array elements; must not be null
   */
  public ArrayPopulator(final KRandom kRandom) {
    this.kRandom = kRandom;
  }

  /**
   * Generate a new array of random elements for the specified array {@code fieldType}.
   *
   * <p>The array size is chosen uniformly at random within the collection size range defined by
   * {@link KRandomParameters#getCollectionSizeRange()} in the provided {@code context}. Each
   * element is created by delegating to {@link KRandom#doPopulateBean(Class, RandomizationContext)}
   * for the array component type.
   *
   * @param fieldType the expected array type to populate (for example, {@code String[].class});
   *     must represent an array class
   * @param context the current randomization context providing parameters and state; must not be
   *     null
   * @return a newly created array of the requested component type with a random length and randomly
   *     populated elements
   * @throws NullPointerException if {@code fieldType} is not an array type (i.e., its component
   *     type is {@code null}) or if {@code context} is {@code null}
   * @throws NegativeArraySizeException if the configured size range yields a negative size
   */
  public Object getRandomArray(final Class<?> fieldType, final RandomizationContext context) {
    Class<?> componentType = fieldType.getComponentType();
    int randomSize = getRandomArraySize(context.getParameters());
    Object result = Array.newInstance(componentType, randomSize);
    for (int i = 0; i < randomSize; i++) {
      Object randomElement = kRandom.doPopulateBean(componentType, context);
      Array.set(result, i, randomElement);
    }
    return result;
  }

  private int getRandomArraySize(KRandomParameters parameters) {
    KRandomParameters.Range<Integer> collectionSizeRange = parameters.getCollectionSizeRange();
    return new IntegerRangeRandomizer(
            collectionSizeRange.getMin(), collectionSizeRange.getMax(), kRandom.nextLong())
        .getRandomValue();
  }
}
