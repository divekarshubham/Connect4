package edu.nyu.pqs.stopwatch.test;

import edu.nyu.pqs.stopwatch.api.Stopwatch;
import edu.nyu.pqs.stopwatch.impl.StopwatchFactory;
import org.junit.Assert;

public class SharedStopWatchTest {

    private Stopwatch stopwatch;

    @org.junit.Before
    public void setUp() throws Exception {
        stopwatch = StopwatchFactory.getStopwatch(
                "ID " + System.currentTimeMillis());
    }

    /**
     * This test may result fail if thread 2 is suspended in the process.
     * This initializes a shared stopwatch between two threads where one thread
     * starts laps and stops, while the other laps and stops(which raises the IllegalStateException)
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testingThreadSafety() {
        final Stopwatch shared = StopwatchFactory.getStopwatch("thread-safe");
        Thread t = new Thread(new Runnable() {
            Stopwatch p = shared;

            public void run() {
                p.start();
                try {
                    Thread.sleep(500);
                    p.lap();
                    Thread.sleep(500);
                    p.stop();
                } catch (InterruptedException ignored) { }
            }
        });
        t.start();

        try {
            Thread.sleep(600);
            shared.lap();
            Thread.sleep(1000);
        } catch (InterruptedException ignored) { }
        Assert.assertEquals(3, shared.getLapTimes().size());
        shared.stop();
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
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void lapOnStopwatch_ThatsNotStarted() {
        stopwatch.lap();
    }

    @org.junit.Test
    public void stopAfterTenLapsAndResumeAfterPause() {
        tenLapsOnStopwatch();
        stopwatch.stop();
        Assert.assertEquals(11, stopwatch.getLapTimes().size());

        stopwatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
        stopwatch.stop();
        Assert.assertEquals(11, stopwatch.getLapTimes().size());
    }

    @org.junit.Test
    public void reset() {
        tenLapsOnStopwatch();
        stopwatch.reset();
        Assert.assertEquals(0, stopwatch.getLapTimes().size());
    }
}