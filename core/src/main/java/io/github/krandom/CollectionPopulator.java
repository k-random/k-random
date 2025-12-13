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

import static io.github.krandom.util.ReflectionUtils.*;

import io.github.krandom.randomizers.range.IntRangeRandomizer;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Random collection populator.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class CollectionPopulator {

  private final KRandom kRandom;

  /**
   * Create a new collection populator backed by the given {@link KRandom} instance.
   *
   * <p>The provided {@code KRandom} is used to determine the collection size (according to {@link
   * KRandomParameters}) and to populate elements of the target collection when the field is
   * parameterized with a populatable element type.
   *
   * @param kRandom the random data generator and engine used for population (must not be {@code
   *     null})
   */
  public CollectionPopulator(final KRandom kRandom) {
    this.kRandom = kRandom;
  }

  /**
   * Create and populate a random {@link Collection} for the given field.
   *
   * <p>The resulting collection type depends on the field declaration:
   *
   * <ul>
   *   <li>If the field type is a collection interface, a default concrete implementation is
   *       instantiated.
   *   <li>If the field type is a concrete collection class, an empty instance of that class is
   *       created.
   * </ul>
   *
   * <p>The size of the collection is chosen randomly within the range defined by {@link
   * KRandomParameters#getCollectionSizeRange()} in the provided {@link RandomizationContext}. If
   * the field is parameterized (for example {@code List<Person>}), and the element type is
   * considered populatable, each element is populated using {@link KRandom#doPopulateBean(Class,
   * RandomizationContext)}. For raw types (for example {@code List}) or non-populatable element
   * types, the collection will remain empty.
   *
   * @param field the collection-typed field that should be populated
   * @param context the randomization context providing parameters and state
   * @return a new collection instance with a random size and, when applicable, randomly populated
   *     elements
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public Collection<?> getRandomCollection(final Field field, final RandomizationContext context) {
    int randomSize = getRandomCollectionSize(context.getParameters());
    Class<?> fieldType = field.getType();
    Type fieldGenericType = field.getGenericType();
    Collection collection;

    if (isInterface(fieldType)) {
      collection = getEmptyImplementationForCollectionInterface(fieldType);
    } else {
      collection = createEmptyCollectionForType(fieldType, randomSize);
    }

    if (isParameterizedType(
        fieldGenericType)) { // populate only parametrized types, raw types will be empty
      ParameterizedType parameterizedType = (ParameterizedType) fieldGenericType;
      Type type = parameterizedType.getActualTypeArguments()[0];
      if (isPopulatable(type)) {
        for (int i = 0; i < randomSize; i++) {
          Object item = kRandom.doPopulateBean((Class<?>) type, context);
          collection.add(item);
        }
      }
    }
    return collection;
  }

  private int getRandomCollectionSize(KRandomParameters parameters) {
    KRandomParameters.Range<Integer> collectionSizeRange = parameters.getCollectionSizeRange();
    return new IntRangeRandomizer(
            collectionSizeRange.getMin(), collectionSizeRange.getMax(), kRandom.nextLong())
        .getRandomValue();
  }
}
