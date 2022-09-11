package com.alexjprog.deezerforandroid

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import kotlinx.coroutines.flow.flow
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun generateTrackFlow(size: Int) =
    flow {
        emit(buildList {
            repeat(size) { i ->
                add(TrackModel(i))
            }
        })
    }

fun <T> generateFlowWithException() =
    flow<T> { throw Exception("test network exception") }

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}