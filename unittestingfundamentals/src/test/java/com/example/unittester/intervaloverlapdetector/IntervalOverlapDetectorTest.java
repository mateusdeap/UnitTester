package com.example.unittester.intervaloverlapdetector;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IntervalOverlapDetectorTest {

    private IntervalOverlapDetector intervalOverlapDetector;

    @Before
    public void setup() {
        intervalOverlapDetector = new IntervalOverlapDetector();
    }

    // interval1 is before interval2
    @Test
    public void givenInterval1IsBeforeInterval2_detectOverlap_returnsFalse() {
        Interval interval1 = new Interval(0, 5);
        Interval interval2 = new Interval(8, 12);

        boolean overlaps = intervalOverlapDetector.detectOverlap(interval1, interval2);
        assertFalse(overlaps);
    }

    // interval1 overlaps interval2 on start
    @Test
    public void givenInterval1OverlapsInterval2OnStart_detectOverlap_returnsTrue() {
        Interval interval1 = new Interval(0, 9);
        Interval interval2 = new Interval(8, 12);

        boolean overlaps = intervalOverlapDetector.detectOverlap(interval1, interval2);
        assertTrue(overlaps);
    }

    // interval1 is contained within interval2
    @Test
    public void givenInterval1IsContainedWithinInterval2_detectOverlap_returnsTrue() {
        Interval interval1 = new Interval(9, 11);
        Interval interval2 = new Interval(8, 12);

        boolean overlaps = intervalOverlapDetector.detectOverlap(interval1, interval2);
        assertTrue(overlaps);
    }

    // interval1 contains interval2
    @Test
    public void givenInterval1ContainsInterval2_detectOverlap_returnsTrue() {
        Interval interval1 = new Interval(5, 15);
        Interval interval2 = new Interval(8, 12);

        boolean overlaps = intervalOverlapDetector.detectOverlap(interval1, interval2);
        assertTrue(overlaps);
    }

    // interval1 overlaps interval2 on end
    @Test
    public void givenInterval1OverlapsInterval2OnEnd_detectOverlap_returnsTrue() {
        Interval interval1 = new Interval(10, 15);
        Interval interval2 = new Interval(8, 12);

        boolean overlaps = intervalOverlapDetector.detectOverlap(interval1, interval2);
        assertTrue(overlaps);
    }

    // interval1 is after interval2
    @Test
    public void givenInterval1IsAfterInterval2_detectOverlap_returnsFalse() {
        Interval interval1 = new Interval(13, 15);
        Interval interval2 = new Interval(8, 12);

        boolean overlaps = intervalOverlapDetector.detectOverlap(interval1, interval2);
        assertFalse(overlaps);
    }

    // interval1 is adjacent to interval2 from the left
    @Test
    public void givenInterval1IsLeftAdjacentToInterval2_detectOverlap_returnsFalse() {
        Interval interval1 = new Interval(5, 8);
        Interval interval2 = new Interval(8, 12);

        boolean overlaps = intervalOverlapDetector.detectOverlap(interval1, interval2);
        assertFalse(overlaps);
    }

    // interval1 is adjacent to interval2 from the right
    @Test
    public void givenInterval1IsRightAdjacentToInterval2_detectOverlap_returnsFalse() {
        Interval interval1 = new Interval(12, 15);
        Interval interval2 = new Interval(8, 12);

        boolean overlaps = intervalOverlapDetector.detectOverlap(interval1, interval2);
        assertFalse(overlaps);
    }

    // interval1 is the same as interval2
    @Test
    public void givenInterval1IsTheSameAsInterval2_detectOverlap_returnsTrue() {
        Interval interval1 = new Interval(12, 15);
        Interval interval2 = new Interval(12, 15);

        boolean overlaps = intervalOverlapDetector.detectOverlap(interval1, interval2);
        assertTrue(overlaps);
    }
}