package org.jeasy.random.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AssertTrueAnnotationHandlerTest {

  private EasyRandom easyRandom;

  @BeforeEach
  void setUp() {
    easyRandom = new EasyRandom();
  }

  @Test
  void generatedBeanShouldBeValidAccordingToValidationConstraints() {
    TestBean testBean = easyRandom.nextObject(TestBean.class);

    assertThat(testBean.isTestPrimitiveBoolean()).isTrue();
    assertThat(testBean.getTestWrapperBoolean()).isTrue();
  }

  @Test
  void generatedBeanShouldBeValidUsingBeanValidationApi() {
    TestBean testBean = easyRandom.nextObject(TestBean.class);

    Validator validator;
    try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      validator = validatorFactory.getValidator();
    }
    Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);

    assertThat(violations).isEmpty();
  }

  static class TestBean {
    @AssertTrue private boolean testPrimitiveBoolean;

    public boolean isTestPrimitiveBoolean() {
      return testPrimitiveBoolean;
    }

    @AssertTrue private Boolean testWrapperBoolean;

    public Boolean getTestWrapperBoolean() {
      return testWrapperBoolean;
    }
  }
}
