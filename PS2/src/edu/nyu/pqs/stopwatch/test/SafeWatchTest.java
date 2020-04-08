package edu.nyu.pqs.stopwatch.test;

import edu.nyu.pqs.stopwatch.api.Stopwatch;
import edu.nyu.pqs.stopwatch.impl.StopwatchFactory;
import org.junit.Assert;

public class SafeWatchTest {

    private Stopwatch stopwatch;
    @org.junit.Before
    public void setUp() throws Exception {
       stopwatch = StopwatchFactory.getStopwatch(
                "ID " + Thread.currentThread().getId());
    }

    @org.junit.Test
    public void start() {
        stopwatch.start();
        Assert.assertEquals(0, stopwatch.getLapTimes().size());
    }

    @org.junit.Test
    public void tenLapsOnStopwatch() {
        stopwatch.start();
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
            stopwatch.lap();
        }
        Assert.assertEquals(10, stopwatch.getLapTimes().size());
        System.out.println(stopwatch);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void lapOnStopwatch_ThatsNotStarted(){
        stopwatch.lap();
    }

    @org.junit.Test
    public void stop() {
        stopwatch.start();
        tenLapsOnStopwatch();
        stopwatch.stop();
        Assert.assertEquals(11, stopwatch.getLapTimes().size());
    }

    @org.junit.Test
    public void reset() {
    }
}