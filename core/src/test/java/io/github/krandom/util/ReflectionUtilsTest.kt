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

import io.github.krandom.beans.Bar
import io.github.krandom.beans.ChainedSetterBean
import io.github.krandom.beans.CustomList
import io.github.krandom.beans.CustomMap
import io.github.krandom.beans.Foo
import io.github.krandom.beans.Gender
import io.github.krandom.beans.Human
import io.github.krandom.beans.Mammal
import io.github.krandom.beans.SocialPerson
import io.github.krandom.beans.Street
import io.github.krandom.util.ReflectionUtils.createEmptyCollectionForType
import io.github.krandom.util.ReflectionUtils.getAnnotation
import io.github.krandom.util.ReflectionUtils.getDeclaredFields
import io.github.krandom.util.ReflectionUtils.getEmptyImplementationForCollectionInterface
import io.github.krandom.util.ReflectionUtils.getEmptyImplementationForMapInterface
import io.github.krandom.util.ReflectionUtils.getInheritedFields
import io.github.krandom.util.ReflectionUtils.getReadMethod
import io.github.krandom.util.ReflectionUtils.getWrapperType
import io.github.krandom.util.ReflectionUtils.isAbstract
import io.github.krandom.util.ReflectionUtils.isAnnotationPresent
import io.github.krandom.util.ReflectionUtils.isArrayType
import io.github.krandom.util.ReflectionUtils.isCollectionType
import io.github.krandom.util.ReflectionUtils.isEnumType
import io.github.krandom.util.ReflectionUtils.isInterface
import io.github.krandom.util.ReflectionUtils.isJdkBuiltIn
import io.github.krandom.util.ReflectionUtils.isMapType
import io.github.krandom.util.ReflectionUtils.isPrimitiveFieldWithDefaultValue
import io.github.krandom.util.ReflectionUtils.isPublic
import io.github.krandom.util.ReflectionUtils.isStatic
import io.github.krandom.util.ReflectionUtils.setProperty
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.spyk
import io.mockk.verify
import java.lang.Boolean
import java.lang.Byte
import java.lang.Double
import java.lang.Float
import java.lang.Long
import java.lang.Short
import java.lang.reflect.Method
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.DelayQueue
import java.util.concurrent.SynchronousQueue
import kotlin.Char
import kotlin.Exception
import kotlin.Int
import kotlin.IntArray
import kotlin.String
import kotlin.Suppress
import kotlin.Throws
import kotlin.UnsupportedOperationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@Suppress("UNCHECKED_CAST")
internal class ReflectionUtilsTest {
  @Test
  fun `test get declared fields`() {
    val javaVersion = BigDecimal(System.getProperty("java.specification.version"))
    val expectedSize =
      when {
        javaVersion >= BigDecimal("12") -> 21
        javaVersion >= BigDecimal("9") -> 22
        else -> 20
      }
    getDeclaredFields(Street::class.java)?.size shouldBe expectedSize
  }

  @Test
  fun `test get inherited fields`() {
    getInheritedFields(SocialPerson::class.java) shouldHaveSize 11
  }

  @Test
  @Throws(Exception::class)
  fun `test is static`() {
    isStatic(Human::class.java.getField("SERIAL_VERSION_UID")) shouldBe true
  }

  @Test
  fun `test is interface`() {
    isInterface(List::class.java) shouldBe true
    isInterface(Mammal::class.java) shouldBe true
    isInterface(Human::class.java) shouldBe false
    isInterface(ArrayList::class.java) shouldBe false
  }

  @Test
  fun `test is abstract`() {
    isAbstract(Foo::class.java as Class<Foo?>) shouldBe false
    isAbstract(Bar::class.java as Class<Bar?>) shouldBe true
  }

  @Test
  fun `test is public`() {
    isPublic<Foo?>(Foo::class.java as Class<Foo?>) shouldBe true
    isPublic<Dummy?>(Dummy::class.java as Class<Dummy?>) shouldBe false
  }

  @Test
  fun `test is array type`() {
    isArrayType(IntArray::class.java) shouldBe true
    isArrayType(Foo::class.java) shouldBe false
  }

  @Test
  fun `test is enum type`() {
    isEnumType(Gender::class.java) shouldBe true
    isEnumType(Foo::class.java) shouldBe false
  }

  @Test
  fun `test is collection type`() {
    isCollectionType(CustomList::class.java) shouldBe true
    isCollectionType(Foo::class.java) shouldBe false
  }

  @Test
  fun `test is map type`() {
    isMapType(CustomMap::class.java) shouldBe true
    isMapType(Foo::class.java) shouldBe false
  }

  @Test
  fun `test is jdk built in`() {
    isJdkBuiltIn(ArrayList::class.java) shouldBe true
    isJdkBuiltIn(CustomList::class.java) shouldBe false
  }

  @Test
  fun `get wrapper type test`() {
    getWrapperType(Byte.TYPE) shouldBe Byte::class.java
    getWrapperType(Short.TYPE) shouldBe Short::class.java
    getWrapperType(Integer.TYPE) shouldBe Integer::class.java
    getWrapperType(Long.TYPE) shouldBe Long::class.java
    getWrapperType(Double.TYPE) shouldBe Double::class.java
    getWrapperType(Float.TYPE) shouldBe Float::class.java
    getWrapperType(Boolean.TYPE) shouldBe Boolean::class.java
    getWrapperType(Character.TYPE) shouldBe Character::class.java
    getWrapperType(String::class.java) shouldBe String::class.java
  }

  @Test
  @Throws(Exception::class)
  fun `test is primitive field with default value`() {
    val defaultValueClass = PrimitiveFieldsWithDefaultValuesBean::class
    val defaultValueBean = PrimitiveFieldsWithDefaultValuesBean()
    val boolField =
      defaultValueClass.java.getDeclaredField(PrimitiveFieldsWithDefaultValuesBean::bool.name)
    val bField =
      defaultValueClass.java.getDeclaredField(PrimitiveFieldsWithDefaultValuesBean::b.name)
    val sField =
      defaultValueClass.java.getDeclaredField(PrimitiveFieldsWithDefaultValuesBean::s.name)
    val iField =
      defaultValueClass.java.getDeclaredField(PrimitiveFieldsWithDefaultValuesBean::i.name)
    val lField =
      defaultValueClass.java.getDeclaredField(PrimitiveFieldsWithDefaultValuesBean::l.name)
    val fField =
      defaultValueClass.java.getDeclaredField(PrimitiveFieldsWithDefaultValuesBean::f.name)
    val dField =
      defaultValueClass.java.getDeclaredField(PrimitiveFieldsWithDefaultValuesBean::d.name)
    val cField =
      defaultValueClass.java.getDeclaredField(PrimitiveFieldsWithDefaultValuesBean::c.name)

    isPrimitiveFieldWithDefaultValue(defaultValueBean, boolField) shouldBe true
    isPrimitiveFieldWithDefaultValue(defaultValueBean, bField) shouldBe true
    isPrimitiveFieldWithDefaultValue(defaultValueBean, sField) shouldBe true
    isPrimitiveFieldWithDefaultValue(defaultValueBean, iField) shouldBe true
    isPrimitiveFieldWithDefaultValue(defaultValueBean, lField) shouldBe true
    isPrimitiveFieldWithDefaultValue(defaultValueBean, fField) shouldBe true
    isPrimitiveFieldWithDefaultValue(defaultValueBean, dField) shouldBe true
    isPrimitiveFieldWithDefaultValue(defaultValueBean, cField) shouldBe true
  }

  @Test
  fun `test is primitive field with non default value`() {
    val nonDefaultValueClass = PrimitiveFieldsWithNonDefaultValuesBean::class
    val nonDefaultValueBean = PrimitiveFieldsWithNonDefaultValuesBean()
    val nonDefaultBoolField =
      nonDefaultValueClass.java.getDeclaredField(PrimitiveFieldsWithNonDefaultValuesBean::bool.name)
    val nonDefaultBField =
      nonDefaultValueClass.java.getDeclaredField(PrimitiveFieldsWithNonDefaultValuesBean::b.name)
    val nonDefaultSField =
      nonDefaultValueClass.java.getDeclaredField(PrimitiveFieldsWithNonDefaultValuesBean::s.name)
    val nonDefaultIField =
      nonDefaultValueClass.java.getDeclaredField(PrimitiveFieldsWithNonDefaultValuesBean::i.name)
    val nonDefaultLField =
      nonDefaultValueClass.java.getDeclaredField(PrimitiveFieldsWithNonDefaultValuesBean::l.name)
    val nonDefaultFField =
      nonDefaultValueClass.java.getDeclaredField(PrimitiveFieldsWithNonDefaultValuesBean::f.name)
    val nonDefaultDField =
      nonDefaultValueClass.java.getDeclaredField(PrimitiveFieldsWithNonDefaultValuesBean::d.name)
    val nonDefaultCField =
      nonDefaultValueClass.java.getDeclaredField(PrimitiveFieldsWithNonDefaultValuesBean::c.name)

    isPrimitiveFieldWithDefaultValue(nonDefaultValueBean, nonDefaultBoolField) shouldBe false
    isPrimitiveFieldWithDefaultValue(nonDefaultValueBean, nonDefaultBField) shouldBe false
    isPrimitiveFieldWithDefaultValue(nonDefaultValueBean, nonDefaultSField) shouldBe false
    isPrimitiveFieldWithDefaultValue(nonDefaultValueBean, nonDefaultIField) shouldBe false
    isPrimitiveFieldWithDefaultValue(nonDefaultValueBean, nonDefaultLField) shouldBe false
    isPrimitiveFieldWithDefaultValue(nonDefaultValueBean, nonDefaultFField) shouldBe false
    isPrimitiveFieldWithDefaultValue(nonDefaultValueBean, nonDefaultDField) shouldBe false
    isPrimitiveFieldWithDefaultValue(nonDefaultValueBean, nonDefaultCField) shouldBe false
  }

  @Test
  @Throws(NoSuchFieldException::class)
  fun `test get read method`() {
    getReadMethod(PrimitiveFieldsWithDefaultValuesBean::class.java.getDeclaredField("b"))
      .isEmpty() shouldBe true
    val readMethod: Optional<Method> = getReadMethod(Foo::class.java.getDeclaredField("bar"))
    readMethod.isPresent shouldBe true
    readMethod.get().name shouldBe "getBar"
  }

  @ParameterizedTest
  @MethodSource("provideFieldNames")
  @Throws(NoSuchFieldException::class)
  fun `test get annotation with annotation`(fieldName: String) {
    val field = AnnotatedBean::class.java.getDeclaredField(fieldName)
    getAnnotation<NotNull?>(field, NotNull::class.java as Class<NotNull?>)
      .shouldBeInstanceOf<NotNull>()
  }

  @Test
  @Throws(NoSuchFieldException::class)
  fun `test get annotation without annotation`() {
    val field = AnnotatedBean::class.java.getDeclaredField("noAnnotation")
    getAnnotation(field, NotNull::class.java as Class<NotNull?>).shouldBeNull()
  }

  @ParameterizedTest
  @MethodSource("provideFieldNames")
  @Throws(NoSuchFieldException::class)
  fun `test is annotation present true`(fieldName: String) {
    val field = AnnotatedBean::class.java.getDeclaredField(fieldName)
    isAnnotationPresent(field, NotNull::class.java) shouldBe true
  }

  @Test
  @Throws(NoSuchFieldException::class)
  fun `test is annotation present false`() {
    val field = AnnotatedBean::class.java.getDeclaredField("noAnnotation")
    isAnnotationPresent(field, NotNull::class.java) shouldBe false
  }

  @Test
  fun `test get empty implementation for collection interface`() {
    val collection = getEmptyImplementationForCollectionInterface(MutableList::class.java)

    collection.shouldBeInstanceOf<ArrayList<*>>()
    collection.shouldBeEmpty()
  }

  @Test
  fun `create empty collection for array blocking queue`() {
    val collection = createEmptyCollectionForType(ArrayBlockingQueue::class.java, INITIAL_CAPACITY)

    collection.shouldBeInstanceOf<ArrayBlockingQueue<*>>()
    collection.shouldBeEmpty()
    collection.remainingCapacity() shouldBe INITIAL_CAPACITY
  }

  @Test
  fun `synchronous queue should be rejected`() {
    shouldThrow<UnsupportedOperationException> {
      createEmptyCollectionForType(SynchronousQueue::class.java, INITIAL_CAPACITY)
    }
  }

  @Test
  fun `delay queue should be rejected`() {
    shouldThrow<UnsupportedOperationException> {
      createEmptyCollectionForType(DelayQueue::class.java, INITIAL_CAPACITY)
    }
  }

  @Test
  fun `get empty implementation for map interface`() {
    val map = getEmptyImplementationForMapInterface(SortedMap::class.java)

    map.shouldBeInstanceOf<TreeMap<*, *>>()
    map.isEmpty() shouldBe true
  }

  @Test
  @Throws(Exception::class)
  fun `set property fluent bean`() {
    // given
    val chainedSetterBean = spyk<ChainedSetterBean>()
    val nameField = ChainedSetterBean::class.java.getDeclaredField("name")

    // when
    setProperty(chainedSetterBean, nameField, "myName")

    // then
    verify { chainedSetterBean.setName("myName") }
    chainedSetterBean.name shouldBe "myName"
  }

  @Test
  @Throws(Exception::class)
  fun `set property fluent bean primitive type`() {
    // given
    val chainedSetterBean = spyk<ChainedSetterBean>()
    val indexField = ChainedSetterBean::class.java.getDeclaredField("index")

    // when
    setProperty(chainedSetterBean, indexField, 100)

    // then
    verify { chainedSetterBean.setIndex(100) }
    chainedSetterBean.index shouldBe 100
  }

  @Suppress("unused")
  private class PrimitiveFieldsWithDefaultValuesBean {
    var bool: kotlin.Boolean = false
    @JvmField var b: kotlin.Byte = 0
    var s: kotlin.Short = 0
    var i: Int = 0
    var l: kotlin.Long = 0
    var f: kotlin.Float = 0f
    var d: kotlin.Double = 0.0
    var c: Char = 0.toChar()
  }

  @Suppress("unused")
  private class PrimitiveFieldsWithNonDefaultValuesBean {
    var bool: kotlin.Boolean = true
    var b: kotlin.Byte = 1.toByte()
    var s: kotlin.Short = 1.toShort()
    var i: Int = 1
    var l: kotlin.Long = 1L
    var f: kotlin.Float = 1.0f
    var d: kotlin.Double = 1.0
    var c: Char = 'a'
  }

  @Suppress("unused")
  class AnnotatedBean {
    @NotNull var fieldAnnotation: String? = null
    @get:NotNull var methodAnnotation: String? = null
    var noAnnotation: String? = null
  }

  private class Dummy

  @Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FIELD,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.VALUE_PARAMETER,
  )
  @Retention(AnnotationRetention.RUNTIME)
  @MustBeDocumented
  annotation class NotNull

  companion object {
    private const val INITIAL_CAPACITY = 10

    @JvmStatic
    @Suppress("unused")
    private fun provideFieldNames(): List<Arguments> =
      listOf(Arguments.of("fieldAnnotation"), Arguments.of("methodAnnotation"))
  }
}
