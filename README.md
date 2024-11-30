***

<div align="center">
    <b><em>K-Random</em></b><br>
    A Java library for generating random objects.
</div>

<div align="center">

[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](http://opensource.org/licenses/MIT)
[![Build Status](https://github.com/k-random/k-random/actions/workflows/build-and-test.yml/badge.svg)](https://github.com/k-random/k-random/actions/workflows/build-and-test.yml)
[![Detekt Report](https://github.com/k-random/k-random/actions/workflows/detekt-report.yml/badge.svg)](https://github.com/k-random/k-random/actions/workflows/detekt-report.yml)
[![Kover Report](https://github.com/k-random/k-random/actions/workflows/kover-report.yml/badge.svg)](https://github.com/k-random/k-random/actions/workflows/kover-report.yml)
[![Test Report](https://github.com/k-random/k-random/actions/workflows/test-report.yml/badge.svg)](https://github.com/k-random/k-random/actions/workflows/test-report.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.k-random/k-random-core
)](https://central.sonatype.com/artifact/io.github.k-random/k-random-core)
[![javadoc](https://javadoc.io/badge2/io.github.k-random/k-random-core/javadoc.svg)](https://javadoc.io/doc/io.github.k-random/k-random-core)

</div>

***

# What is K-Random?

K-Random is a Java library that generates random Java objects. It is a fork of the Easy Random project, originally created by Mahmoud Ben Hassine and Pascal Schumacher.

**Key Features:**

* **Object Generation:** Generate random instances of any Java class.
* **Customization:** Control the randomization process with various parameters.
* **Extensibility:** Define custom randomizers for specific types.

**New Features (planned):**

* **Convert to Kotlin:** Convert the project to Kotlin.
* **Utilize `kotlin-reflect`:** Generate Kotlin classes using `kotlin-reflect` library.

**Why K-Random?**

K-Random simplifies the creation of test data and mock objects, making it easier to write unit tests and integration tests. It helps developers save time and effort by automating the process of generating random data.

**How to Use K-Random:**

```java
KRandom kRandom = new KRandom();
MyObject obj = kRandom.nextObject(MyObject.class);
```

**License:**

K-Random is licensed under the MIT License.

**Acknowledgments:**

We would like to express our gratitude to the original authors of Easy Random for their work on the project.