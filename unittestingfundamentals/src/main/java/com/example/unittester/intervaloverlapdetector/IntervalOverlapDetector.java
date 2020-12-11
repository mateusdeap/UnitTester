package com.example.unittester.intervaloverlapdetector;

public class IntervalOverlapDetector {

    public boolean detectOverlap(Interval interval1, Interval interval2) {
        return (interval1.getEnd() > interval2.getStart() && interval1.getStart() < interval2.getStart()) ||
                (interval1.getStart() < interval2.getEnd() && interval1.getEnd() > interval2.getEnd()) ||
                (interval1.getEnd() <= interval2.getEnd() && interval1.getStart() >= interval2.getStart()) ||
                (interval1.getEnd() >= interval2.getEnd() && interval1.getStart() <= interval2.getStart());
    }
}
