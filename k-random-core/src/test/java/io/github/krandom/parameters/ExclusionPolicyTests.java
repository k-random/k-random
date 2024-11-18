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
package io.github.krandom.parameters;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.krandom.KRandom;
import io.github.krandom.KRandomParameters;
import io.github.krandom.api.ExclusionPolicy;
import io.github.krandom.api.RandomizerContext;
import io.github.krandom.beans.Address;
import io.github.krandom.beans.Person;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;

class ExclusionPolicyTests {

  @Test
  void testCustomExclusionPolicy() {
    // given
    KRandomParameters parameters =
        new KRandomParameters()
            .exclusionPolicy(
                new ExclusionPolicy() {
                  @Override
                  public boolean shouldBeExcluded(Field field, RandomizerContext context) {
                    return field.getName().equals("birthDate");
                  }

                  @Override
                  public boolean shouldBeExcluded(Class<?> type, RandomizerContext context) {
                    return type.isAssignableFrom(Address.class);
                  }
                });
    KRandom kRandom = new KRandom(parameters);

    // when
    Person person = kRandom.nextObject(Person.class);

    // then
    assertThat(person).isNotNull();
    assertThat(person.getName()).isNotNull();
    assertThat(person.getEmail()).isNotNull();
    assertThat(person.getPhoneNumber()).isNotNull();

    assertThat(person.getBirthDate()).isNull();
    assertThat(person.getAddress()).isNull();
  }
}
