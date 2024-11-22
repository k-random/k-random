package io.github.krandom.validation;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.krandom.KRandom;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Email;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailAnnotationHandlerTest {

  private KRandom kRandom;

  @BeforeEach
  void setUp() {
    kRandom = new KRandom();
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
    public TestBean(String testField) {
      this.testField = testField;
    }

    public String getTestField() {
      return testField;
    }

    public void setTestField(final String testField) {
      this.testField = testField;
    }

    @Email private String testField;
  }
}
