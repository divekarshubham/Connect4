package edu.nyu.pqs.stopwatch.impl;

import edu.nyu.pqs.stopwatch.api.Stopwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The StopwatchFactory is a thread-safe factory class for Stopwatch objects.
 * It maintains references to all created Stopwatch objects and provides a
 * convenient method for getting a list of those objects.
 */
public class StopwatchFactory {
    private static ConcurrentMap<String, SharedStopWatch> stopwatches = new ConcurrentHashMap<>();
    private static Object SWFlock = new Object();

    /***
     * Private constructor to make StopWatchFactory non-initializable
     */
    private StopwatchFactory() {
        throw new AssertionError("Impossible scenario");
    }

    /**
     * Creates and returns a new Stopwatch object
     *
     * @param id The identifier of the new object
     * @return The new Stopwatch object
     * @throws IllegalArgumentException if <code>id</code> is empty, null, or
     *                                  already taken.
     */
    public static Stopwatch getStopwatch(String id) {
        if (id == null || id.equals("")) {
            throw new IllegalArgumentException("Invalid id");
        }
        synchronized (SWFlock) {
            if (stopwatches.containsKey(id)) {
                throw new IllegalArgumentException("ID already exists");
            }
            SharedStopWatch sw = new SharedStopWatch(id);
            stopwatches.put(id, sw);
            return sw;
        }
    }

    /**
     * Returns a list of all created stopwatches
     *
     * @return a List of al creates Stopwatch objects.  Returns an empty
     * list if no Stopwatches have been created.
     */
    public static List<Stopwatch> getStopwatches() {
        synchronized (SWFlock) {
            return new ArrayList<Stopwatch>(stopwatches.values());
        }
    }

    /**
     * Package private method for testing the class
     */
    static void clearList(){
        stopwatches.clear();
    }
}
