package edu.nyu.pqs.stopwatch.impl;

import edu.nyu.pqs.stopwatch.api.Stopwatch;
import org.junit.Assert;

public class SharedStopWatchTest {

    private Stopwatch stopwatch;

    @org.junit.Before
    public void setUp() throws Exception {
        stopwatch = StopwatchFactory.getStopwatch(
                "ID " + System.currentTimeMillis());
    }

    @org.junit.Test
    public void startNormally() {
        stopwatch.start();
        Assert.assertEquals(0, stopwatch.getLapTimes().size());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void startingRunningWatch() {
        stopwatch.start();
        stopwatch.start();
    }

    @org.junit.Test
    public void test_lap() {
    stopwatch.start();
    stopwatch.lap();
        stopwatch.lap();
        stopwatch.lap();
        Assert.assertEquals(3, stopwatch.getLapTimes().size());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void lapOnStopwatch_ThatsNotStarted() {
        stopwatch.lap();
    }

    @org.junit.Test
    public void test_stop(){
        stopwatch.start();
        stopwatch.lap();
        stopwatch.stop();
        Assert.assertEquals(2, stopwatch.getLapTimes().size());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void stopOnStopwatch_ThatsNotStarted() {
        stopwatch.stop();
    }

    @org.junit.Test
    public void test_reset() {
        stopwatch.start();
        stopwatch.lap();
        stopwatch.reset();
        Assert.assertEquals(0, stopwatch.getLapTimes().size());
    }
}