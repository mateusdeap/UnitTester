package com.example.unittester.intervaladjacencydetector;

import com.example.unittester.intervaloverlapdetector.Interval;

public class IntervalAdjacencyDetector {

    public boolean detectAdjacency(Interval interval1, Interval interval2) {
        return interval1.getEnd() == interval2.getStart() || interval1.getStart() == interval2.getEnd();
    }
}
