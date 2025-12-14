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
package io.github.krandom

import io.github.krandom.api.ExclusionPolicy
import io.github.krandom.api.ObjectFactory
import io.github.krandom.api.Randomizer
import io.github.krandom.api.RandomizerProvider
import io.github.krandom.api.RandomizerRegistry
import io.github.krandom.randomizers.registry.CustomRandomizerRegistry
import io.github.krandom.randomizers.registry.ExclusionRandomizerRegistry
import java.lang.reflect.Field
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Objects
import java.util.function.Predicate

/**
 * Parameters of an [KRandom] instance.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
@Suppress("TooManyFunctions")
class KRandomParameters {
  var seed: Long
  private var objectPoolSize: Int
  private var randomizationDepth: Int
  var charset: Charset
  var isScanClasspathForConcreteTypes: Boolean = false
  var isOverrideDefaultInitialization: Boolean = false
  var isIgnoreRandomizationErrors: Boolean = false
  var isBypassSetters: Boolean = false
  @JvmField var collectionSizeRange: Range<Int>
  var stringLengthRange: Range<Int>
  var dateRange: Range<LocalDate>
  var timeRange: Range<LocalTime>
  private var exclusionPolicy: ExclusionPolicy
  private var objectFactory: ObjectFactory
  private var randomizerProvider: RandomizerProvider

  // internal params
  var customRandomizerRegistry: CustomRandomizerRegistry
    private set

  var exclusionRandomizerRegistry: ExclusionRandomizerRegistry
    private set

  var userRegistries: MutableSet<RandomizerRegistry>
    private set

  var fieldExclusionPredicates: MutableSet<Predicate<Field>>
    private set

  var typeExclusionPredicates: MutableSet<Predicate<Class<*>>>
    private set

  /** Create a new [KRandomParameters] with default values. */
  init {
    seed = DEFAULT_SEED
    charset = DEFAULT_CHARSET
    objectPoolSize = DEFAULT_OBJECT_POOL_SIZE
    randomizationDepth = DEFAULT_RANDOMIZATION_DEPTH
    dateRange = Range(DEFAULT_DATES_RANGE.min.toLocalDate(), DEFAULT_DATES_RANGE.max.toLocalDate())
    timeRange = Range(LocalTime.MIN, LocalTime.MAX)
    collectionSizeRange = DEFAULT_COLLECTION_SIZE_RANGE
    stringLengthRange = DEFAULT_STRING_LENGTH_RANGE
    customRandomizerRegistry = CustomRandomizerRegistry()
    exclusionRandomizerRegistry = ExclusionRandomizerRegistry()
    userRegistries = LinkedHashSet()
    fieldExclusionPredicates = HashSet()
    typeExclusionPredicates = HashSet()
    exclusionPolicy = DefaultExclusionPolicy
    objectFactory = ObjenesisObjectFactory()
    randomizerProvider = RegistriesRandomizerProvider()
  }

  fun getObjectPoolSize(): Int {
    return objectPoolSize
  }

  fun setObjectPoolSize(objectPoolSize: Int) {
    require(objectPoolSize >= 1) { "objectPoolSize must be >= 1" }
    this.objectPoolSize = objectPoolSize
  }

  fun getRandomizationDepth(): Int {
    return randomizationDepth
  }

  fun setRandomizationDepth(randomizationDepth: Int) {
    require(randomizationDepth >= 1) { "randomizationDepth must be >= 1" }
    this.randomizationDepth = randomizationDepth
  }

  fun getExclusionPolicy(): ExclusionPolicy {
    return exclusionPolicy
  }

  fun setExclusionPolicy(exclusionPolicy: ExclusionPolicy) {
    Objects.requireNonNull(exclusionPolicy, "Exclusion policy must not be null")
    this.exclusionPolicy = exclusionPolicy
  }

  fun getObjectFactory(): ObjectFactory {
    return objectFactory
  }

  fun setObjectFactory(objectFactory: ObjectFactory) {
    Objects.requireNonNull(objectFactory, "Object factory must not be null")
    this.objectFactory = objectFactory
  }

  fun getRandomizerProvider(): RandomizerProvider {
    return randomizerProvider
  }

  fun setRandomizerProvider(randomizerProvider: RandomizerProvider) {
    Objects.requireNonNull(randomizerProvider, "Randomizer provider must not be null")
    this.randomizerProvider = randomizerProvider
  }

  /**
   * Register a custom randomizer for the given field predicate. **The predicate must at least
   * specify the field type**
   *
   * @param predicate to identify the field
   * @param randomizer to register
   * @param <T> The field type
   * @return the current [KRandomParameters] instance for method chaining
   * @see FieldPredicates </T>
   */
  fun <T> randomize(predicate: Predicate<Field>, randomizer: Randomizer<T>): KRandomParameters {
    Objects.requireNonNull(predicate, "Predicate must not be null")
    Objects.requireNonNull<Randomizer<T>>(randomizer, "Randomizer must not be null")
    customRandomizerRegistry.registerRandomizer(predicate, randomizer)
    return this
  }

  /**
   * Register a custom randomizer for a given type.
   *
   * @param type class of the type to randomize
   * @param randomizer the custom [Randomizer] to use
   * @param <T> The field type
   * @return the current [KRandomParameters] instance for method chaining </T>
   */
  fun <T> randomize(type: Class<T>, randomizer: Randomizer<T>): KRandomParameters {
    customRandomizerRegistry.registerRandomizer(type, randomizer)
    return this
  }

  /**
   * Exclude a field from being randomized.
   *
   * @param predicate to identify the field to exclude
   * @return the current [KRandomParameters] instance for method chaining
   * @see FieldPredicates
   */
  fun excludeField(predicate: Predicate<Field>): KRandomParameters {
    Objects.requireNonNull(predicate, "Predicate must not be null")
    fieldExclusionPredicates.add(predicate)
    exclusionRandomizerRegistry.addFieldPredicate(predicate)
    return this
  }

  /**
   * Exclude a type from being randomized.
   *
   * @param predicate to identify the type to exclude
   * @return the current [KRandomParameters] instance for method chaining
   * @see FieldPredicates
   */
  fun excludeType(predicate: Predicate<Class<*>>): KRandomParameters {
    Objects.requireNonNull(predicate, "Predicate must not be null")
    typeExclusionPredicates.add(predicate)
    exclusionRandomizerRegistry.addTypePredicate(predicate)
    return this
  }

  /**
   * Provide a custom exclusion policy.
   *
   * @param exclusionPolicy to use
   * @return the current [KRandomParameters] instance for method chaining
   */
  fun exclusionPolicy(exclusionPolicy: ExclusionPolicy): KRandomParameters {
    setExclusionPolicy(exclusionPolicy)
    return this
  }

  /**
   * Provide a custom object factory.
   *
   * @param objectFactory to use
   * @return the current [KRandomParameters] instance for method chaining
   */
  fun objectFactory(objectFactory: ObjectFactory): KRandomParameters {
    setObjectFactory(objectFactory)
    return this
  }

  /**
   * Provide a custom randomizer provider.
   *
   * @param randomizerProvider to use
   * @return the current [KRandomParameters] instance for method chaining
   */
  fun randomizerProvider(randomizerProvider: RandomizerProvider): KRandomParameters {
    setRandomizerProvider(randomizerProvider)
    return this
  }

  /**
   * Set the initial random seed.
   *
   * @param seed the initial seed
   * @return the current [KRandomParameters] instance for method chaining
   */
  fun seed(seed: Long): KRandomParameters {
    this.seed = seed
    return this
  }

  /**
   * Set the charset to use for character based generation.
   *
   * @param charset the charset to use
   * @return the current [KRandomParameters] instance for method chaining
   */
  fun charset(charset: Charset): KRandomParameters {
    this.charset = charset
    return this
  }

  /**
   * Set the collection size range.
   *
   * @param minCollectionSize the minimum collection size
   * @param maxCollectionSize the maximum collection size
   * @return the current [KRandomParameters] instance for method chaining
   */
  fun collectionSizeRange(minCollectionSize: Int, maxCollectionSize: Int): KRandomParameters {
    require(minCollectionSize >= 0) { "minCollectionSize must be >= 0" }
    require(minCollectionSize <= maxCollectionSize) {
      "minCollectionSize ($minCollectionSize) must be <= than maxCollectionSize ($maxCollectionSize)"
    }
    this.collectionSizeRange = Range(minCollectionSize, maxCollectionSize)
    return this
  }

  /**
   * Set the string length range.
   *
   * @param minStringLength the minimum string length
   * @param maxStringLength the maximum string length
   * @return the current [KRandomParameters] instance for method chaining
   */
  fun stringLengthRange(minStringLength: Int, maxStringLength: Int): KRandomParameters {
    require(minStringLength >= 0) { "minStringLength must be >= 0" }
    require(minStringLength <= maxStringLength) {
      "minStringLength ($minStringLength) must be <= than maxStringLength ($maxStringLength)"
    }
    this.stringLengthRange = Range(minStringLength, maxStringLength)
    return this
  }

  /**
   * Set the number of different objects to generate for a type.
   *
   * @param objectPoolSize the number of objects to generate in the pool
   * @return the current [KRandomParameters] instance for method chaining
   */
  fun objectPoolSize(objectPoolSize: Int): KRandomParameters {
    setObjectPoolSize(objectPoolSize)
    return this
  }

  /**
   * Set the randomization depth for objects graph.
   *
   * @param randomizationDepth the maximum randomization depth
   * @return the current [KRandomParameters] instance for method chaining
   */
  fun randomizationDepth(randomizationDepth: Int): KRandomParameters {
    setRandomizationDepth(randomizationDepth)
    return this
  }

  /**
   * Set the date range.
   *
   * @param min date
   * @param max date
   * @return the current [KRandomParameters] instance for method chaining
   */
  fun dateRange(min: LocalDate, max: LocalDate): KRandomParameters {
    require(!min.isAfter(max)) { "Min date should be before max date" }
    this.dateRange = Range(min, max)
    return this
  }

  /**
   * Set the time range.
   *
   * @param min time
   * @param max time
   * @return the current [KRandomParameters] instance for method chaining
   */
  fun timeRange(min: LocalTime, max: LocalTime): KRandomParameters {
    require(!min.isAfter(max)) { "Min time should be before max time" }
    this.timeRange = Range(min, max)
    return this
  }

  /**
   * Register a [RandomizerRegistry].
   *
   * @param registry the [RandomizerRegistry] to register
   * @return the current [KRandomParameters] instance for method chaining
   */
  fun randomizerRegistry(registry: RandomizerRegistry): KRandomParameters {
    Objects.requireNonNull(registry, "Registry must not be null")
    userRegistries.add(registry)
    return this
  }

  /**
   * Should the classpath be scanned for concrete types when a field with an interface or abstract
   * class type is encountered?
   *
   * Deactivated by default.
   *
   * @param scanClasspathForConcreteTypes whether to scan the classpath or not
   * @return the current [KRandomParameters] instance for method chaining
   */
  fun scanClasspathForConcreteTypes(scanClasspathForConcreteTypes: Boolean): KRandomParameters {
    this.isScanClasspathForConcreteTypes = scanClasspathForConcreteTypes
    return this
  }

  /**
   * With this parameter, any randomization error will be silently ignored and the corresponding
   * field will be set to null.
   *
   * Deactivated by default.
   *
   * @param ignoreRandomizationErrors whether to silently ignore randomization errors or not
   * @return the current [KRandomParameters] instance for method chaining
   */
  fun ignoreRandomizationErrors(ignoreRandomizationErrors: Boolean): KRandomParameters {
    this.isIgnoreRandomizationErrors = ignoreRandomizationErrors
    return this
  }

  /**
   * Should default initialization of field values be overridden? E.g., should the values of the
   * `strings` and `integers` fields below be kept untouched or should they be randomized.
   * Deactivated by default.
   *
   * @param overrideDefaultInitialization whether to override default initialization of field values
   *   or not
   * @return the current [KRandomParameters] instance for method chaining
   */
  fun overrideDefaultInitialization(overrideDefaultInitialization: Boolean): KRandomParameters {
    this.isOverrideDefaultInitialization = overrideDefaultInitialization
    return this
  }

  /**
   * Flag to bypass setters if any and use reflection directly instead. False by default.
   *
   * @param bypassSetters true if setters should be ignored
   * @return the current [KRandomParameters] instance for method chaining
   */
  fun bypassSetters(bypassSetters: Boolean): KRandomParameters {
    this.isBypassSetters = bypassSetters
    return this
  }

  /**
   * Utility class to hold a range of values.
   *
   * @param <T> type of values </T>
   */
  class Range<T>(@JvmField var min: T, @JvmField var max: T)

  /**
   * Return a shallow copy of randomization parameters.
   *
   * @return a shallow copy of randomization parameters.
   */
  fun copy(): KRandomParameters {
    val copy = KRandomParameters()
    copy.seed = this.seed
    copy.setObjectPoolSize(this.getObjectPoolSize())
    copy.setRandomizationDepth(this.getRandomizationDepth())
    copy.charset = this.charset
    copy.isScanClasspathForConcreteTypes = this.isScanClasspathForConcreteTypes
    copy.isOverrideDefaultInitialization = this.isOverrideDefaultInitialization
    copy.isIgnoreRandomizationErrors = this.isIgnoreRandomizationErrors
    copy.isBypassSetters = this.isBypassSetters
    copy.collectionSizeRange = this.collectionSizeRange
    copy.stringLengthRange = this.stringLengthRange
    copy.dateRange = this.dateRange
    copy.timeRange = this.timeRange
    copy.setExclusionPolicy(this.getExclusionPolicy())
    copy.setObjectFactory(this.getObjectFactory())
    copy.setRandomizerProvider(this.getRandomizerProvider())
    copy.customRandomizerRegistry = this.customRandomizerRegistry
    copy.exclusionRandomizerRegistry = this.exclusionRandomizerRegistry
    copy.userRegistries = this.userRegistries
    copy.fieldExclusionPredicates = this.fieldExclusionPredicates
    copy.typeExclusionPredicates = this.typeExclusionPredicates
    return copy
  }

  companion object {
    /** Default seed. */
    const val DEFAULT_SEED: Long = 123L

    /** Default charset for Strings. */
    val DEFAULT_CHARSET: Charset = StandardCharsets.US_ASCII

    /** Default collection size range. */
    val DEFAULT_COLLECTION_SIZE_RANGE: Range<Int> = Range(1, 100)

    /** Number of different objects to generate for a type. */
    const val DEFAULT_OBJECT_POOL_SIZE: Int = 10

    /** Default value for randomization depth, which means, that randomization depth is unlimited */
    const val DEFAULT_RANDOMIZATION_DEPTH: Int = Int.MAX_VALUE

    /** Default string length size. */
    val DEFAULT_STRING_LENGTH_RANGE: Range<Int> = Range(1, 32)

    /** Default date range in which dates will be generated: [now - 10 years, now + 10 years]. */
    const val DEFAULT_DATE_RANGE: Int = 10

    /** Reference date around which random dates will be generated. */
    private val REFERENCE_DATE: ZonedDateTime =
      ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"))

    /** Default dates range. */
    val DEFAULT_DATES_RANGE: Range<ZonedDateTime> =
      Range(
        REFERENCE_DATE.minusYears(DEFAULT_DATE_RANGE.toLong()),
        REFERENCE_DATE.plusYears(DEFAULT_DATE_RANGE.toLong()),
      )
  }
}
