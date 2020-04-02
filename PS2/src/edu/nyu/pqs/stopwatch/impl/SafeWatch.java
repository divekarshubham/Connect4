package edu.nyu.pqs.stopwatch.impl;

import edu.nyu.pqs.stopwatch.api.Stopwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SafeWatch implements Stopwatch {

    private final String id;
    private List<Long> elapsedTimes;
    private long starttime;
    private long endtime;
    private long lastLapTime;
    private WatchState watchState;
    private Object lock;
    private long delay;

    /***
     * Constructor to initialize the state of the watch, lock for synchronization and the list to keep track of elapsed
     * times
     * @param id identifier for the Stopwatch
     */
    public SafeWatch(String id) {
        this.id = id;
        elapsedTimes = new ArrayList<>();
        watchState = WatchState.READY;
        lock = new Object();
        delay = 0;
    }

    /***
     * Returns the Id of this stopwatch
     * @return the Id of this stopwatch.  Will never be empty or null.
     */
    @Override
    public String getId() {
        return id;
    }

    /***
     * Starts the timer of the clock and changes the state of the stopwatch to RUNNING
     * @throws IllegalStateException If the stopwatch is already RUNNING
     */
    @Override
    public void start() throws IllegalStateException {
        synchronized (lock) {
            if (watchState == WatchState.RUNNING)
                throw new IllegalStateException("Watch " + id + " already running");
            else if (watchState == WatchState.STOPPED) {
                delay += elapsedTimes.get(elapsedTimes.size() - 1);
                elapsedTimes.remove(elapsedTimes.size() - 1);
            }
            watchState = WatchState.RUNNING;
        }
        starttime = System.currentTimeMillis();
        lastLapTime = starttime;
    }

    @Override
    public void lap() throws IllegalStateException {
        synchronized (lock) {
            if (watchState != WatchState.RUNNING)
                throw new IllegalStateException("Watch " + id + " is not running");
        }
        long lapTime = System.currentTimeMillis();
        elapsedTimes.add(lapTime - lastLapTime + delay);
        lastLapTime = lapTime;
    }

    @Override
    public void stop() throws IllegalStateException {
        synchronized (lock) {
            if (watchState != WatchState.RUNNING)
                throw new IllegalStateException("Watch " + id + " is not running");
        }
        endtime = System.currentTimeMillis();
        elapsedTimes.add(endtime - lastLapTime + delay);
        watchState = WatchState.STOPPED;
    }

    @Override
    public void reset() {
        synchronized (lock) {
            starttime = 0;
            endtime = 0;
            elapsedTimes.clear();
            watchState = WatchState.READY;
        }

    }

    @Override
    public List<Long> getLapTimes() {
        return elapsedTimes;
    }

    @Override
    public String toString() {
        long millis = endtime - starttime;
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        return hms;
    }
}
