package io.github.krandom.validation;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.krandom.KRandom;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.AssertTrue;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssertTrueAnnotationHandlerTest {

  private KRandom kRandom;

  @BeforeEach
  void setUp() {
    kRandom = new KRandom();
  }

  @Test
  void generatedBeanShouldBeValidAccordingToValidationConstraints() {
    // given
    // when
    TestBean testBean = kRandom.nextObject(TestBean.class);

    // then
    assertThat(testBean.isTestPrimitiveBoolean()).isTrue();
    assertThat(testBean.getTestWrapperBoolean()).isTrue();
  }

  @Test
  void generatedBeanShouldBeValidUsingBeanValidationApi() {
    TestBean testBean = kRandom.nextObject(TestBean.class);

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
