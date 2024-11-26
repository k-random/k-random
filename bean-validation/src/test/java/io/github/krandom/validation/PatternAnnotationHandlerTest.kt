package io.github.krandom.validation;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.krandom.KRandom;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Pattern;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PatternAnnotationHandlerTest {
  private KRandom kRandom;

  @BeforeEach
  void setUp() {
    kRandom = new KRandom();
  }

  @Test
  void generatedBeanShouldBeValidAccordingToValidationConstraints() {
    TestBean testBean = kRandom.nextObject(TestBean.class);

    assertThat(testBean.getTestString()).matches("^A$");
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
    @Pattern(regexp = "^A$")
    private String testString;

    public String getTestString() {
      return testString;
    }
  }
}
