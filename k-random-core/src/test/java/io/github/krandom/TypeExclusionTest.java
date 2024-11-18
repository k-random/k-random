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

import static io.github.krandom.TypePredicates.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.krandom.beans.Address;
import io.github.krandom.beans.Website;
import org.junit.jupiter.api.Test;

class TypeExclusionTest {

  @Test
  void testTypeExclusion() {
    // given
    KRandomParameters parameters =
        new KRandomParameters()
            .excludeType(inPackage("io.github.krandom.beans").or(isInterface()).or(isAbstract()));
    KRandom kRandom = new KRandom(parameters);

    // when
    Foo foo = kRandom.nextObject(Foo.class);

    // then
    assertThat(foo).isNotNull();
    // types from "io.github.krandom.beans" package should be excluded
    assertThat(foo.getAddress()).isNull();
    assertThat(foo.getWebsite()).isNull();
    // abstract types should not be randomized
    assertThat(foo.getBar()).isNull();
    assertThat(foo.getBaz()).isNull();
  }

  static class Foo {
    private String name;
    private Address address;
    private Website website;
    private Bar bar;
    private Baz baz;

    public Foo() {}

    public String getName() {
      return this.name;
    }

    public Address getAddress() {
      return this.address;
    }

    public Website getWebsite() {
      return this.website;
    }

    public Bar getBar() {
      return this.bar;
    }

    public Baz getBaz() {
      return this.baz;
    }

    public void setName(String name) {
      this.name = name;
    }

    public void setAddress(Address address) {
      this.address = address;
    }

    public void setWebsite(Website website) {
      this.website = website;
    }

    public void setBar(Bar bar) {
      this.bar = bar;
    }

    public void setBaz(Baz baz) {
      this.baz = baz;
    }
  }

  interface Bar {}

  abstract class Baz {}
}
