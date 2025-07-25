package com.tiation.dnddiceroller.performance

import android.content.Context
import android.os.Debug
import android.util.Log
import androidx.work.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * Performance monitoring system for the DnD Dice Roller app
 * 
 * Built by Jack Jonas (WA rigger) & Tia (dev, ChaseWhiteRabbit NGO) 
 * to serve riggers and fund social impact through a smart, practical SaaS 
 * for the heavy lifting industry.
 * 
 * Contact:
 * Jack Jonas – jackjonas95@gmail.com
 * Tia – tiatheone@protonmail.com
 */
class PerformanceMonitor(
    private val context: Context,
    private val analytics: FirebaseAnalytics
) {
    companion object {
        private const val TAG = "PerformanceMonitor"
        private const val PERFORMANCE_WORKER_NAME = "PerformanceWorker"
        
        // Metric names
        const val DICE_ROLL_TRACE = "dice_roll"
        const val ANIMATION_TRACE = "dice_animation"
        const val UI_RENDER_TRACE = "ui_render"
        const val MEMORY_USAGE = "memory_usage"
        const val BATTERY_USAGE = "battery_usage"
        const val APP_STARTUP = "app_startup"
    }
    
    private val performance = FirebasePerformance.getInstance()
    private val workManager = WorkManager.getInstance(context)
    private val scope = CoroutineScope(Dispatchers.IO)
    
    init {
        setupPeriodicMonitoring()
    }
    
    /**
     * Start a performance trace
     */
    fun startTrace(traceName: String): Trace {
        val trace = performance.newTrace(traceName)
        trace.start()
        Log.d(TAG, "Started trace: $traceName")
        return trace
    }
    
    /**
     * Stop a performance trace and log custom metrics
     */
    fun stopTrace(trace: Trace, customMetrics: Map<String, Long> = emptyMap()) {
        customMetrics.forEach { (key, value) ->
            trace.putMetric(key, value)
        }
        trace.stop()
        Log.d(TAG, "Stopped trace: ${trace}")
    }
    
    /**
     * Track dice roll performance
     */
    fun trackDiceRoll(diceType: String, rollCount: Int, duration: Long) {
        val trace = startTrace(DICE_ROLL_TRACE)
        
        // Add custom metrics
        trace.putAttribute("dice_type", diceType)
        trace.putMetric("roll_count", rollCount.toLong())
        trace.putMetric("duration_ms", duration)
        
        // Log to Analytics
        analytics.logEvent("dice_roll_performance") {
            param("dice_type", diceType)
            param("roll_count", rollCount.toLong())
            param("duration_ms", duration)
        }
        
        stopTrace(trace)
    }
    
    /**
     * Track animation performance
     */
    fun trackAnimation(animationType: String, duration: Long, frameDrops: Int = 0) {
        val trace = startTrace(ANIMATION_TRACE)
        
        trace.putAttribute("animation_type", animationType)
        trace.putMetric("duration_ms", duration)
        trace.putMetric("frame_drops", frameDrops.toLong())
        
        analytics.logEvent("animation_performance") {
            param("animation_type", animationType)
            param("duration_ms", duration)
            param("frame_drops", frameDrops.toLong())
        }
        
        stopTrace(trace)
    }
    
    /**
     * Track memory usage
     */
    fun trackMemoryUsage() {
        scope.launch {
            val runtime = Runtime.getRuntime()
            val usedMemory = runtime.totalMemory() - runtime.freeMemory()
            val maxMemory = runtime.maxMemory()
            val memoryUsagePercent = (usedMemory * 100 / maxMemory).toInt()
            
            val trace = startTrace(MEMORY_USAGE)
            trace.putMetric("used_memory_mb", usedMemory / (1024 * 1024))
            trace.putMetric("max_memory_mb", maxMemory / (1024 * 1024))
            trace.putMetric("usage_percent", memoryUsagePercent.toLong())
            
            // Check for memory pressure
            if (memoryUsagePercent > 80) {
                analytics.logEvent("memory_pressure") {
                    param("usage_percent", memoryUsagePercent.toLong())
                }
                Log.w(TAG, "High memory usage detected: $memoryUsagePercent%")
            }
            
            stopTrace(trace)
        }
    }
    
    /**
     * Track app startup time
     */
    fun trackAppStartup(startupTime: Long) {
        val trace = startTrace(APP_STARTUP)
        trace.putMetric("startup_time_ms", startupTime)
        
        analytics.logEvent("app_startup") {
            param("startup_time_ms", startupTime)
        }
        
        stopTrace(trace)
    }
    
    /**
     * Setup periodic performance monitoring
     */
    private fun setupPeriodicMonitoring() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        
        val periodicWorkRequest = PeriodicWorkRequestBuilder<PerformanceWorker>(
            15, TimeUnit.MINUTES,
            5, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()
        
        workManager.enqueueUniquePeriodicWork(
            PERFORMANCE_WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }
    
    /**
     * Get current performance metrics
     */
    fun getCurrentMetrics(): PerformanceMetrics {
        val runtime = Runtime.getRuntime()
        val usedMemory = runtime.totalMemory() - runtime.freeMemory()
        val maxMemory = runtime.maxMemory()
        
        return PerformanceMetrics(
            usedMemoryMB = usedMemory / (1024 * 1024),
            maxMemoryMB = maxMemory / (1024 * 1024),
            memoryUsagePercent = (usedMemory * 100 / maxMemory).toInt(),
            nativeHeapSize = Debug.getNativeHeapSize() / (1024 * 1024),
            nativeHeapAllocatedSize = Debug.getNativeHeapAllocatedSize() / (1024 * 1024)
        )
    }
}

/**
 * Data class for performance metrics
 */
data class PerformanceMetrics(
    val usedMemoryMB: Long,
    val maxMemoryMB: Long,
    val memoryUsagePercent: Int,
    val nativeHeapSize: Long,
    val nativeHeapAllocatedSize: Long
)

/**
 * Extension function for easier analytics logging
 */
private inline fun FirebaseAnalytics.logEvent(
    name: String,
    block: androidx.core.os.bundleOf.() -> Unit
) {
    logEvent(name, androidx.core.os.bundleOf(block))
}
