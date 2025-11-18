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
package io.github.krandom.util

import io.github.krandom.ObjectCreationException
import io.github.krandom.annotation.RandomizerArgument
import io.github.krandom.api.Randomizer
import io.github.krandom.util.ConversionUtils.convertArguments
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.InvocationHandler
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Proxy
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import java.lang.reflect.WildcardType
import java.util.*
import java.util.concurrent.*
import java.util.function.Supplier
import org.objenesis.ObjenesisStd

private const val BOOLEAN_IS_PREFIX: String = "is"
private const val BOOLEAN_IS_PREFIX_MIN_LENGTH: Int = 3

/**
 * Reflection utility methods.
 *
 * **This class is intended for internal use only. All public methods (except
 * [ ][ReflectionUtils.asRandomizer] might change between minor versions without notice.**
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
@Suppress("TooManyFunctions", "UNCHECKED_CAST")
object ReflectionUtils {
  private val PRIMITIVE_DEFAULT_VALUES: Map<Class<*>, Any> =
    mapOf(
      Boolean::class.javaPrimitiveType!! to false,
      Byte::class.javaPrimitiveType!! to 0.toByte(),
      Short::class.javaPrimitiveType!! to 0.toShort(),
      Int::class.javaPrimitiveType!! to 0,
      Long::class.javaPrimitiveType!! to 0L,
      Float::class.javaPrimitiveType!! to 0.0f,
      Double::class.javaPrimitiveType!! to 0.0,
      Char::class.javaPrimitiveType!! to '\u0000',
    )

  /**
   * Create a dynamic proxy that adapts the given [Supplier] to a [Randomizer].
   *
   * @param supplier to adapt
   * @param <T> target type
   * @return the proxy randomizer </T>
   */
  @JvmStatic
  fun <T> asRandomizer(supplier: Supplier<T?>): Randomizer<T?> {
    class RandomizerProxy(private val target: Supplier<*>) : InvocationHandler {
      @Throws(Throwable::class)
      override fun invoke(proxy: Any?, method: Method, args: Array<Any?>?): Any? {
        if ("getRandomValue" == method.name) {
          val getMethod = target.javaClass.getMethod("get")
          getMethod.setAccessible(true)
          return getMethod.invoke(target)
        }
        return null
      }
    }

    return Proxy.newProxyInstance(
      Randomizer::class.java.getClassLoader(),
      arrayOf<Class<*>>(Randomizer::class.java),
      RandomizerProxy(supplier),
    ) as Randomizer<T?>
  }

  /**
   * Get declared fields of a given type.
   *
   * @param type the type to introspect
   * @param <T> the actual type to introspect
   * @return list of declared fields </T>
   */
  @JvmStatic
  fun <T> getDeclaredFields(type: T): MutableList<Field>? =
    type?.javaClass?.getDeclaredFields()?.toMutableList()

  /**
   * Get inherited fields of a given type.
   *
   * @param type the type to introspect
   * @return list of inherited fields
   */
  @JvmStatic
  fun getInheritedFields(type: Class<*>): List<Field> {
    var type = type
    val inheritedFields = mutableListOf<Field>()
    while (type.getSuperclass() != null) {
      val superclass: Class<*> = type.getSuperclass()
      inheritedFields.addAll(superclass.getDeclaredFields())
      type = superclass
    }
    return inheritedFields
  }

  /**
   * Set a value in a field of a target object. If the target object provides a setter for the
   * field, this setter will be used. Otherwise, the field will be set using reflection.
   *
   * @param any instance to set the property on
   * @param field field to set the property on
   * @param value value to set
   * @throws IllegalAccessException if the property cannot be set
   */
  @JvmStatic
  @Throws(IllegalAccessException::class, InvocationTargetException::class)
  fun setProperty(any: Any?, field: Field, value: Any?) {
    try {
      val setter = getWriteMethod(field)
      if (setter.isPresent) {
        setter.get().invoke(any, value)
      } else {
        setFieldValue(any, field, value)
      }
    } catch (_: IllegalAccessException) {
      // otherwise, set field using reflection
      setFieldValue(any, field, value)
    }
  }

  /**
   * Set a value (accessible or not accessible) in a field of a target object.
   *
   * @param any instance to set the property on
   * @param field field to set the property on
   * @param value value to set
   * @throws IllegalAccessException if the property cannot be set
   */
  @JvmStatic
  @Throws(IllegalAccessException::class)
  fun setFieldValue(any: Any?, field: Field, value: Any?) {
    val access = field.trySetAccessible()
    field.set(any, value)
    field.setAccessible(access)
  }

  /**
   * Get the value (accessible or not accessible) of a field of a target object.
   *
   * @param any instance to get the field of
   * @param field field to get the value of
   * @return the value of the field
   * @throws IllegalAccessException if field cannot be accessed
   */
  @JvmStatic
  @Throws(IllegalAccessException::class)
  fun getFieldValue(any: Any?, field: Field): Any? {
    val access = field.trySetAccessible()
    val value = field.get(any)
    field.setAccessible(access)
    return value
  }

  /**
   * Get wrapper type of a primitive type.
   *
   * @param primitiveType to get its wrapper type
   * @return the wrapper type of the given primitive type
   */
  @JvmStatic
  fun getWrapperType(primitiveType: Class<*>?): Class<out Any>? =
    PrimitiveEnum.entries.firstOrNull { it.type == primitiveType }?.clazz ?: primitiveType

  /**
   * Check if a field has a primitive type and matching default value which is set by the compiler.
   *
   * @param any instance to get the field value of
   * @param field field to check
   * @return true if the field is primitive and is set to the default value, false otherwise
   * @throws IllegalAccessException if field cannot be accessed
   */
  @JvmStatic
  @Throws(IllegalAccessException::class)
  fun isPrimitiveFieldWithDefaultValue(any: Any?, field: Field): Boolean {
    if (!field.type.isPrimitive) {
      return false
    }
    val fieldValue = getFieldValue(any, field)
    return fieldValue == PRIMITIVE_DEFAULT_VALUES[field.type]
  }

  /**
   * Check if a field is static.
   *
   * @param field the field to check
   * @return true if the field is static, false otherwise
   */
  @JvmStatic fun isStatic(field: Field): Boolean = Modifier.isStatic(field.modifiers)

  /**
   * Check if a type is an interface.
   *
   * @param type the type to check
   * @return true if the type is an interface, false otherwise
   */
  @JvmStatic fun isInterface(type: Class<*>): Boolean = type.isInterface

  /**
   * Check if the type is abstract (either an interface or an abstract class).
   *
   * @param type the type to check
   * @param <T> the actual type to check
   * @return true if the type is abstract, false otherwise </T>
   */
  @JvmStatic fun <T> isAbstract(type: Class<T?>): Boolean = Modifier.isAbstract(type.modifiers)

  /**
   * Check if the type is public.
   *
   * @param type the type to check
   * @param <T> the actual type to check
   * @return true if the type is public, false otherwise </T>
   */
  @JvmStatic fun <T> isPublic(type: Class<T?>): Boolean = Modifier.isPublic(type.modifiers)

  /**
   * Check if a type is an array type.
   *
   * @param type the type to check.
   * @return true if the type is an array type, false otherwise.
   */
  @JvmStatic fun isArrayType(type: Class<*>): Boolean = type.isArray

  /**
   * Check if a type is an enum type.
   *
   * @param type the type to check.
   * @return true if the type is an enum type, false otherwise.
   */
  @JvmStatic fun isEnumType(type: Class<*>): Boolean = type.isEnum

  /**
   * Check if a type is a collection type.
   *
   * @param type the type to check.
   * @return true if the type is a collection type, false otherwise
   */
  @JvmStatic
  fun isCollectionType(type: Class<*>): Boolean =
    MutableCollection::class.java.isAssignableFrom(type)

  /**
   * Check if a type is a collection type.
   *
   * @param type the type to check.
   * @return true if the type is a collection type, false otherwise
   */
  @JvmStatic
  fun isCollectionType(type: Type?): Boolean =
    isParameterizedType(type) &&
      ReflectionUtils.isCollectionType((type as ParameterizedType).rawType as Class<*>)

  /**
   * Check if a type is populatable.
   *
   * @param type the type to check
   * @return true if the type is populatable, false otherwise
   */
  @JvmStatic
  fun isPopulatable(type: Type?): Boolean =
    !isWildcardType(type) &&
      !isTypeVariable(type) &&
      !isCollectionType(type) &&
      !isParameterizedType(type)

  /**
   * Check if a type should be introspected for internal fields.
   *
   * @param type the type to check
   * @return true if the type should be introspected, false otherwise
   */
  @JvmStatic
  fun isIntrospectable(type: Class<*>): Boolean =
    !isEnumType(type) &&
      !isArrayType(type) &&
      !(ReflectionUtils.isCollectionType(type) && isJdkBuiltIn(type)) &&
      !(isMapType(type) && isJdkBuiltIn(type))

  /**
   * Check if a type is a map type.
   *
   * @param type the type to check
   * @return true if the type is a map type, false otherwise.
   */
  @JvmStatic fun isMapType(type: Class<*>): Boolean = MutableMap::class.java.isAssignableFrom(type)

  /**
   * Check if a type is [Optional].
   *
   * @param type the type to check
   * @return true if the type is [Optional], false otherwise.
   */
  @JvmStatic
  fun isOptionalType(type: Class<*>): Boolean = Optional::class.java.isAssignableFrom(type)

  /**
   * Check if a type is a JDK built-in collection/map.
   *
   * @param type the type to check
   * @return true if the type is a built-in collection/map type, false otherwise.
   */
  @JvmStatic fun isJdkBuiltIn(type: Class<*>): Boolean = type.name.startsWith("java.util")

  /**
   * Check if a type is a parameterized type
   *
   * @param type the type to check
   * @return true if the type is parameterized, false otherwise
   */
  @JvmStatic
  fun isParameterizedType(type: Type?): Boolean {
    return type is ParameterizedType && type.actualTypeArguments.size > 0
  }

  /**
   * Check if a type is a wildcard type
   *
   * @param type the type to check
   * @return true if the type is a wildcard type, false otherwise
   */
  @JvmStatic
  fun isWildcardType(type: Type?): Boolean {
    return type is WildcardType
  }

  /**
   * Check if a type is a type variable
   *
   * @param type the type to check
   * @return true if the type is a type variable, false otherwise
   */
  @JvmStatic
  fun isTypeVariable(type: Type?): Boolean {
    return type is TypeVariable<*>
  }

  /**
   * Searches the classpath for all public concrete subtypes of the given interface or abstract
   * class.
   *
   * @param type to search concrete subtypes of
   * @param <T> the actual type to introspect
   * @return a list of all concrete subtypes found </T>
   */
  @JvmStatic
  fun <T> getPublicConcreteSubTypesOf(type: Class<T?>): List<Class<*>?> {
    return ClassGraphFacade.getPublicConcreteSubTypesOf(type)
  }

  /**
   * Filters a list of types to keep only elements having the same parameterized types as the given
   * type.
   *
   * @param type the type to use for the search
   * @param types a list of types to filter
   * @return a list of types having the same parameterized types as the given type
   */
  @JvmStatic
  fun filterSameParameterizedTypes(types: List<Class<*>>, type: Type): List<Class<*>> {
    if (type is ParameterizedType) {
      val fieldArugmentTypes = type.getActualTypeArguments()
      val typesWithSameParameterizedTypes: MutableList<Class<*>> = ArrayList<Class<*>>()
      for (currentConcreteType in types) {
        val actualTypeArguments = getActualTypeArgumentsOfGenericInterfaces(currentConcreteType)
        typesWithSameParameterizedTypes.addAll(
          actualTypeArguments
            .filter { currentTypeArguments: Array<Type> ->
              fieldArugmentTypes.contentEquals(currentTypeArguments)
            }
            .map { _: Array<Type> -> currentConcreteType }
            .toList()
        )
      }
      return typesWithSameParameterizedTypes
    }
    return types
  }

  /**
   * Looks for given annotationType on given field or read method for field.
   *
   * @param field field to check
   * @param annotationType Type of annotation you're looking for.
   * @param <T> the actual type of annotation
   * @return given annotation if field or read method has this annotation or null. </T>
   */
  @JvmStatic
  fun <T : Annotation?> getAnnotation(field: Field, annotationType: Class<T?>): T? {
    return if (field.getAnnotation<T?>(annotationType) == null)
      getAnnotationFromReadMethod<T?>(getReadMethod(field).orElse(null), annotationType)
    else field.getAnnotation<T?>(annotationType)
  }

  /**
   * Checks if field or corresponding read method is annotated with given annotationType.
   *
   * @param field Field to check
   * @param annotationType Annotation you're looking for.
   * @return true if field or read method it annotated with given annotationType or false.
   */
  @JvmStatic
  fun isAnnotationPresent(field: Field, annotationType: Class<out Annotation?>): Boolean {
    val readMethod = getReadMethod(field)
    return field.isAnnotationPresent(annotationType) ||
      readMethod.isPresent && readMethod.get().isAnnotationPresent(annotationType)
  }

  /**
   * Return an empty implementation for a [Collection] type.
   *
   * @param collectionInterface for which an empty implementation should be returned
   * @return empty implementation for the collection interface
   */
  @JvmStatic
  fun getEmptyImplementationForCollectionInterface(
    collectionInterface: Class<*>
  ): MutableCollection<*> {
    var collection: MutableCollection<*> = ArrayList<Any?>()
    if (MutableList::class.java.isAssignableFrom(collectionInterface)) {
      collection = ArrayList<Any?>()
    } else if (NavigableSet::class.java.isAssignableFrom(collectionInterface)) {
      collection = TreeSet<Any?>()
    } else if (SortedSet::class.java.isAssignableFrom(collectionInterface)) {
      collection = TreeSet<Any?>()
    } else if (MutableSet::class.java.isAssignableFrom(collectionInterface)) {
      collection = HashSet<Any?>()
    } else if (BlockingDeque::class.java.isAssignableFrom(collectionInterface)) {
      collection = LinkedBlockingDeque<Any>()
    } else if (Deque::class.java.isAssignableFrom(collectionInterface)) {
      collection = ArrayDeque<Any?>()
    } else if (TransferQueue::class.java.isAssignableFrom(collectionInterface)) {
      collection = LinkedTransferQueue<Any>()
    } else if (BlockingQueue::class.java.isAssignableFrom(collectionInterface)) {
      collection = LinkedBlockingQueue<Any>()
    } else if (Queue::class.java.isAssignableFrom(collectionInterface)) {
      collection = LinkedList<Any?>()
    }
    return collection
  }

  /**
   * Create an empty collection for the given type.
   *
   * @param fieldType for which an empty collection should we created
   * @param initialSize initial size of the collection
   * @return empty collection
   */
  @JvmStatic
  fun createEmptyCollectionForType(fieldType: Class<*>, initialSize: Int): MutableCollection<*>? {
    rejectUnsupportedTypes(fieldType)
    return try {
      fieldType.getDeclaredConstructor().newInstance() as MutableCollection<*>
    } catch (_: ReflectiveOperationException) {
      if (fieldType == ArrayBlockingQueue::class.java) {
        ArrayBlockingQueue<Any>(initialSize)
      } else {
        ObjenesisStd().newInstance(fieldType) as MutableCollection<*>?
      }
    }
  }

  /**
   * Returns an empty, mutable map implementation for the given map interface.
   *
   * @param mapInterface The class of the map interface for which to create an empty implementation.
   * @return A new empty [MutableMap] instance compatible with the given interface.
   */
  @JvmStatic
  fun getEmptyImplementationForMapInterface(mapInterface: Class<*>): MutableMap<*, *> =
    when {
      ConcurrentNavigableMap::class.java.isAssignableFrom(mapInterface) ->
        ConcurrentSkipListMap<Any, Any>()
      ConcurrentMap::class.java.isAssignableFrom(mapInterface) -> ConcurrentHashMap<Any, Any>()
      SortedMap::class.java.isAssignableFrom(mapInterface) -> TreeMap<Any?, Any?>()
      else -> HashMap<Any?, Any?>()
    }

  private fun rejectUnsupportedTypes(type: Class<*>) {
    if (type == SynchronousQueue::class.java) {
      // SynchronousQueue is not supported since it requires a consuming thread at insertion time
      throw UnsupportedOperationException(
        SynchronousQueue::class.java.getName() + " type is not supported"
      )
    }
    if (type == DelayQueue::class.java) {
      // DelayQueue is not supported since it requires creating dummy delayed objects
      throw UnsupportedOperationException(
        DelayQueue::class.java.getName() + " type is not supported"
      )
    }
  }

  /**
   * Get the write method for given field.
   *
   * @param field field to get the write method for
   * @return Optional of write method or empty if field has no write method
   */
  @JvmStatic
  fun getWriteMethod(field: Field): Optional<Method> {
    return getPublicMethod("set" + capitalize(field.name), field.declaringClass, field.type)
  }

  /**
   * Get the read method for given field.
   *
   * @param field field to get the read method for.
   * @return Optional of read method or empty if field has no read method
   */
  @JvmStatic
  fun getReadMethod(field: Field): Optional<Method> {
    // Detekt: keep return count low by computing a single Optional and returning once
    val fieldName = field.name
    val fieldClass = field.declaringClass
    val capitalizedFieldName = capitalize(fieldName)
    // try to find getProperty
    var result = getPublicMethod("get$capitalizedFieldName", fieldClass)
    // try to find isProperty for boolean properties
    if (result.isEmpty) {
      result = getPublicMethod("is$capitalizedFieldName", fieldClass)
    }
    // Special case: boolean properties whose name already starts with "is",
    // e.g., field name is "isActive" with getter method "isActive()".
    // In this case, the previous attempts tried getIsActive()/isIsActive(),
    // but the actual getter is the field name itself. Try that as a last resort.
    if (
      result.isEmpty &&
        fieldName.startsWith(BOOLEAN_IS_PREFIX) &&
        fieldName.length >= BOOLEAN_IS_PREFIX_MIN_LENGTH
    ) {
      result = getPublicMethod(fieldName, fieldClass)
    }
    return result
  }

  /**
   * Gets the canonical constructor of a record.
   *
   * @param T the generic type of the record.
   * @param recordType the class of the record.
   * @return the canonical constructor of the record.
   * @throws IllegalArgumentException if the given type is not a record class.
   */
  @Suppress("SpreadOperator")
  @JvmStatic
  fun <T> getCanonicalConstructor(recordType: Class<T>): Constructor<T> {
    val recordComponents =
      recordType.recordComponents
        ?: throw IllegalArgumentException("Class ${recordType.name} is not a record type")

    // recordComponents are ordered, see javadoc:
    // "The components are returned in the same order that they are declared in the record header"
    val componentTypes = recordComponents.map { it.type }.toTypedArray()
    return try {
      recordType.getDeclaredConstructor(*componentTypes)
    } catch (e: NoSuchMethodException) {
      // should not happen, from Record javadoc:
      // "A record class has the following mandated members: a public canonical constructor,
      // whose descriptor is the same as the record descriptor;"
      throw ObjectCreationException("Invalid record definition for ${recordType.name}", e)
    }
  }

  private fun capitalize(propertyName: String): String {
    return propertyName.substring(0, 1).uppercase(Locale.ENGLISH) + propertyName.substring(1)
  }

  private fun getPublicMethod(
    name: String,
    target: Class<*>,
    vararg parameterTypes: Class<*>?,
  ): Optional<Method> {
    return try {
      Optional.of(target.getMethod(name, *parameterTypes))
    } catch (_: Exception) {
      Optional.empty<Method>()
    }
  }

  private fun <T : Annotation?> getAnnotationFromReadMethod(
    readMethod: Method?,
    clazz: Class<T?>,
  ): T? = readMethod?.getAnnotation<T?>(clazz)

  private fun getActualTypeArgumentsOfGenericInterfaces(type: Class<*>): List<Array<Type>> =
    type.genericInterfaces.filterIsInstance<ParameterizedType>().map { it.actualTypeArguments }

  /**
   * Creates a new instance of the specified type using the provided constructor arguments, if any.
   * If no arguments are provided or if no matching constructor is found, the default constructor of
   * the type is used to create the instance.
   *
   * @param T the type of the instance to be created
   * @param type the class of the type to instantiate
   * @param randomizerArguments an array of arguments to pass to the constructor of the specified
   *   type
   * @return a new instance of the specified type wrapped in a [Randomizer]
   * @throws ObjectCreationException if the instance could not be created due to any
   *   reflection-related issues
   */
  @Suppress("SpreadOperator")
  @JvmStatic
  fun <T> newInstance(
    type: Class<T>,
    randomizerArguments: Array<RandomizerArgument>,
  ): Randomizer<T> {
    try {
      if (randomizerArguments.isNotEmpty()) {
        type.constructors
          .firstOrNull { constructor ->
            hasSameArgumentNumber(constructor, randomizerArguments) &&
              hasSameArgumentTypes(constructor, randomizerArguments)
          }
          ?.let { matchingConstructor ->
            val initArgs = convertArguments(randomizerArguments)
            return matchingConstructor.newInstance(*initArgs) as Randomizer<T>
          }
      }
      return type.getDeclaredConstructor().newInstance() as Randomizer<T>
    } catch (e: ReflectiveOperationException) {
      val contentToString = randomizerArguments.contentToString()
      throw ObjectCreationException(
        "Could not create Randomizer of type: $type with constructor arguments: $contentToString",
        e,
      )
    }
  }

  private fun hasSameArgumentNumber(
    constructor: Constructor<*>,
    randomizerArguments: Array<RandomizerArgument>,
  ): Boolean {
    return constructor.parameterCount == randomizerArguments.size
  }

  private fun hasSameArgumentTypes(
    constructor: Constructor<*>,
    randomizerArguments: Array<RandomizerArgument>,
  ): Boolean =
    constructor.parameterTypes.zip(randomizerArguments).all { (paramType, randomizerArg) ->
      paramType.isAssignableFrom(randomizerArg.type.java)
    }
}
