package com.tiation.dnddiceroller

import com.chasewhiterabbit.dicengine.data.repository.DiceRollRepository
import javax.inject.Inject

/**
 * Injectable class for logging dice roll results to persistent storage
 */
class RollLogger @Inject constructor(
    private val repo: DiceRollRepository
) {
    /**
     * Log a dice roll result
     * @param diceType The type of die rolled (e.g. "d20", "d6")
     * @param result The numeric result of the roll
     * @param modifier Optional modifier to add to the result (default: 0)
     */
    suspend fun log(diceType: String, result: Int, modifier: Int = 0) = 
        repo.logRoll(diceType, result, modifier)
}
