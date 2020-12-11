package com.example.unittester.stringduplicator;

import com.example.unittester.stringduplicator.StringDuplicator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringDuplicatorTest {

    private StringDuplicator stringDuplicator;

    @Before
    public void setUp() throws Exception {
        stringDuplicator = new StringDuplicator();
    }

    @Test
    public void givenAnEmptyString_duplicate_returnsAnEmptyString() {
        String duplicatedString = stringDuplicator.duplicate("");

        assertEquals("", duplicatedString);
    }

    @Test
    public void givenASingleCharacter_duplicate_returnsTheSameCharacterDuplicated() {
        String duplicatedString = stringDuplicator.duplicate("a");

        assertEquals("aa", duplicatedString);
    }

    @Test
    public void givenSomeString_duplicate_returnsTheSameStringDuplicated() {
        String duplicatedString = stringDuplicator.duplicate("Some Random String");

        assertEquals("Some Random StringSome Random String", duplicatedString);
    }
}