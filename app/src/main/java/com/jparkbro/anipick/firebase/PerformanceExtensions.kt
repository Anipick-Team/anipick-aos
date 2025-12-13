package com.jparkbro.anipick.firebase

import android.util.Log
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.jparkbro.anipick.BuildConfig

inline fun <T> measurePerformance(
    traceName: String,
    attributes: Map<String, String> = emptyMap(),
    block: () -> T
): T {
    if (BuildConfig.DEBUG) {
        return block()
    }

    val trace = FirebasePerformance.getInstance().newTrace(traceName)

    attributes.forEach { (key, value) ->
        trace.putAttribute(key, value)
    }

    trace.start()

    return try {
        block()
    } catch (e: Exception) {
        trace.putAttribute("error", e.message ?: "Unknown error")
        throw e
    } finally {
        trace.stop()
    }
}

suspend inline fun <T> measurePerformanceSuspend(
    traceName: String,
    attributes: Map<String, String> = emptyMap(),
    crossinline block: suspend () -> T
): T {
    if (BuildConfig.DEBUG) {
        return block()
    }

    val trace = FirebasePerformance.getInstance().newTrace(traceName)

    attributes.forEach { (key, value) ->
        trace.putAttribute(key, value)
    }

    trace.start()

    return try {
        block()
    } catch (e: Exception) {
        trace.putAttribute("error", e.message ?: "Unknown error")
        throw e
    } finally {
        trace.stop()
    }
}

class PerformanceTracer(private val traceName: String) {
    private var trace: Trace? = null

    fun start() {
        if (BuildConfig.DEBUG) return

        trace = FirebasePerformance.getInstance().newTrace(traceName).apply {
            start()
            Log.d("PerformanceTracer", "Started trace: $traceName")
        }
    }

    fun putAttribute(key: String, value: String) {
        if (BuildConfig.DEBUG) return

        trace?.putAttribute(key, value)
    }

    fun incrementMetric(metricName: String, incrementBy: Long = 1L) {
        if (BuildConfig.DEBUG) return

        trace?.incrementMetric(metricName, incrementBy)
    }

    fun stop() {
        if (BuildConfig.DEBUG) return

        trace?.stop()
        Log.d("PerformanceTracer", "Stopped trace: $traceName")
        trace = null
    }
}
