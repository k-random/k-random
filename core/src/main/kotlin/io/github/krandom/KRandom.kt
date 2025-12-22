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

import io.github.krandom.api.ContextAwareRandomizer
import io.github.krandom.api.ExclusionPolicy
import io.github.krandom.api.ObjectFactory
import io.github.krandom.api.Randomizer
import io.github.krandom.api.RandomizerProvider
import io.github.krandom.api.RandomizerRegistry
import io.github.krandom.randomizers.misc.EnumRandomizer
import io.github.krandom.util.ReflectionUtils.getCanonicalConstructor
import io.github.krandom.util.ReflectionUtils.getDeclaredFields
import io.github.krandom.util.ReflectionUtils.getEmptyImplementationForCollectionInterface
import io.github.krandom.util.ReflectionUtils.getEmptyImplementationForMapInterface
import io.github.krandom.util.ReflectionUtils.getFieldValue
import io.github.krandom.util.ReflectionUtils.getInheritedFields
import io.github.krandom.util.ReflectionUtils.isArrayType
import io.github.krandom.util.ReflectionUtils.isCollectionType
import io.github.krandom.util.ReflectionUtils.isEnumType
import io.github.krandom.util.ReflectionUtils.isIntrospectable
import io.github.krandom.util.ReflectionUtils.isMapType
import io.github.krandom.util.ReflectionUtils.isPrimitiveFieldWithDefaultValue
import java.lang.reflect.Field
import java.util.Random
import java.util.ServiceLoader
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer
import java.util.function.Supplier

/**
 * Extension of [Random] that is able to generate random Java objects.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
@Suppress("UNCHECKED_CAST")
class KRandom
@JvmOverloads
constructor(kRandomParameters: KRandomParameters = KRandomParameters()) : Random() {
  private val parameters: KRandomParameters

  private val fieldPopulator: FieldPopulator

  private val arrayPopulator: ArrayPopulator

  private val enumRandomizersByType: MutableMap<Class<*>, EnumRandomizer<*>>

  private val randomizerProvider: RandomizerProvider

  private val objectFactory: ObjectFactory

  private val exclusionPolicy: ExclusionPolicy

  /**
   * Create a new [KRandom] instance.
   *
   * @param kRandomParameters randomization parameters
   */
  /** Create a new [KRandom] instance with default parameters. */
  init {
    super.setSeed(kRandomParameters.seed)
    val registries = setupRandomizerRegistries(kRandomParameters)
    randomizerProvider = kRandomParameters.getRandomizerProvider()
    randomizerProvider.setRandomizerRegistries(registries)
    objectFactory = kRandomParameters.getObjectFactory()
    arrayPopulator = ArrayPopulator(this)
    val collectionPopulator = CollectionPopulator(this)
    val mapPopulator = MapPopulator(this, objectFactory)
    val optionalPopulator = OptionalPopulator(this)
    enumRandomizersByType = ConcurrentHashMap()
    fieldPopulator =
      FieldPopulator(
        this,
        this.randomizerProvider,
        arrayPopulator,
        collectionPopulator,
        mapPopulator,
        optionalPopulator,
      )
    exclusionPolicy = kRandomParameters.getExclusionPolicy()
    parameters = kRandomParameters
  }

  /**
   * Generate a random instance of the given type.
   *
   * @param type the type for which an instance will be generated
   * @param <T> the actual type of the target object
   * @return a random instance of the given type
   * @throws ObjectCreationException when unable to create a new instance of the given type </T>
   */
  fun <T> nextObject(type: Class<T>): T? =
    if (type.isRecord) {
      createRandomRecord(type)
    } else {
      doPopulateBean(type, RandomizationContext(type, parameters))
    }

  /**
   * Generate a stream of random instances of the given type.
   *
   * @param type the type for which instances will be generated
   * @param size the number of instances to generate
   * @param <T> the actual type of the target objects
   * @return a list of random instances of the given type
   * @throws ObjectCreationException when unable to create a new instance of the given type </T>
   */
  fun <T> objects(type: Class<T>, size: Int): List<T> {
    require(size >= 0) { "The size must be positive" }
    return List(size) { nextObject(type) }.filterNotNull()
  }

  @Suppress("TooGenericExceptionCaught", "SpreadOperator")
  private fun <T> createRandomRecord(recordType: Class<T>): T? {
    // generate random values for record components
    val recordComponents = recordType.getRecordComponents()
    val randomValues: Array<Any?> = arrayOfNulls(recordComponents.size)
    for (i in recordComponents.indices) {
      randomValues[i] = this.nextObject(recordComponents[i].type as Class<Any>)
    }
    // create a random instance with random values
    try {
      return getCanonicalConstructor(recordType).newInstance(*randomValues)
    } catch (e: Exception) {
      throw ObjectCreationException(
        "Unable to create a random instance of recordType $recordType",
        e,
      )
    }
  }

  @Suppress("ReturnCount", "TooGenericExceptionCaught")
  fun <T> doPopulateBean(type: Class<T>, context: RandomizationContext): T? {
    if (exclusionPolicy.shouldBeExcluded(type, context)) {
      return null
    }

    val result: T?
    try {
      val randomizer: Randomizer<*>? = randomizerProvider.getRandomizerByType(type, context)
      if (randomizer != null) {
        if (randomizer is ContextAwareRandomizer<*>) {
          randomizer.setRandomizerContext(context)
        }
        return randomizer.getRandomValue() as T?
      }

      // Collection types are randomized without introspection for internal fields
      if (!isIntrospectable(type)) {
        return randomize(type, context)
      }

      // If the type has been already randomized, return one cached instance to avoid recursion.
      if (context.hasAlreadyRandomizedType(type)) {
        return context.getPopulatedBean(type) as T
      }

      // create a new instance of the target type
      result = objectFactory.createInstance(type, context)
      context.setRandomizedObject(result!!)

      // cache instance in the population context
      context.addPopulatedBean(type, result)

      // retrieve declared and inherited fields
      val fields = getDeclaredFields<T?>(result)
      // we cannot use type here, because with classpath scanning enabled the result can be a
      // subtype
      fields!!.addAll(getInheritedFields(result.javaClass))

      // inner classes (and static nested classes) have a field named "this$0" that references the
      // enclosing class.
      // This field should be excluded
      if (type.getEnclosingClass() != null) {
        fields.removeIf { field: Field -> field.name == "this$0" }
      }

      // populate fields with random data
      populateFields<T?>(fields, result, context)

      return result
    } catch (e: Throwable) {
      if (parameters.isIgnoreRandomizationErrors) {
        return null
      } else {
        throw ObjectCreationException("Unable to create a random instance of type $type", e)
      }
    }
  }

  private fun <T> randomize(type: Class<T>, context: RandomizationContext): T? {
    return when {
      isEnumType(type) -> {
        if (!enumRandomizersByType.containsKey(type)) {
          enumRandomizersByType[type] = createEnumRandomizer(type, parameters.seed)
        }
        enumRandomizersByType[type]!!.getRandomValue() as T?
      }

      isArrayType(type) -> {
        arrayPopulator.getRandomArray(type, context) as T
      }

      isCollectionType(type) -> {
        getEmptyImplementationForCollectionInterface(type) as T
      }

      isMapType(type) -> {
        getEmptyImplementationForMapInterface(type) as T
      }

      else -> null
    }
  }

  @Suppress("UNCHECKED_CAST")
  private fun createEnumRandomizer(type: Class<*>, seed: Long): EnumRandomizer<*> {
    return EnumRandomizer((type as Class<DummyEnum>).kotlin, seed)
  }

  private enum class DummyEnum

  @Throws(IllegalAccessException::class)
  private fun <T> populateFields(
    fields: MutableList<Field>,
    result: T?,
    context: RandomizationContext,
  ) {
    for (field in fields) {
      populateField<T?>(field, result, context)
    }
  }

  @Throws(IllegalAccessException::class)
  private fun <T> populateField(field: Field, result: T?, context: RandomizationContext) {
    if (exclusionPolicy.shouldBeExcluded(field, context)) {
      return
    }
    if (
      !parameters.isOverrideDefaultInitialization &&
        getFieldValue(result, field) != null &&
        !isPrimitiveFieldWithDefaultValue(result, field)
    ) {
      return
    }
    fieldPopulator.populateField(result!!, field, context)
  }

  private fun setupRandomizerRegistries(
    parameters: KRandomParameters
  ): LinkedHashSet<RandomizerRegistry> {
    val registries = LinkedHashSet<RandomizerRegistry>()
    registries.add(parameters.customRandomizerRegistry)
    registries.add(parameters.exclusionRandomizerRegistry)
    registries.addAll(parameters.userRegistries)
    registries.addAll(loadRegistries())
    registries.forEach(Consumer { registry: RandomizerRegistry? -> registry!!.init(parameters) })
    return registries
  }

  private fun loadRegistries(): MutableCollection<RandomizerRegistry> {
    return RegistryLoaderHolder.loadSafely()
  }

  /**
   * Helper to load [RandomizerRegistry] implementations safely.
   *
   * Some environments/agents may cause the default [ServiceLoader] discovery to re-enter while
   * providers are being initialized (for example if a provider triggers the creation of another
   * [KRandom] instance during static init). This helper guards against such re-entrance using a
   * [ThreadLocal] flag and also uses a stable class loader to avoid class path handler issues.
   */
  private object RegistryLoaderHolder {
    private val LOADING: ThreadLocal<Boolean> = ThreadLocal.withInitial<Boolean>(Supplier { false })

    @Suppress("TooGenericExceptionCaught")
    fun loadSafely(): MutableCollection<RandomizerRegistry> {
      if (LOADING.get() == true) {
        // Prevent infinite recursion if a provider initialization triggers another KRandom
        return mutableListOf()
      }
      LOADING.set(true)
      try {
        val registries: MutableList<RandomizerRegistry> = ArrayList()
        var cl = KRandom::class.java.getClassLoader()
        if (cl == null) {
          cl = ClassLoader.getSystemClassLoader()
        }
        try {
          val loader = ServiceLoader.load<RandomizerRegistry>(RandomizerRegistry::class.java, cl)
          for (registry in loader) {
            registries.add(registry)
          }
        } catch (_: Throwable) {
          // Fall back to manual defaults in case of agent/instrumentation issues
          registries.addAll(buildDefaultRegistries())
        }

        if (registries.isEmpty()) {
          registries.addAll(buildDefaultRegistries())
        }
        return registries
      } finally {
        LOADING.set(false)
      }
    }

    fun buildDefaultRegistries(): MutableCollection<RandomizerRegistry> {
      val defaults: MutableList<RandomizerRegistry> = ArrayList()
      // Core registries
      try {
        defaults.add(
          Class.forName(
              "io.github.krandom.randomizers.registry.InternalRandomizerRegistry",
              true,
              KRandom::class.java.getClassLoader(),
            )
            .getDeclaredConstructor()
            .newInstance() as RandomizerRegistry
        )
      } catch (_: Throwable) {}
      try {
        defaults.add(
          Class.forName(
              "io.github.krandom.randomizers.registry.TimeRandomizerRegistry",
              true,
              KRandom::class.java.getClassLoader(),
            )
            .getDeclaredConstructor()
            .newInstance() as RandomizerRegistry
        )
      } catch (_: Throwable) {}
      try {
        defaults.add(
          Class.forName(
              "io.github.krandom.randomizers.registry.AnnotationRandomizerRegistry",
              true,
              KRandom::class.java.getClassLoader(),
            )
            .getDeclaredConstructor()
            .newInstance() as RandomizerRegistry
        )
      } catch (_: Throwable) {}

      // Optional bean-validation registry if present on classpath
      try {
        val bv =
          Class.forName(
            "io.github.krandom.validation.BeanValidationRandomizerRegistry",
            true,
            KRandom::class.java.getClassLoader(),
          )
        val instance: Any = bv.getDeclaredConstructor().newInstance()
        if (instance is RandomizerRegistry) {
          defaults.add(instance)
        }
      } catch (_: Throwable) {}

      return defaults
    }
  }
}
