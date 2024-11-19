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
package io.github.krandom.randomizers.collection;

import io.github.krandom.api.Randomizer;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A {@link Randomizer} that generates a queue of random values using a delegate {@link Randomizer}.
 *
 * @param <T> the type of elements in the queue
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class QueueRandomizer<T> extends CollectionRandomizer<T> {

  /**
   * Create a new {@link QueueRandomizer} that will generate a queue with a random number of
   * elements.
   *
   * @param delegate the delegate {@link Randomizer} used to generate elements
   */
  public QueueRandomizer(final Randomizer<T> delegate) {
    super(delegate);
  }

  /**
   * Create a new {@link QueueRandomizer} that will generate a queue with a fixed number of
   * elements.
   *
   * @param delegate The delegate {@link Randomizer} used to generate elements
   * @param nbElements The number of elements to generate
   */
  public QueueRandomizer(final Randomizer<T> delegate, final int nbElements) {
    super(delegate, nbElements);
  }

  @Override
  public Queue<T> getRandomValue() {
    Queue<T> result = new LinkedList<>();
    for (int i = 0; i < nbElements; i++) {
      result.add(getRandomElement());
    }
    return result;
  }

  @Override
  public String toString() {
    return "QueueRandomizer [delegate=" + delegate + ", nbElements=" + nbElements + "]";
  }
}