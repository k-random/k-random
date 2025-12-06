package io.github.krandom.validation

import io.github.krandom.KRandomParameters
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.maps.shouldContainKey
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import jakarta.validation.constraints.AssertFalse
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Negative
import jakarta.validation.constraints.NegativeOrZero
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Null
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import kotlin.reflect.KClass
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@ExtendWith(MockKExtension::class)
internal class BeanValidationRandomizerRegistryTest {
  private val underTest = BeanValidationRandomizerRegistry()
  @MockK lateinit var kRandomParameters: KRandomParameters

  @BeforeEach
  fun setUp() {
    every { kRandomParameters.seed } returns 100L
  }

  @ParameterizedTest
  @MethodSource("provideArguments")
  fun mapContainsExpectedKeyValuePairs(
    annotation: KClass<out Annotation>,
    annotationHandler: KClass<*>,
  ) {
    underTest.init(kRandomParameters)

    val map = underTest.getAnnotationHandlers()

    map shouldContainKey annotation
    annotationHandler.isInstance(map[annotation]).shouldBeTrue()
  }

  companion object {
    @JvmStatic
    @Suppress("unused")
    private fun provideArguments(): List<Arguments> {
      return listOf(
        // keep-sorted start
        Arguments.of(AssertFalse::class, AssertFalseAnnotationHandler::class),
        Arguments.of(AssertTrue::class, AssertTrueAnnotationHandler::class),
        Arguments.of(DecimalMax::class, DecimalMinMaxAnnotationHandler::class),
        Arguments.of(DecimalMin::class, DecimalMinMaxAnnotationHandler::class),
        Arguments.of(Email::class, EmailAnnotationHandler::class),
        Arguments.of(Future::class, FutureAnnotationHandler::class),
        Arguments.of(FutureOrPresent::class, FutureOrPresentAnnotationHandler::class),
        Arguments.of(Max::class, MinMaxAnnotationHandler::class),
        Arguments.of(Min::class, MinMaxAnnotationHandler::class),
        Arguments.of(Negative::class, NegativeAnnotationHandler::class),
        Arguments.of(NegativeOrZero::class, NegativeOrZeroAnnotationHandler::class),
        Arguments.of(NotBlank::class, NotBlankAnnotationHandler::class),
        Arguments.of(Null::class, NullAnnotationHandler::class),
        Arguments.of(Past::class, PastAnnotationHandler::class),
        Arguments.of(PastOrPresent::class, PastOrPresentAnnotationHandler::class),
        Arguments.of(Pattern::class, PatternAnnotationHandler::class),
        Arguments.of(Positive::class, PositiveAnnotationHandler::class),
        Arguments.of(PositiveOrZero::class, PositiveOrZeroAnnotationHandler::class),
        Arguments.of(Size::class, SizeAnnotationHandler::class),
        // keep-sorted end
      )
    }
  }
}
