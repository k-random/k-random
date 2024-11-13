package org.jeasy.random.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NegativeOrZero;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NegativeOrZeroAnnotationHandlerTest {
  private EasyRandom easyRandom;

  @BeforeEach
  void setUp() {
    easyRandom = new EasyRandom();
  }

  @Test
  void generatedBeanShouldBeValidAccordingToValidationConstraints() {
    byte byteValue = 0;
    short shortValue = 0;
    int intValue = 0;
    long longValue = 0L;
    float floatValue = 0F;

    TestBean testBean = easyRandom.nextObject(TestBean.class);

    assertThat(testBean.getTestBigDecimal()).isLessThanOrEqualTo(BigDecimal.ZERO);
    assertThat(testBean.getTestBigInteger()).isLessThanOrEqualTo(BigInteger.ZERO);
    assertThat(testBean.getTestPrimitiveByte()).isLessThanOrEqualTo(byteValue);
    assertThat(testBean.getTestWrapperByte()).isLessThanOrEqualTo(byteValue);
    assertThat(testBean.getTestPrimitiveShort()).isLessThanOrEqualTo(shortValue);
    assertThat(testBean.getTestWrapperShort()).isLessThanOrEqualTo(shortValue);
    assertThat(testBean.getTestPrimitiveInt()).isLessThanOrEqualTo(intValue);
    assertThat(testBean.getTestWrapperInteger()).isLessThanOrEqualTo(intValue);
    assertThat(testBean.getTestPrimitiveLong()).isLessThanOrEqualTo(longValue);
    assertThat(testBean.getTestWrapperLong()).isLessThanOrEqualTo(longValue);
    assertThat(testBean.getTestPrimitiveFloat()).isLessThanOrEqualTo(floatValue);
    assertThat(testBean.getTestWrapperFloat()).isLessThanOrEqualTo(floatValue);
    assertThat(testBean.getTestPrimitiveDouble()).isLessThanOrEqualTo(0D);
    assertThat(testBean.getTestWrapperDouble()).isLessThanOrEqualTo(0D);
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

    @NegativeOrZero private BigDecimal testBigDecimal;

    @NegativeOrZero private BigInteger testBigInteger;

    @NegativeOrZero private byte testPrimitiveByte;

    @NegativeOrZero private Byte testWrapperByte;

    @NegativeOrZero private short testPrimitiveShort;

    @NegativeOrZero private Short testWrapperShort;

    @NegativeOrZero private int testPrimitiveInt;

    @NegativeOrZero private Integer testWrapperInteger;

    @NegativeOrZero private long testPrimitiveLong;

    @NegativeOrZero private Long testWrapperLong;

    @NegativeOrZero private float testPrimitiveFloat;

    @NegativeOrZero private Float testWrapperFloat;

    @NegativeOrZero private double testPrimitiveDouble;

    @NegativeOrZero private Double testWrapperDouble;
  }
}
