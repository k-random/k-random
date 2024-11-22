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
package io.github.krandom.context;

import static io.github.krandom.FieldPredicates.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.krandom.KRandom;
import io.github.krandom.KRandomParameters;
import org.junit.jupiter.api.Test;

class ContextAwareRandomizationTests {

  @Test
  void testContextAwareRandomization() {
    // given
    String[] names = {"james", "daniel"};
    KRandomParameters parameters =
        new KRandomParameters()
            .randomize(
                named("firstName").and(ofType(String.class)).and(inClass(Person.class)),
                new FirstNameRandomizer(names))
            .randomize(
                named("lastName").and(ofType(String.class)).and(inClass(Person.class)),
                new LastNameRandomizer())
            .excludeField(named("nickname"));
    KRandom kRandom = new KRandom(parameters);

    // when
    Person person = kRandom.nextObject(Person.class);

    // then
    String firstName = person.getFirstName();
    String lastName = person.getLastName();
    assertThat(firstName).isIn(names);
    assertThat(lastName).isNotNull();
    if (firstName.equalsIgnoreCase("james")) {
      assertThat(lastName.equalsIgnoreCase("bond"));
    }
    if (firstName.equalsIgnoreCase("daniel")) {
      assertThat(lastName.equalsIgnoreCase("craig"));
    }
    assertThat(person.getNickname()).isNull();
  }

  @Test
  void testContextAwareRandomizerWithMultipleTypes() {
    // given
    String[] names = {"james", "daniel"};
    String[] countries = {"france", "germany", "belgium"};
    KRandomParameters parameters =
        new KRandomParameters()
            .randomize(named("firstName").and(ofType(String.class)), new FirstNameRandomizer(names))
            .randomize(named("lastName").and(ofType(String.class)), new LastNameRandomizer())
            .randomize(ofType(Country.class), new CountryRandomizer(countries))
            .randomize(ofType(City.class), new CityRandomizer())
            .excludeField(named("nickname"));
    KRandom kRandom = new KRandom(parameters);

    // when
    Person person = kRandom.nextObject(Person.class);

    // then
    if (person.getFirstName().equalsIgnoreCase("james")) {
      assertThat(person.getLastName().equalsIgnoreCase("bond"));
    }
    if (person.getFirstName().equalsIgnoreCase("daniel")) {
      assertThat(person.getLastName().equalsIgnoreCase("craig"));
    }
    assertThat(person.getNickname()).isNull();

    Pet pet = person.getPet();
    if (pet.getFirstName().equalsIgnoreCase("james")) {
      assertThat(pet.getLastName().equalsIgnoreCase("bond"));
    }
    if (pet.getFirstName().equalsIgnoreCase("daniel")) {
      assertThat(pet.getLastName().equalsIgnoreCase("craig"));
    }

    Country country = person.getCountry();
    City city = person.getCity();

    assertThat(country).isNotNull();
    if (country.getName().equalsIgnoreCase("france")) {
      assertThat(city.getName().equalsIgnoreCase("paris"));
    }
    if (country.getName().equalsIgnoreCase("germany")) {
      assertThat(city.getName().equalsIgnoreCase("berlin"));
    }
    if (country.getName().equalsIgnoreCase("belgium")) {
      assertThat(city.getName().equalsIgnoreCase("brussels"));
    }
  }
}
