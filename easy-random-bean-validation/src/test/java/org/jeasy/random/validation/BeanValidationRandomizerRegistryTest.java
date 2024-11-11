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
    // given
    underTest.init(easyRandomParameters);

    // when
    Map<Class<? extends Annotation>, BeanValidationAnnotationHandler> map =
        underTest.getAnnotationHandlers();

    // then
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
