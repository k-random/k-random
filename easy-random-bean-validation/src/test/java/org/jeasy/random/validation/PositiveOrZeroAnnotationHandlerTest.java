package org.jeasy.random.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.PositiveOrZero;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PositiveOrZeroAnnotationHandlerTest {
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

        @PositiveOrZero
        private BigDecimal testBigDecimal;

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
