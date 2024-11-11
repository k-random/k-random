package org.jeasy.random.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Positive;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PositiveAnnotationHandlerTest {
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
    PositiveAnnotationHandlerTest.TestBean testBean =
        easyRandom.nextObject(PositiveAnnotationHandlerTest.TestBean.class);

    // then
    assertThat(testBean.getTestBigDecimal()).isGreaterThan(BigDecimal.ZERO);
    assertThat(testBean.getTestBigInteger()).isGreaterThan(BigInteger.ZERO);
    assertThat(testBean.getTestPrimitiveByte()).isGreaterThan(byteValue);
    assertThat(testBean.getTestWrapperByte()).isGreaterThan(byteValue);
    assertThat(testBean.getTestPrimitiveShort()).isGreaterThan(shortValue);
    assertThat(testBean.getTestWrapperShort()).isGreaterThan(shortValue);
    assertThat(testBean.getTestPrimitiveInt()).isGreaterThan(intValue);
    assertThat(testBean.getTestWrapperInteger()).isGreaterThan(intValue);
    assertThat(testBean.getTestPrimitiveLong()).isGreaterThan(longValue);
    assertThat(testBean.getTestWrapperLong()).isGreaterThan(longValue);
    assertThat(testBean.getTestPrimitiveFloat()).isGreaterThan(floatValue);
    assertThat(testBean.getTestWrapperFloat()).isGreaterThan(floatValue);
    assertThat(testBean.getTestPrimitiveDouble()).isGreaterThan(0D);
    assertThat(testBean.getTestWrapperDouble()).isGreaterThan(0D);
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

    @Positive private BigDecimal testBigDecimal;

    @Positive private BigInteger testBigInteger;

    @Positive private byte testPrimitiveByte;

    @Positive private Byte testWrapperByte;

    @Positive private short testPrimitiveShort;

    @Positive private Short testWrapperShort;

    @Positive private int testPrimitiveInt;

    @Positive private Integer testWrapperInteger;

    @Positive private long testPrimitiveLong;

    @Positive private Long testWrapperLong;

    @Positive private float testPrimitiveFloat;

    @Positive private Float testWrapperFloat;

    @Positive private double testPrimitiveDouble;

    @Positive private Double testWrapperDouble;
  }
}
