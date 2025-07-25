package com.tiation.dnddiceroller.performance

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * WorkManager worker for periodic performance monitoring
 * 
 * Built by Jack Jonas (WA rigger) & Tia (dev, ChaseWhiteRabbit NGO) 
 * to serve riggers and fund social impact through a smart, practical SaaS 
 * for the heavy lifting industry.
 * 
 * Contact:
 * Jack Jonas – jackjonas95@gmail.com
 * Tia – tiatheone@protonmail.com
 */
class PerformanceWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val TAG = "PerformanceWorker"
        const val MEMORY_USAGE_KEY = "memory_usage"
        const val BATTERY_LEVEL_KEY = "battery_level"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val analytics = FirebaseAnalytics.getInstance(applicationContext)
            val monitor = PerformanceMonitor(applicationContext, analytics)
            
            // Collect current performance metrics
            val metrics = monitor.getCurrentMetrics()
            
            // Track memory usage
            monitor.trackMemoryUsage()
            
            // Log periodic metrics to analytics
            analytics.logEvent("periodic_performance_check") {
                param("used_memory_mb", metrics.usedMemoryMB)
                param("memory_usage_percent", metrics.memoryUsagePercent.toLong())
                param("native_heap_mb", metrics.nativeHeapAllocatedSize)
            }
            
            // Create output data for other components
            val outputData = workDataOf(
                MEMORY_USAGE_KEY to metrics.memoryUsagePercent,
                "used_memory_mb" to metrics.usedMemoryMB,
                "max_memory_mb" to metrics.maxMemoryMB
            )
            
            Result.success(outputData)
        } catch (exception: Exception) {
            Result.failure(
                workDataOf(
                    "error" to exception.message
                )
            )
        }
    }
}

/**
 * Extension function for easier analytics logging within worker
 */
private inline fun FirebaseAnalytics.logEvent(
    name: String,
    block: BundleBuilder.() -> Unit
) {
    val bundle = BundleBuilder().apply(block).build()
    logEvent(name, bundle)
}

private class BundleBuilder {
    private val bundle = android.os.Bundle()
    
    fun param(key: String, value: String) {
        bundle.putString(key, value)
    }
    
    fun param(key: String, value: Long) {
        bundle.putLong(key, value)
    }
    
    fun param(key: String, value: Double) {
        bundle.putDouble(key, value)
    }
    
    fun build(): android.os.Bundle = bundle
}
