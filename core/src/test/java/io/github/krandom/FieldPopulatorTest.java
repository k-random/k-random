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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import io.github.krandom.api.ContextAwareRandomizer;
import io.github.krandom.api.Randomizer;
import io.github.krandom.beans.ArrayBean;
import io.github.krandom.beans.CollectionBean;
import io.github.krandom.beans.Human;
import io.github.krandom.beans.MapBean;
import io.github.krandom.beans.Person;
import io.github.krandom.randomizers.misc.SkipRandomizer;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
class FieldPopulatorTest {

  private static final String NAME = "foo";

  @Mock private KRandom kRandom;
  @Mock private RegistriesRandomizerProvider randomizerProvider;
  @Mock private Randomizer randomizer;
  @Mock private ContextAwareRandomizer contextAwareRandomizer;
  @Mock private ArrayPopulator arrayPopulator;
  @Mock private CollectionPopulator collectionPopulator;
  @Mock private MapPopulator mapPopulator;
  @Mock private OptionalPopulator optionalPopulator;

  private FieldPopulator fieldPopulator;

  @BeforeEach
  void setUp() {
    fieldPopulator =
        new FieldPopulator(
            kRandom,
            randomizerProvider,
            arrayPopulator,
            collectionPopulator,
            mapPopulator,
            optionalPopulator);
  }

  @Test
  void whenSkipRandomizerIsRegisteredForTheField_thenTheFieldShouldBeSkipped() throws Exception {
    // Given
    Field name = Human.class.getDeclaredField("name");
    Human human = new Human();
    randomizer = new SkipRandomizer();
    RandomizationContext context = new RandomizationContext(Human.class, new KRandomParameters());
    when(randomizerProvider.getRandomizerByField(name, context)).thenReturn(randomizer);

    // When
    fieldPopulator.populateField(human, name, context);

    // Then
    assertThat(human.getName()).isNull();
  }

  @Test
  void whenCustomRandomizerIsRegisteredForTheField_thenTheFieldShouldBePopulatedWithTheRandomValue()
      throws Exception {
    // Given
    Field name = Human.class.getDeclaredField("name");
    Human human = new Human();
    RandomizationContext context = new RandomizationContext(Human.class, new KRandomParameters());
    when(randomizerProvider.getRandomizerByField(name, context)).thenReturn(randomizer);
    when(randomizer.getRandomValue()).thenReturn(NAME);

    // When
    fieldPopulator.populateField(human, name, context);

    // Then
    assertThat(human.getName()).isEqualTo(NAME);
  }

  @Test
  void
      whenContextAwareRandomizerIsRegisteredForTheField_thenTheFieldShouldBeOnTopOfTheSuppliedContextStack()
          throws Exception {
    // Given
    Field name = Human.class.getDeclaredField("name");
    Human human = new Human();
    RandomizationContext context = new RandomizationContext(Human.class, new KRandomParameters());
    final Human[] currentObjectFromContext = new Human[1];
    when(randomizerProvider.getRandomizerByField(name, context)).thenReturn(contextAwareRandomizer);
    when(contextAwareRandomizer.getRandomValue()).thenReturn(NAME);
    doAnswer(
            invocationOnMock -> {
              currentObjectFromContext[0] =
                  (Human)
                      invocationOnMock
                          .getArgument(0, RandomizationContext.class)
                          .getCurrentObject();
              return null;
            })
        .when(contextAwareRandomizer)
        .setRandomizerContext(context);

    // When
    fieldPopulator.populateField(human, name, context);

    // Then
    assertThat(currentObjectFromContext[0]).isEqualTo(human);
  }

  @Test
  void whenTheFieldIsOfTypeArray_thenShouldDelegatePopulationToArrayPopulator() throws Exception {
    // Given
    Field strings = ArrayBean.class.getDeclaredField("strings");
    ArrayBean arrayBean = new ArrayBean();
    String[] object = new String[0];
    RandomizationContext context =
        new RandomizationContext(ArrayBean.class, new KRandomParameters());
    when(arrayPopulator.getRandomArray(strings.getType(), context)).thenReturn(object);

    // When
    fieldPopulator.populateField(arrayBean, strings, context);

    // Then
    assertThat(arrayBean.getStrings()).isEqualTo(object);
  }

  @Test
  void whenTheFieldIsOfTypeCollection_thenShouldDelegatePopulationToCollectionPopulator()
      throws Exception {
    // Given
    Field strings = CollectionBean.class.getDeclaredField("typedCollection");
    CollectionBean collectionBean = new CollectionBean();
    Collection<Person> persons = Collections.emptyList();
    RandomizationContext context =
        new RandomizationContext(CollectionBean.class, new KRandomParameters());

    // When
    fieldPopulator.populateField(collectionBean, strings, context);

    // Then
    assertThat(collectionBean.getTypedCollection()).isEqualTo(persons);
  }

  @Test
  void whenTheFieldIsOfTypeMap_thenShouldDelegatePopulationToMapPopulator() throws Exception {
    // Given
    Field strings = MapBean.class.getDeclaredField("typedMap");
    MapBean mapBean = new MapBean();
    Map<Integer, Person> idToPerson = new HashMap<>();
    RandomizationContext context = new RandomizationContext(MapBean.class, new KRandomParameters());

    // When
    fieldPopulator.populateField(mapBean, strings, context);

    // Then
    assertThat(mapBean.getTypedMap()).isEqualTo(idToPerson);
  }

  @Test
  void whenRandomizationDepthIsExceeded_thenFieldsAreNotInitialized() throws Exception {
    // Given
    Field name = Human.class.getDeclaredField("name");
    Human human = new Human();
    RandomizationContext context = Mockito.mock(RandomizationContext.class);
    when(context.hasExceededRandomizationDepth()).thenReturn(true);

    // When
    fieldPopulator.populateField(human, name, context);

    // Then
    assertThat(human.getName()).isNull();
  }
}
