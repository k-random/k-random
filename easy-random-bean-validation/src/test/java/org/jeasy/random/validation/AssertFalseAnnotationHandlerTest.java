package org.jeasy.random.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.AssertFalse;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AssertFalseAnnotationHandlerTest {

  private EasyRandom easyRandom;

  @BeforeEach
  void setUp() {
    easyRandom = new EasyRandom();
  }

  @Test
  void generatedBeanShouldBeValidAccordingToValidationConstraints() {
    // given
    // when
    TestBean testBean = easyRandom.nextObject(TestBean.class);

    // then
    assertThat(testBean.isTestPrimitiveBoolean()).isFalse();
    assertThat(testBean.getTestWrapperBoolean()).isFalse();
  }

  @Test
  void generatedBeanShouldBeValidUsingBeanValidationApi() {
    // given
    // when
    TestBean testBean = easyRandom.nextObject(TestBean.class);

    Validator validator;
    try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      validator = validatorFactory.getValidator();
    }
    Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);

    // then
    assertThat(violations).isEmpty();
  }

  static class TestBean {
    @AssertFalse private boolean testPrimitiveBoolean;

    public boolean isTestPrimitiveBoolean() {
      return testPrimitiveBoolean;
    }

    @AssertFalse private Boolean testWrapperBoolean;

    public Boolean getTestWrapperBoolean() {
      return testWrapperBoolean;
    }
  }
}
