package com.tiation.dnddiceroller

/**
 * Functional interface for logging dice roll results
 */
fun interface RollLogger {
    /**
     * Log a dice roll result
     * @param diceType The type of die rolled (e.g. "d20", "d6")
     * @param result The numeric result of the roll
     */
    fun logRoll(diceType: String, result: Int)
}
