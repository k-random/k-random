***

<div align="center">
    <b><em>K Random</em></b><br>
    The simple, stupid random Kotlin&trade; and Java&trade; objects generator
</div>

<div align="center">

[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](http://opensource.org/licenses/MIT)
[![Build Status](https://github.com/k-random/k-random/workflows/Java%20CI/badge.svg)](https://github.com/k-random/k-random/actions)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.krandom/k-random-core/badge.svg?style=flat)](https://repo1.maven.org/maven2/org/jeasy/k-random-core/5.0.0/)
[![Javadocs](http://www.javadoc.io/badge/io.github.krandom/k-random-core.svg)](http://www.javadoc.io/doc/io.github.krandom/k-random-core)
[![Project status](https://img.shields.io/badge/Project%20status-Maintenance-orange.svg)](https://img.shields.io/badge/Project%20status-Maintenance-orange.svg)

</div>

***

## Project status

As of November 15, 2020, K-Random is in maintenance mode. This means only bug fixes will be addressed from now on (except
for [records support](https://github.com/k-random/k-random/issues/397) which will be released when Java 16 is out).
Version 5.0.x (based on Java 11) and version 4.3.x (based on Java 8) are the only supported versions
for now. Please consider upgrading to one of these versions at your earliest convenience.

## Latest news

* 15/11/2020: K-Random v5.0.0 is out and is now based on Java 11. Feature wise, this release is the same as v4.3.0. Please check the [release notes](https://github.com/k-random/k-random/releases/tag/k-random-5.0.0) for more details.
* 07/11/2020: K-Random v4.3.0 is now released with support for generic types and fluent setters! You can find all details in the [change log](https://github.com/k-random/k-random/releases/tag/k-random-4.3.0).

# What is K-Random ?

K-Random is a library that generates random Java objects. You can think of it as an [ObjectMother](https://martinfowler.com/bliki/ObjectMother.html) for the JVM. Let's say you have a class `Person` and you want to generate a random instance of it, here we go:

```java
KRandom kRandom = new KRandom();
Person person = kRandom.nextObject(Person.class);
```

The method `KRandom#nextObject` is able to generate random instances of any given type.

# What is this KRandom API ?

The `java.util.Random` API provides 7 methods to generate random data: `nextInt()`, `nextLong()`, `nextDouble()`, `nextFloat()`, `nextBytes()`, `nextBoolean()` and `nextGaussian()`.
What if you need to generate a random `String`? Or say a random instance of your domain object?
K-Random provides the `KRandom` API that extends `java.util.Random` with a method called `nextObject(Class type)`.
This method is able to generate a random instance of any arbitrary Java bean.

The `KRandomParameters` class is the main entry point to configure `KRandom` instances. It allows you to set all
parameters to control how random data is generated:

```java
KRandomParameters parameters = new KRandomParameters()
   .seed(123L)
   .objectPoolSize(100)
   .randomizationDepth(3)
   .charset(forName("UTF-8"))
   .timeRange(nine, five)
   .dateRange(today, tomorrow)
   .stringLengthRange(5, 50)
   .collectionSizeRange(1, 10)
   .scanClasspathForConcreteTypes(true)
   .overrideDefaultInitialization(false)
   .ignoreRandomizationErrors(true);

KRandom kRandom = new KRandom(parameters);
```

For more details about these parameters, please refer to the [configuration parameters](https://github.com/k-random/k-random/wiki/Randomization-parameters) section.

In most cases, default options are enough and you can use the default constructor of `KRandom`.

K-Random allows you to control how to generate random data through the [`api.io.github.krandomizer`](https://github.com/k-random/k-random/blob/main/k-random-core/src/main/java/org/jeasy/random/api/Randomizer.java) interface and makes it easy to exclude some fields from the object graph using a `java.util.function.Predicate`:

```java
KRandomParameters parameters = new KRandomParameters()
   .randomize(String.class, () -> "foo")
   .excludeField(named("age").and(ofType(Integer.class)).and(inClass(Person.class)));

KRandom kRandom = new KRandom(parameters);
Person person = kRandom.nextObject(Person.class);
```

In the previous example, K-Random will:

* Set all fields of type `String` to `foo` (using the `Randomizer` defined as a lambda expression)
* Exclude the field named `age` of type `Integer` in class `Person`.

The static methods `named`, `ofType` and `inClass` are defined in [`random.k.io.github.FieldPredicates`](https://github.com/k-random/k-random/blob/main/k-random-core/src/main/java/org/jeasy/random/FieldPredicates.java) 
which provides common predicates you can use in combination to define exactly which fields to exclude.
A similar class called [`TypePredicates`](https://github.com/k-random/k-random/blob/main/k-random-core/src/main/java/org/jeasy/random/TypePredicates.java) can be used to define which types to exclude from the object graph.
You can of course use your own `java.util.function.Predicate` in combination with those predefined predicates. 

# Why K-Random ?

Populating a Java object with random data can look easy at first glance, unless your domain model involves many related classes. In the previous example, let's suppose the `Person` type is defined as follows:

<p align="center">
    <img src="https://raw.githubusercontent.com/wiki/k-random/k-random/images/person.png">
</p>

**Without** K-Random, you would write the following code in order to create an instance of the `Person` class:

```java
Street street = new Street(12, (byte) 1, "Oxford street");
Address address = new Address(street, "123456", "London", "United Kingdom");
Person person = new Person("Foo", "Bar", "foo.bar@gmail.com", Gender.MALE, address);
```

And if these classes do not provide constructors with parameters (may be some legacy types you can't change), you would write:

```java
Street street = new Street();
street.setNumber(12);
street.setType((byte) 1);
street.setName("Oxford street");

Address address = new Address();
address.setStreet(street);
address.setZipCode("123456");
address.setCity("London");
address.setCountry("United Kingdom");

Person person = new Person();
person.setFirstName("Foo");
person.setLastName("Bar");
person.setEmail("foo.bar@gmail.com");
person.setGender(Gender.MALE);
person.setAddress(address);
```

**With** K-Random, generating a random `Person` object is done with `new KRandom().nextObject(Person.class)`.
The library will **recursively** populate all the object graph. That's a big difference!

## How can this be useful ?

Sometimes, the test fixture does not really matter to the test logic. For example, if we want to test the result of a new sorting algorithm, we can generate random input data and assert the output is sorted, regardless of the data itself:

```java
@org.junit.Test
public void testSortAlgorithm() {

   // Given
   int[] ints = kRandom.nextObject(int[].class);

   // When
   int[] sortedInts = myAwesomeSortAlgo.sort(ints);

   // Then
   assertThat(sortedInts).isSorted(); // fake assertion

}
```

Another example is testing the persistence of a domain object, we can generate a random domain object, persist it and assert the database contains the same values:

```java
@org.junit.Test
public void testPersistPerson() throws Exception {
   // Given
   Person person = kRandom.nextObject(Person.class);

   // When
   personDao.persist(person);

   // Then
   assertThat("person_table").column("name").value().isEqualTo(person.getName()); // assretj db
}
```

There are many other uses cases where K-Random can be useful, you can find a non exhaustive list in the [wiki](https://github.com/k-random/k-random/wiki/use-cases).

## Extensions

* [JUnit extension](https://glytching.github.io/junit-extensions/randomBeans): Use K-Random to generate random data in JUnit tests (courtesy of [glytching](https://github.com/glytching))
* [Vavr extension](https://github.com/xShadov/k-random-vavr-extension): This extension adds support to randomize [Vavr](https://github.com/vavr-io/vavr) types (courtesy of [xShadov](https://github.com/xShadov))
* [Protocol Buffers extension](https://github.com/murdos/k-random-protobuf): This extension adds support to randomize [Protocol Buffers](https://developers.google.com/protocol-buffers) generated types (courtesy of [murdos](https://github.com/murdos))

## Articles and blog posts

* [Easy testing with ObjectMothers and KRandom](https://www.jworks.io/easy-testing-with-objectmothers-and-KRandom/)
* [Quick Guide to KRandom in Java](https://www.baeldung.com/java-k-random)
* [Top secrets of the efficient test data preparation](https://waverleysoftware.com/blog/test-data-preparation/)
* [Random Data Generators for API Testing in Java](https://techblog.dotdash.com/random-data-generators-for-api-testing-in-java-369c99075208)
* [KRandom 4.0 Released](https://www.jworks.io/KRandom-4-0-released/)
* [Type Erasure Revisited](https://www.beyondjava.net/type-erasure-revisited)
* [Generate Random Test Data With jPopulator](https://www.beyondjava.net/newsflash-generate-random-test-data-jpopulator)

## Who is using K-Random ?

* [Netflix](https://github.com/Netflix/AWSObjectMapper/blob/v1.11.723/build.gradle#L71)
* [JetBrains](https://github.com/JetBrains/intellij-community/blob/201.6073/plugins/gradle/tooling-extension-impl/testSources/org/jetbrains/plugins/gradle/tooling/serialization/ToolingSerializerTest.kt#L8)
* [Mulesoft](https://github.com/mulesoft/mule-wsc-connector/blob/1.5.6/src/test/java/org/mule/extension/ws/ConfigEqualsTestCase.java#L14)
* [Easy Cucumber](https://github.com/osvaldjr/easy-cucumber/blob/0.0.18/pom.xml#L122)
* [Open Network Automation Platform](https://github.com/onap/vid/blob/6.0.3/vid-app-common/src/test/java/org/onap/vid/controller/MsoControllerTest.java#L43)

## Contribution

You are welcome to contribute to the project with pull requests on GitHub.
Please note that K-Random is in [maintenance mode](https://github.com/k-random/k-random#project-status),
which means only pull requests for bug fixes will be considered.

If you believe you found a bug or have any question, please use the [issue tracker](https://github.com/k-random/k-random/issues).

## Core team and contributors

#### Core team

* [Mahmoud Ben Hassine](https://github.com/benas)
* [Pascal Schumacher](https://github.com/PascalSchumacher)

#### Awesome contributors

* [Adriano Machado](https://github.com/ammachado)
* [Alberto Lagna](https://github.com/alagna)
* [Andrew Neal](https://github.com/aeneal)
* [Aurélien Mino](https://github.com/murdos)
* [Arne Zelasko](https://github.com/arnzel)
* [dadiyang](https://github.com/dadiyang)
* [Dovid Kopel](https://github.com/dovidkopel)
* [Eric Taix](https://github.com/eric-taix)
* [euZebe](https://github.com/euzebe)
* [Fred Eckertson](https://github.com/feckertson)
* [huningd](https://github.com/huningd)
* [Johan Kindgren](https://github.com/johkin)
* [Joren Inghelbrecht](https://github.com/joinghel)
* [Jose Manuel Prieto](https://github.com/prietopa)
* [kermit-the-frog](https://github.com/kermit-the-frog)
* [Lucas Andersson](https://github.com/LucasAndersson)
* [Michael Düsterhus](https://github.com/reitzmichnicht)
* [Nikola Milivojevic](https://github.com/dziga)
* [nrenzoni](https://github.com/nrenzoni)
* [Oleksandr Shcherbyna](https://github.com/sansherbina)
* [Petromir Dzhunev](https://github.com/petromir)
* [Rebecca McQuary](https://github.com/rmcquary)
* [Rémi Alvergnat](http://www.pragmasphere.com)
* [Rodrigue Alcazar](https://github.com/rodriguealcazar)
* [Ryan Dunckel](https://github.com/sparty02)
* [Sam Van Overmeire](https://github.com/VanOvermeire)
* [Valters Vingolds](https://github.com/valters)
* [Vincent Potucek](https://github.com/punkratz312)
* [Weronika Redlarska](https://github.com/weronika-redlarska)
* [Konstantin Lutovich](https://github.com/lutovich)
* [Steven_Van_Ophem](https://github.com/stevenvanophem)
* [Jean-Michel Leclercq](https://github.com/LeJeanbono)
* [Marian Jureczko](https://github.com/mjureczko)
* [Unconditional One](https://github.com/unconditional)
* [JJ1216](https://github.com/JJ1216)
* [Sergey Chernov](https://github.com/seregamorph)

Thank you all for your contributions!

## License

The [MIT License](http://opensource.org/licenses/MIT). See [LICENSE.txt](https://github.com/k-random/k-random/blob/main/LICENSE.txt).
