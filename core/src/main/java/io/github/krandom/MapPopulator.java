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

import io.github.krandom.api.ObjectFactory;
import io.github.krandom.randomizers.range.IntRangeRandomizer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Map;

/**
 * Populates {@link Map}-typed fields with random entries.
 *
 * <p>This helper creates an appropriate {@link Map} instance for the target field (choosing a
 * concrete implementation for interfaces, and handling {@link java.util.EnumMap} specially), then
 * fills it with randomly generated key/value pairs using {@link KRandom}. Only parameterized map
 * types are populated; raw map types are left empty. If the key type cannot be determined (for
 * example, a raw {@code EnumMap}), this method may return {@code null}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class MapPopulator {

  private final KRandom kRandom;

  private final ObjectFactory objectFactory;

  /**
   * Create a new {@code MapPopulator}.
   *
   * @param kRandom the engine used to generate random keys and values
   * @param objectFactory factory used to instantiate concrete map implementations when needed
   */
  public MapPopulator(final KRandom kRandom, final ObjectFactory objectFactory) {
    this.kRandom = kRandom;
    this.objectFactory = objectFactory;
  }

  /**
   * Generate a random {@link Map} for the given map-typed {@link Field}.
   *
   * <p>The size of the map is chosen randomly within the configured {@link
   * KRandomParameters#getCollectionSizeRange() collection size range}. Only parameterized map types
   * are populated; raw maps are returned empty. If the target type is an {@link java.util.EnumMap}
   * without a resolvable key type, {@code null} is returned.
   *
   * @param field a field whose type implements {@link Map}
   * @param context the current randomization context
   * @return a concrete map instance filled with random entries, empty, or {@code null} when the key
   *     type cannot be resolved
   */
  @SuppressWarnings("unchecked")
  public Map<?, ?> getRandomMap(final Field field, final RandomizationContext context) {
    int randomSize = getRandomMapSize(context.getParameters());
    Class<?> fieldType = field.getType();
    Type fieldGenericType = field.getGenericType();
    Map<Object, Object> map;

    if (isInterface(fieldType)) {
      map = (Map<Object, Object>) getEmptyImplementationForMapInterface(fieldType);
    } else {
      try {
        map = (Map<Object, Object>) fieldType.getDeclaredConstructor().newInstance();
      } catch (InstantiationException
          | IllegalAccessException
          | NoSuchMethodException
          | InvocationTargetException e) {
        // Creating EnumMap with objenesis by-passes the constructor with keyType which leads to CCE
        // at insertion time
        if (fieldType.isAssignableFrom(EnumMap.class)) {
          if (isParameterizedType(fieldGenericType)) {
            Type type = ((ParameterizedType) fieldGenericType).getActualTypeArguments()[0];
            map = new EnumMap((Class<?>) type);
          } else {
            return null;
          }
        } else {
          map = (Map<Object, Object>) objectFactory.createInstance(fieldType, context);
        }
      }
    }

    if (isParameterizedType(
        fieldGenericType)) { // populate only parameterized types, raw types will be empty
      ParameterizedType parameterizedType = (ParameterizedType) fieldGenericType;
      Type keyType = parameterizedType.getActualTypeArguments()[0];
      Type valueType = parameterizedType.getActualTypeArguments()[1];
      if (isPopulatable(keyType) && isPopulatable(valueType)) {
        for (int index = 0; index < randomSize; index++) {
          Object randomKey = kRandom.doPopulateBean((Class<?>) keyType, context);
          Object randomValue = kRandom.doPopulateBean((Class<?>) valueType, context);
          if (randomKey != null) {
            map.put(randomKey, randomValue);
          }
        }
      }
    }
    return map;
  }

  private int getRandomMapSize(KRandomParameters parameters) {
    KRandomParameters.Range<Integer> collectionSizeRange = parameters.getCollectionSizeRange();
    return new IntRangeRandomizer(
            collectionSizeRange.getMin(), collectionSizeRange.getMax(), parameters.getSeed())
        .getRandomValue();
  }
}
