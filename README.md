# PQS_SP20_sjd451
## 1. Thread-Safe Stopwatch

### I. SharedStopWatch

This is the implementation of the `StopWatch` interface. It uses a explicit `lock` to synchronize various segments of the code. This stopwatch can be shared and operated by multiple threads. However if the thread performs an illegal operation an `IllegalStateException`.

### II. WatchState

Represents the states that the stopwatch can be in i.e. READY(when the stopwatch has just been created or reset), RUNNING, and STOPPED(after calling `stop()`)

### III. FunctionalityTester

This contains a class to test the edge case behavior of the `StopWatch`. It contains the test to check the pausing functionality and also contains a method to test the thread-safety of the `StopWatch`

IV. UnitTests

`SharedStopWatchTest` and `StopWatchFactoryTest` contain the unit test for their respective classes.

## 2. Code Review

The code view is in the following format

[line number, class name]: comment