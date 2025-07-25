package com.tiation.dnddiceroller.features.history

import java.time.LocalDateTime

/**
 * UI model for displaying roll history items
 * 
 * Contact: Garrett Dillman (garrett.dillman@gmail.com, garrett@sxc.codes)
 * Contact: Tia (tiatheone@protonmail.com)
 */
data class RollHistoryItem(
    val id: Long,
    val diceType: String,
    val result: Int,
    val modifier: Int,
    val totalResult: Int,
    val timestamp: LocalDateTime,
    val sessionId: String? = null,
    val context: String? = null,
    val characterName: String? = null
)

/**
 * Filter options for roll history
 */
data class HistoryFilter(
    val diceType: String? = null,
    val dateRange: DateRange = DateRange.ALL_TIME,
    val sessionId: String? = null,
    val characterName: String? = null,
    val context: String? = null
)

/**
 * Date range options for filtering
 */
enum class DateRange {
    ALL_TIME,
    TODAY,
    YESTERDAY,
    LAST_WEEK,
    LAST_MONTH,
    CUSTOM
}

/**
 * Statistics for roll history
 */
data class RollStatistics(
    val totalRolls: Int = 0,
    val diceTypeStats: List<DiceTypeStatistic> = emptyList(),
    val distributionData: List<RollDistributionData> = emptyList(),
    val averagesByType: Map<String, Double> = emptyMap(),
    val streaks: StreakData = StreakData()
)

/**
 * Statistics for a specific dice type
 */
data class DiceTypeStatistic(
    val diceType: String,
    val count: Int,
    val average: Double,
    val min: Int,
    val max: Int,
    val percentage: Double
)

/**
 * Distribution data for roll results
 */
data class RollDistributionData(
    val diceType: String,
    val results: List<ResultFrequency>
)

/**
 * Frequency of specific roll results
 */
data class ResultFrequency(
    val result: Int,
    val frequency: Int,
    val percentage: Double
)

/**
 * Streak information
 */
data class StreakData(
    val currentWinStreak: Int = 0,
    val currentLossStreak: Int = 0,
    val longestWinStreak: Int = 0,
    val longestLossStreak: Int = 0
)

/**
 * Export options for roll history
 */
data class ExportOptions(
    val format: ExportFormat,
    val includeStatistics: Boolean = false,
    val dateRange: DateRange = DateRange.ALL_TIME,
    val customStartDate: LocalDateTime? = null,
    val customEndDate: LocalDateTime? = null
)

/**
 * Export format options
 */
enum class ExportFormat {
    CSV,
    JSON,
    PDF
}
