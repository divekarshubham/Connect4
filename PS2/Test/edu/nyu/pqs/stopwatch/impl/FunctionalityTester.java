package edu.nyu.pqs.stopwatch.impl;

import edu.nyu.pqs.stopwatch.api.Stopwatch;
import org.junit.Assert;

import java.util.logging.Logger;

public class FunctionalityTester {

    private static Stopwatch stopwatch;
    private static final Logger logger =
            Logger.getLogger("edu.nyu.pqs.stopwatch.impl.FunctionalityTester");

    public FunctionalityTester() {
        stopwatch = StopwatchFactory.getStopwatch("DemoApp");
    }

    /**
     * This test may result fail if thread 2 is suspended in the process.
     * This initializes a shared stopwatch between two threads where one thread
     * starts laps and stops, while the other laps and stops(which raises the IllegalStateException)
     */
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
            Assert.assertEquals(3, shared.getLapTimes().size());
            logger.info(stopwatch.toString());
            shared.stop();
        }
        catch (IllegalStateException ise){
            logger.info("Exception occured when trying to stop a stopped thread:");
            logger.info(ise.toString());
        }
        catch (InterruptedException ignored) { }
    }

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

    /**
     * This tests the pausing functionality of the stop(). When the watch is restarted,
     * the last lap is removed from the elapsedTimes and appended when it is stopped again
     * with the added time.
     */
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

    public static void main(String[] args) {
        FunctionalityTester ft = new FunctionalityTester();
        ft.stopAfterTenLapsAndResumeAfterPause();
        ft.testingThreadSafety();
    }
}
