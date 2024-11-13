package org.jeasy.random.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Size;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SizeAnnotationHandlerTest {
  private EasyRandom easyRandom;

  @BeforeEach
  void setUp() {
    easyRandom = new EasyRandom();
  }

  @Test
  void generatedBeanShouldBeValidAccordingToValidationConstraints() {
    TestBean testBean = easyRandom.nextObject(TestBean.class);

    assertThat(testBean.getTestString()).hasSizeBetween(1, 10);
    assertThat(testBean.getTestList()).hasSizeBetween(1, 10);
    assertThat(testBean.getTestArray()).hasSizeBetween(1, 10);
    assertThat(testBean.getTestMap()).hasSizeBetween(1, 10);
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
    @Size(min = 1, max = 10)
    private String testString;

    @Size(min = 1, max = 10)
    private List<String> testList;

    @Size(min = 1, max = 10)
    private String[] testArray;

    @Size(min = 1, max = 10)
    private Map<String, String> testMap;

    public Map<String, String> getTestMap() {
      return testMap;
    }

    public String[] getTestArray() {
      return testArray;
    }

    public List<String> getTestList() {
      return testList;
    }

    public String getTestString() {
      return testString;
    }
  }
}
