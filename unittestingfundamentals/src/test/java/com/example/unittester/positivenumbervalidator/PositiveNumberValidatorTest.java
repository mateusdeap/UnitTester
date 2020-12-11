package com.example.unittester.positivenumbervalidator;

import com.example.unittester.positivenumbervalidator.PositiveNumberValidator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PositiveNumberValidatorTest {

    PositiveNumberValidator numberValidator;

    @Before
    public void setup() {
        numberValidator = new PositiveNumberValidator();
    }

    @Test
    public void givenAPositiveNumber_isPositive_ReturnsTrue() {
        int number = 5;
        boolean result = numberValidator.isPositive(number);
        assertTrue(result);
    }

    @Test
    public void givenANegativeNumber_isPositive_ReturnsFalse() {
        int number = -1;
        boolean result = numberValidator.isPositive(number);
        assertFalse(result);
    }

    @Test
    public void givenZero_isPositive_ReturnsFalse() {
        int number = 0;
        boolean result = numberValidator.isPositive(number);
        assertFalse(result);
    }
}