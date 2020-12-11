package com.example.unittester.negativenumbervalidator;

import com.example.unittester.negativenumbervalidator.NegativeNumberValidator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NegativeNumberValidatorTest {

    private NegativeNumberValidator negativeNumberValidator;

    @Before
    public void setup() {
        negativeNumberValidator = new NegativeNumberValidator();
    }

    @Test
    public void givenANegativeNumber_isNegative_ReturnsTrue() {
        int number = -2;
        boolean result = negativeNumberValidator.isNegative(number);

        assertTrue(result);
    }

    @Test
    public void givenAPositiveNumber_isNegative_ReturnsFalse() {
        int number = 2;
        boolean result = negativeNumberValidator.isNegative(number);

        assertFalse(result);
    }

    @Test
    public void givenZero_isNegative_ReturnsTrue() {
        int number = 0;
        boolean result = negativeNumberValidator.isNegative(number);

        assertTrue(result);
    }
}