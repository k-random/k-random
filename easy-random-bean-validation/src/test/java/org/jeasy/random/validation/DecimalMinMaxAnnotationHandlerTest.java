package org.jeasy.random.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DecimalMinMaxAnnotationHandlerTest {
  private EasyRandom easyRandom;

  @BeforeEach
  void setUp() {
    easyRandom = new EasyRandom();
  }

  @Test
  void generatedBeanShouldBeValidAccordingToValidationConstraints() {
    BigDecimal bigDecimal = new BigDecimal("10.0");

    TestBean testBean = easyRandom.nextObject(TestBean.class);

    assertThat(testBean.getTestMinBigDecimal()).isGreaterThanOrEqualTo(bigDecimal);
    assertThat(testBean.getTestMaxBigDecimal()).isLessThanOrEqualTo(bigDecimal);
    assertThat(testBean.getTestMinBigInteger()).isGreaterThanOrEqualTo(BigInteger.TEN);
    assertThat(testBean.getTestMaxBigInteger()).isLessThanOrEqualTo(BigInteger.TEN);
    assertThat(new BigDecimal(testBean.getTestMinString())).isGreaterThanOrEqualTo(bigDecimal);
    assertThat(new BigDecimal(testBean.getTestMaxString())).isLessThanOrEqualTo(bigDecimal);
    assertThat(testBean.getTestMinPrimitiveByte()).isGreaterThanOrEqualTo(Byte.valueOf("10"));
    assertThat(testBean.getTestMaxPrimitiveByte()).isLessThanOrEqualTo(Byte.valueOf("10"));
    assertThat(testBean.getTestMinWrapperByte()).isGreaterThanOrEqualTo(Byte.valueOf("10"));
    assertThat(testBean.getTestMaxWrapperByte()).isLessThanOrEqualTo(Byte.valueOf("10"));
    assertThat(testBean.getTestMinPrimitiveShort()).isGreaterThanOrEqualTo(Short.valueOf("10"));
    assertThat(testBean.getTestMaxPrimitiveShort()).isLessThanOrEqualTo(Short.valueOf("10"));
    assertThat(testBean.getTestMinWrapperShort()).isGreaterThanOrEqualTo(Short.valueOf("10"));
    assertThat(testBean.getTestMaxWrapperShort()).isLessThanOrEqualTo(Short.valueOf("10"));
    assertThat(testBean.getTestMinPrimitiveInt()).isGreaterThanOrEqualTo(Integer.valueOf("10"));
    assertThat(testBean.getTestMaxPrimitiveInt()).isLessThanOrEqualTo(Integer.valueOf("10"));
    assertThat(testBean.getTestMinWrapperInt()).isGreaterThanOrEqualTo(Integer.valueOf("10"));
    assertThat(testBean.getTestMaxWrapperInt()).isLessThanOrEqualTo(Integer.valueOf("10"));
    assertThat(testBean.getTestMinPrimitiveLong()).isGreaterThanOrEqualTo(Long.valueOf("10"));
    assertThat(testBean.getTestMaxPrimitiveLong()).isLessThanOrEqualTo(Long.valueOf("10"));
    assertThat(testBean.getTestMinWrapperLong()).isGreaterThanOrEqualTo(Long.valueOf("10"));
    assertThat(testBean.getTestMaxWrapperLong()).isLessThanOrEqualTo(Long.valueOf("10"));
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
    @DecimalMin("10.0")
    private BigDecimal testMinBigDecimal;

    @DecimalMax("10.0")
    private BigDecimal testMaxBigDecimal;

    @DecimalMin("10.0")
    private BigInteger testMinBigInteger;

    @DecimalMax("10.0")
    private BigInteger testMaxBigInteger;

    @DecimalMin("10.0")
    private String testMinString;

    @DecimalMax("10.0")
    private String testMaxString;

    @DecimalMin("10.0")
    private byte testMinPrimitiveByte;

    @DecimalMax("10.0")
    private byte testMaxPrimitiveByte;

    @DecimalMin("10.0")
    private Byte testMinWrapperByte;

    @DecimalMax("10.0")
    private Byte testMaxWrapperByte;

    @DecimalMin("10.0")
    private short testMinPrimitiveShort;

    @DecimalMax("10.0")
    private short testMaxPrimitiveShort;

    @DecimalMin("10.0")
    private Short testMinWrapperShort;

    @DecimalMax("10.0")
    private Short testMaxWrapperShort;

    @DecimalMin("10.0")
    private int testMinPrimitiveInt;

    @DecimalMax("10.0")
    private int testMaxPrimitiveInt;

    @DecimalMin("10.0")
    private Integer testMinWrapperInt;

    @DecimalMax("10.0")
    private Integer testMaxWrapperInt;

    @DecimalMin("10.0")
    private long testMinPrimitiveLong;

    @DecimalMax("10.0")
    private long testMaxPrimitiveLong;

    @DecimalMin("10.0")
    private Long testMinWrapperLong;

    @DecimalMax("10.0")
    private Long testMaxWrapperLong;

    public BigInteger getTestMinBigInteger() {
      return testMinBigInteger;
    }

    public BigInteger getTestMaxBigInteger() {
      return testMaxBigInteger;
    }

    public String getTestMinString() {
      return testMinString;
    }

    public String getTestMaxString() {
      return testMaxString;
    }

    public byte getTestMinPrimitiveByte() {
      return testMinPrimitiveByte;
    }

    public byte getTestMaxPrimitiveByte() {
      return testMaxPrimitiveByte;
    }

    public Byte getTestMinWrapperByte() {
      return testMinWrapperByte;
    }

    public Byte getTestMaxWrapperByte() {
      return testMaxWrapperByte;
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
