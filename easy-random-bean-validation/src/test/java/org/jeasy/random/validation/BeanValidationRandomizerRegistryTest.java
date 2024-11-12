package org.jeasy.random.validation;

import jakarta.validation.constraints.*;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeanValidationRandomizerRegistryTest {
  private BeanValidationRandomizerRegistry underTest = new BeanValidationRandomizerRegistry();
  @Mock private EasyRandomParameters easyRandomParameters;

  @BeforeEach
  void setUp() {
    when(easyRandomParameters.getSeed()).thenReturn(100L);
  }

  @ParameterizedTest
  @MethodSource("provideArguments")
  void mapContainsExpectedKeyValuePairs(
      Class<? extends Annotation> annotation, Class<?> annotationHandler) {
    underTest.init(easyRandomParameters);

    Map<Class<? extends Annotation>, BeanValidationAnnotationHandler> map =
        underTest.getAnnotationHandlers();

    assertThat(map).containsKey(annotation);
    assertThat(map.get(annotation)).isInstanceOf(annotationHandler);
  }

  private static Stream<Arguments> provideArguments() {
    return Stream.of(
        Arguments.of(AssertFalse.class, AssertFalseAnnotationHandler.class),
        Arguments.of(AssertTrue.class, AssertTrueAnnotationHandler.class),
        Arguments.of(Null.class, NullAnnotationHandler.class),
        Arguments.of(Future.class, FutureAnnotationHandler.class),
        Arguments.of(FutureOrPresent.class, FutureOrPresentAnnotationHandler.class),
        Arguments.of(Past.class, PastAnnotationHandler.class),
        Arguments.of(PastOrPresent.class, PastOrPresentAnnotationHandler.class),
        Arguments.of(Min.class, MinMaxAnnotationHandler.class),
        Arguments.of(Max.class, MinMaxAnnotationHandler.class),
        Arguments.of(DecimalMin.class, DecimalMinMaxAnnotationHandler.class),
        Arguments.of(DecimalMax.class, DecimalMinMaxAnnotationHandler.class),
        Arguments.of(Pattern.class, PatternAnnotationHandler.class),
        Arguments.of(Size.class, SizeAnnotationHandler.class),
        Arguments.of(Positive.class, PositiveAnnotationHandler.class),
        Arguments.of(PositiveOrZero.class, PositiveOrZeroAnnotationHandler.class),
        Arguments.of(Negative.class, NegativeAnnotationHandler.class),
        Arguments.of(NegativeOrZero.class, NegativeOrZeroAnnotationHandler.class),
        Arguments.of(NotBlank.class, NotBlankAnnotationHandler.class),
        Arguments.of(Email.class, EmailAnnotationHandler.class));
  }
}
