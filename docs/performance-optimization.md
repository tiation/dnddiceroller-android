# Performance Optimization and Monitoring

This document describes the comprehensive performance optimization and monitoring system implemented in the DnD Dice Roller Android app.

## Overview

The performance optimization system is designed to:
- Monitor app performance in real-time
- Automatically adapt to device conditions (battery, memory, etc.)
- Provide detailed metrics through Grafana dashboards
- Alert on performance degradation
- Optimize animations and resource usage

Built by **Jack Jonas** (WA rigger) & **Tia** (dev, ChaseWhiteRabbit NGO) to serve riggers and fund social impact through a smart, practical SaaS for the heavy lifting industry.

**Contact:**
- Jack Jonas – jackjonas95@gmail.com
- Tia – tiatheone@protonmail.com

## System Components

### 1. Performance Monitoring (`PerformanceMonitor.kt`)

**Purpose:** Tracks app performance metrics and logs them to Firebase Analytics and Performance Monitoring.

**Key Features:**
- Real-time performance trace collection
- Memory usage monitoring
- Animation performance tracking
- Dice roll performance metrics
- Automatic alerting on performance issues

**Usage:**
```kotlin
val performanceMonitor = PerformanceMonitor(context, analytics)

// Track dice roll performance
performanceMonitor.trackDiceRoll(
    diceType = "d20",
    rollCount = 1,
    duration = 150L
)

// Track animation performance
performanceMonitor.trackAnimation(
    animationType = "dice_roll_d20",
    duration = 800L
)
```

### 2. Memory Optimization (`MemoryOptimizer.kt`)

**Purpose:** Manages memory usage and responds to system memory pressure.

**Key Features:**
- Automatic cache clearing during memory pressure
- Image cache management with weak references
- Memory usage monitoring
- Low memory mode detection
- Composable integration for UI components

**Usage:**
```kotlin
@Composable
fun MyScreen() {
    val memoryOptimizer = rememberMemoryOptimizer()
    val memoryInfo = memoryOptimizer.getMemoryInfo()
    
    if (memoryInfo.isLowMemoryMode) {
        // Reduce UI complexity
    }
}
```

### 3. Battery Optimization (`BatteryOptimizer.kt`)

**Purpose:** Adapts app behavior based on battery level and power state.

**Key Features:**
- Battery level monitoring
- Charging state detection
- Automatic feature disabling in low battery situations
- Temperature monitoring
- Power save mode integration

**Optimization Modes:**
- **NORMAL:** Full functionality
- **CONSERVATIVE:** Light optimization (power save mode)
- **AGGRESSIVE:** Heavy optimization (low battery <15%)
- **EXTREME:** Maximum optimization (critical battery <5%)

**Usage:**
```kotlin
@Composable
fun AnimatedComponent() {
    val batteryOptimizer = rememberBatteryOptimizer()
    val shouldAnimate = batteryOptimizer.shouldEnableFeature(BatteryFeature.ANIMATIONS)
    val durationModifier = batteryOptimizer.getAnimationDurationModifier()
    
    if (shouldAnimate) {
        // Show animations with modified duration
    }
}
```

### 4. Optimized Animations (`DiceAnimation.kt`)

**Purpose:** Battery-aware dice roll animations with performance monitoring.

**Optimizations:**
- Battery-based animation duration scaling
- Conditional haptic feedback
- Reduced animation complexity in low power modes
- Performance metric collection

### 5. Background Performance Worker (`PerformanceWorker.kt`)

**Purpose:** Periodic performance data collection using WorkManager.

**Features:**
- Runs every 15 minutes (with 5-minute flex period)
- Collects memory usage statistics
- Respects battery constraints
- Logs metrics to Firebase Analytics

## Monitoring and Alerting

### Grafana Dashboard

The system includes a comprehensive Grafana dashboard (`monitoring/grafana-dashboard.json`) with panels for:

1. **Memory Usage (%)** - Real-time memory consumption
2. **Animation Performance (ms)** - Animation duration metrics
3. **Daily Active Users** - User engagement metrics
4. **Dice Rolls per Hour** - Usage patterns
5. **App Crashes** - Stability monitoring
6. **Dice Type Distribution** - Feature usage analytics

### Alert Configuration

Automated alerts (`monitoring/grafana-alerts.json`) monitor:

1. **High Memory Usage** (>85% for 5+ minutes)
2. **Slow Animation Performance** (>1000ms average)
3. **App Crash Rate** (>5 crashes per hour)
4. **High Battery Usage** (>10% per 30 minutes)

**Alert Recipients:**
- tiatheone@protonmail.com
- garrett@sxc.codes  
- garrett.dillman@gmail.com

## Performance Optimizations Applied

### Animation Optimizations
- Reduced rotation from 720° to 360° (180° in low battery)
- Decreased bounce height from 50px to 30px (15px in low battery)
- Battery-aware duration scaling (0.5x to 1.0x)
- Conditional haptic feedback

### Memory Optimizations
- Weak reference image caching
- Automatic cache trimming during memory pressure
- Memory usage monitoring and garbage collection hints
- Low memory mode detection and adaptation

### Battery Optimizations
- Feature gating based on battery level
- Animation disabling in critical battery situations
- Background work limitations
- Temperature-based CPU throttling

### Render Performance
- Efficient Compose animations using `Animatable`
- Optimized `graphicsLayer` transformations
- Reduced animation complexity in constrained modes
- Lazy loading of UI components

## Integration with Existing Infrastructure

The system integrates with your existing VPS infrastructure:

- **Grafana Server:** `grafana.sxc.codes` (153.92.214.1)
- **Elastic Search:** `elastic.sxc.codes` (145.223.22.14) for log aggregation
- **Docker Runners:** For CI/CD deployment of monitoring updates

## Best Practices

1. **Memory Management:**
   - Always use weak references for caches
   - Respond to `onTrimMemory` callbacks
   - Monitor memory usage regularly

2. **Battery Optimization:**
   - Check battery state before intensive operations
   - Provide degraded experiences for low battery
   - Respect system power save mode

3. **Performance Monitoring:**
   - Log performance metrics consistently
   - Set appropriate alert thresholds
   - Monitor trends, not just individual events

4. **User Experience:**
   - Gracefully degrade features rather than crashing
   - Provide feedback about optimizations to users
   - Maintain core functionality even in extreme modes

## Deployment

1. **Firebase Setup:**
   - Add `google-services.json` to the `app/` directory
   - Configure Firebase Performance Monitoring
   - Set up Firebase Analytics

2. **Grafana Configuration:**
   - Import dashboard from `monitoring/grafana-dashboard.json`
   - Configure alert rules from `monitoring/grafana-alerts.json`
   - Set up email notifications

3. **Build Configuration:**
   - Ensure all dependencies are included
   - Test performance monitoring in debug builds
   - Verify alert configurations

## Future Enhancements

1. **Predictive Optimization:** Use ML to predict user behavior and pre-optimize
2. **Network Optimization:** Monitor and optimize network usage
3. **Thermal Throttling:** More sophisticated CPU thermal management
4. **User Preferences:** Allow users to override optimization settings
5. **A/B Testing:** Test different optimization strategies

This comprehensive system ensures the DnD Dice Roller app provides optimal performance across all device conditions while maintaining detailed monitoring and alerting capabilities.
