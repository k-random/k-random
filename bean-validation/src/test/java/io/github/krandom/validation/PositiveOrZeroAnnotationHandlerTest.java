package io.github.krandom.validation;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.krandom.KRandom;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PositiveOrZeroAnnotationHandlerTest {
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

    assertThat(testBean.getTestBigDecimal()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
    assertThat(testBean.getTestBigInteger()).isGreaterThanOrEqualTo(BigInteger.ZERO);
    assertThat(testBean.getTestPrimitiveByte()).isGreaterThanOrEqualTo(byteValue);
    assertThat(testBean.getTestWrapperByte()).isGreaterThanOrEqualTo(byteValue);
    assertThat(testBean.getTestPrimitiveShort()).isGreaterThanOrEqualTo(shortValue);
    assertThat(testBean.getTestWrapperShort()).isGreaterThanOrEqualTo(shortValue);
    assertThat(testBean.getTestPrimitiveInt()).isGreaterThanOrEqualTo(intValue);
    assertThat(testBean.getTestWrapperInteger()).isGreaterThanOrEqualTo(intValue);
    assertThat(testBean.getTestPrimitiveLong()).isGreaterThanOrEqualTo(longValue);
    assertThat(testBean.getTestWrapperLong()).isGreaterThanOrEqualTo(longValue);
    assertThat(testBean.getTestPrimitiveFloat()).isGreaterThanOrEqualTo(floatValue);
    assertThat(testBean.getTestWrapperFloat()).isGreaterThanOrEqualTo(floatValue);
    assertThat(testBean.getTestPrimitiveDouble()).isGreaterThanOrEqualTo(0D);
    assertThat(testBean.getTestWrapperDouble()).isGreaterThanOrEqualTo(0D);
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

    @PositiveOrZero private BigDecimal testBigDecimal;

    @PositiveOrZero private BigInteger testBigInteger;

    @PositiveOrZero private byte testPrimitiveByte;

    @PositiveOrZero private Byte testWrapperByte;

    @PositiveOrZero private short testPrimitiveShort;

    @PositiveOrZero private Short testWrapperShort;

    @PositiveOrZero private int testPrimitiveInt;

    @PositiveOrZero private Integer testWrapperInteger;

    @PositiveOrZero private long testPrimitiveLong;

    @PositiveOrZero private Long testWrapperLong;

    @PositiveOrZero private float testPrimitiveFloat;

    @PositiveOrZero private Float testWrapperFloat;

    @PositiveOrZero private double testPrimitiveDouble;

    @PositiveOrZero private Double testWrapperDouble;
  }
}
