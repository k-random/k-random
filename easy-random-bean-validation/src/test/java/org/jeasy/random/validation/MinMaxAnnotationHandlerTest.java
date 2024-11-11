package org.jeasy.random.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MinMaxAnnotationHandlerTest {
  private EasyRandom easyRandom;

  @BeforeEach
  void setUp() {
    easyRandom = new EasyRandom();
  }

  @Test
  void generatedBeanShouldBeValidAccordingToValidationConstraints() {
    // given
    short shortValue = 10;
    int intValue = 10;
    long longValue = 10L;

    // when
    TestBean testBean = easyRandom.nextObject(TestBean.class);

    // then
    assertThat(testBean.getTestMinBigDecimal()).isGreaterThanOrEqualTo(BigDecimal.TEN);
    assertThat(testBean.getTestMaxBigDecimal()).isLessThanOrEqualTo(BigDecimal.TEN);
    assertThat(testBean.getTestMinBigInteger()).isGreaterThanOrEqualTo(BigInteger.TEN);
    assertThat(testBean.getTestMaxBigInteger()).isLessThanOrEqualTo(BigInteger.TEN);
    assertThat(testBean.getTestMinPrimitiveShort()).isGreaterThanOrEqualTo(shortValue);
    assertThat(testBean.getTestMaxPrimitiveShort()).isLessThanOrEqualTo(shortValue);
    assertThat(testBean.getTestMinWrapperShort()).isGreaterThanOrEqualTo(shortValue);
    assertThat(testBean.getTestMaxWrapperShort()).isLessThanOrEqualTo(shortValue);
    assertThat(testBean.getTestMinPrimitiveInt()).isGreaterThanOrEqualTo(intValue);
    assertThat(testBean.getTestMaxPrimitiveInt()).isLessThanOrEqualTo(intValue);
    assertThat(testBean.getTestMinWrapperInt()).isGreaterThanOrEqualTo(intValue);
    assertThat(testBean.getTestMaxWrapperInt()).isLessThanOrEqualTo(intValue);
    assertThat(testBean.getTestMinPrimitiveLong()).isGreaterThanOrEqualTo(longValue);
    assertThat(testBean.getTestMaxPrimitiveLong()).isLessThanOrEqualTo(longValue);
    assertThat(testBean.getTestMinWrapperLong()).isGreaterThanOrEqualTo(longValue);
    assertThat(testBean.getTestMaxWrapperLong()).isLessThanOrEqualTo(longValue);
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
    @Min(10L)
    private BigDecimal testMinBigDecimal;

    @Max(10L)
    private BigDecimal testMaxBigDecimal;

    @Min(10L)
    private BigInteger testMinBigInteger;

    @Max(10L)
    private BigInteger testMaxBigInteger;

    @Min(10L)
    private short testMinPrimitiveShort;

    @Max(10L)
    private short testMaxPrimitiveShort;

    @Min(10L)
    private Short testMinWrapperShort;

    @Max(10L)
    private Short testMaxWrapperShort;

    @Min(10L)
    private int testMinPrimitiveInt;

    @Max(10L)
    private int testMaxPrimitiveInt;

    @Min(10L)
    private Integer testMinWrapperInt;

    @Max(10L)
    private Integer testMaxWrapperInt;

    @Min(10L)
    private long testMinPrimitiveLong;

    @Max(10L)
    private long testMaxPrimitiveLong;

    @Min(10L)
    private Long testMinWrapperLong;

    @Max(10L)
    private Long testMaxWrapperLong;

    public BigInteger getTestMinBigInteger() {
      return testMinBigInteger;
    }

    public BigInteger getTestMaxBigInteger() {
      return testMaxBigInteger;
    }

    public short getTestMinPrimitiveShort() {
      return testMinPrimitiveShort;
    }

    public short getTestMaxPrimitiveShort() {
      return testMaxPrimitiveShort;
    }

    public Short getTestMinWrapperShort() {
      return testMinWrapperShort;
    }

    public Short getTestMaxWrapperShort() {
      return testMaxWrapperShort;
    }

    public int getTestMinPrimitiveInt() {
      return testMinPrimitiveInt;
    }

    public int getTestMaxPrimitiveInt() {
      return testMaxPrimitiveInt;
    }

    public Integer getTestMinWrapperInt() {
      return testMinWrapperInt;
    }

    public Integer getTestMaxWrapperInt() {
      return testMaxWrapperInt;
    }

    public long getTestMinPrimitiveLong() {
      return testMinPrimitiveLong;
    }

    public long getTestMaxPrimitiveLong() {
      return testMaxPrimitiveLong;
    }

    public Long getTestMinWrapperLong() {
      return testMinWrapperLong;
    }

    public Long getTestMaxWrapperLong() {
      return testMaxWrapperLong;
    }

    public BigDecimal getTestMinBigDecimal() {
      return testMinBigDecimal;
    }

    public BigDecimal getTestMaxBigDecimal() {
      return testMaxBigDecimal;
    }
  }
}
