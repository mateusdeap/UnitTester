package com.example.unittester.stringreverser;

import com.example.unittester.stringreverser.StringReverser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringReverserTest {
    
    StringReverser stringReverser;
    
    @Before
    public void setUp() throws Exception {
        stringReverser = new StringReverser();
    }

    @Test
    public void givenAnEmptyString_reverse_returnsEmptyString() {
        String reversedString = stringReverser.reverse("");

        assertEquals("", reversedString);
    }

    @Test
    public void givenASingleCharacter_reverse_returnsSameString() {
        String reversedString = stringReverser.reverse("a");

        assertEquals("a", reversedString);
    }

    @Test
    public void givenSomeString_reverse_returnsTheStringReversed() {
        String reversedString = stringReverser.reverse("Mateus Pereira");

        assertEquals("ariereP suetaM", reversedString);
    }
}