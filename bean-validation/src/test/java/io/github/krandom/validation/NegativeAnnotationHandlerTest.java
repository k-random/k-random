package io.github.krandom.validation;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.krandom.KRandom;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Negative;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NegativeAnnotationHandlerTest {
  private KRandom kRandom;

  @BeforeEach
  void setUp() {
    kRandom = new KRandom();
  }

  @Test
  void generatedBeanShouldBeValidAccordingToValidationConstraints() {
    byte byteValue = 0;
    short shortValue = 0;
    int intValue = 0;
    long longValue = 0L;
    float floatValue = 0F;

    TestBean testBean = kRandom.nextObject(TestBean.class);

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
    TestBean testBean = kRandom.nextObject(TestBean.class);

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
