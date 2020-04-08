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

    /**
     * Stores the time elapsed since the last time lap() was called
     * or since start() was called if this is the first lap and stores
     * it in a list. Has no effect on the stopwatch state.
     *
     * @throws IllegalStateException thrown when the stopwatch isn't running
     */
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

    /**
     * Stops the stopwatch (and records one final lap). This lap is deleted if the
     * stopwatch is started again. Changes the state of the stopwatch to STOPPED
     *
     * @throws IllegalStateException thrown when the stopwatch isn't running
     */
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

    /**
     * Resets the stopwatch.  If the stopwatch is running, this method stops the
     * watch and resets it.  This also clears all recorded laps.
     */
    @Override
    public void reset() {
        synchronized (lock) {
            starttime = 0;
            endtime = 0;
            elapsedTimes.clear();
            watchState = WatchState.READY;
        }

    }

    /**
     * Returns a list of lap times (in milliseconds).  This method can be called at
     * any time and will not throw an exception.
     *
     * @return a list of recorded lap times or an empty list.
     */
    @Override
    public List<Long> getLapTimes() {
        return elapsedTimes;
    }

    /**
     * Generates a string to represent the current state of the clock, recording all laptimes.
     *
     * @return
     */
    @Override
    public String toString() {
        synchronized (lock) {
            if (watchState == WatchState.READY)
                return "00:00:00";
        }
        String hms = "";
        int i = 1;
        long totalTime = 0;
        for (Long millis : elapsedTimes) {
            totalTime += millis;
            hms = hms + "Lap " + i + ": " + getFormattedTime(totalTime) + " Duration: " + millis + " milliseconds\n";

        }
        synchronized (lock) {
            if (watchState == WatchState.RUNNING)
                endtime = System.currentTimeMillis();
            totalTime += endtime - lastLapTime;
        }
        hms += "Total time elapsed: " + getFormattedTime(totalTime);
        return hms;
    }

    private String getFormattedTime(Long time) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(time) % TimeUnit.MINUTES.toSeconds(1));
    }
}
