package edu.nyu.pqs.stopwatch.impl;

import edu.nyu.pqs.stopwatch.api.Stopwatch;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class StopWatchFactoryTest {

    @After
    public void tearDown() throws Exception {
        StopwatchFactory.clearList();
    }

    @Test
    public void test_getStopwatch() {
        StopwatchFactory.getStopwatch("test-set");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_sameIDWatch(){
        StopwatchFactory.getStopwatch("test-set");
        StopwatchFactory.getStopwatch("test-set");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_nullStopWatch(){
        StopwatchFactory.getStopwatch("");
    }

    @Test
    public void test_getStopwatches() {
        List<Stopwatch> lst = StopwatchFactory.getStopwatches();
        Assert.assertEquals(0, lst.size());
        StopwatchFactory.getStopwatch("test-set");
        StopwatchFactory.getStopwatch("test-set2");
        lst = StopwatchFactory.getStopwatches();
        Assert.assertEquals(2, lst.size());
        Assert.assertEquals("test-set", lst.get(0).getId());
    }

}