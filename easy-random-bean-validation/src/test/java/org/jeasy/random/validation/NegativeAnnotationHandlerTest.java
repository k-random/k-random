package org.jeasy.random.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Negative;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NegativeAnnotationHandlerTest {
  private EasyRandom easyRandom;

  @BeforeEach
  void setUp() {
    easyRandom = new EasyRandom();
  }

  @Test
  void generatedBeanShouldBeValidAccordingToValidationConstraints() {
    // given
    byte byteValue = 0;
    short shortValue = 0;
    int intValue = 0;
    long longValue = 0L;
    float floatValue = 0F;

    // when
    TestBean testBean = easyRandom.nextObject(TestBean.class);

    // then
    assertThat(testBean.getTestBigDecimal()).isLessThan(BigDecimal.ZERO);
    assertThat(testBean.getTestBigInteger()).isLessThan(BigInteger.ZERO);
    assertThat(testBean.getTestPrimitiveByte()).isLessThan(byteValue);
    assertThat(testBean.getTestWrapperByte()).isLessThan(byteValue);
    assertThat(testBean.getTestPrimitiveShort()).isLessThan(shortValue);
    assertThat(testBean.getTestWrapperShort()).isLessThan(shortValue);
    assertThat(testBean.getTestPrimitiveInt()).isLessThan(intValue);
    assertThat(testBean.getTestWrapperInteger()).isLessThan(intValue);
    assertThat(testBean.getTestPrimitiveLong()).isLessThan(longValue);
    assertThat(testBean.getTestWrapperLong()).isLessThan(longValue);
    assertThat(testBean.getTestPrimitiveFloat()).isLessThan(floatValue);
    assertThat(testBean.getTestWrapperFloat()).isLessThan(floatValue);
    assertThat(testBean.getTestPrimitiveDouble()).isLessThan(0D);
    assertThat(testBean.getTestWrapperDouble()).isLessThan(0D);
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
    public BigDecimal getTestBigDecimal() {
      return testBigDecimal;
    }

    public BigInteger getTestBigInteger() {
      return testBigInteger;
    }

    public byte getTestPrimitiveByte() {
      return testPrimitiveByte;
    }

    public Byte getTestWrapperByte() {
      return testWrapperByte;
    }

    public short getTestPrimitiveShort() {
      return testPrimitiveShort;
    }

    public Short getTestWrapperShort() {
      return testWrapperShort;
    }

    public int getTestPrimitiveInt() {
      return testPrimitiveInt;
    }

    public Integer getTestWrapperInteger() {
      return testWrapperInteger;
    }

    public long getTestPrimitiveLong() {
      return testPrimitiveLong;
    }

    public Long getTestWrapperLong() {
      return testWrapperLong;
    }

    public float getTestPrimitiveFloat() {
      return testPrimitiveFloat;
    }

    public Float getTestWrapperFloat() {
      return testWrapperFloat;
    }

    public double getTestPrimitiveDouble() {
      return testPrimitiveDouble;
    }

    public Double getTestWrapperDouble() {
      return testWrapperDouble;
    }

    @Negative private BigDecimal testBigDecimal;

    @Negative private BigInteger testBigInteger;

    @Negative private byte testPrimitiveByte;

    @Negative private Byte testWrapperByte;

    @Negative private short testPrimitiveShort;

    @Negative private Short testWrapperShort;

    @Negative private int testPrimitiveInt;

    @Negative private Integer testWrapperInteger;

    @Negative private long testPrimitiveLong;

    @Negative private Long testWrapperLong;

    @Negative private float testPrimitiveFloat;

    @Negative private Float testWrapperFloat;

    @Negative private double testPrimitiveDouble;

    @Negative private Double testWrapperDouble;
  }
}
