package com.chasewhiterabbit.dicengine.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.chasewhiterabbit.dicengine.data.local.DiceRollDao
import com.chasewhiterabbit.dicengine.data.local.DiceRollEntity
import com.chasewhiterabbit.dicengine.data.local.DiceTypeCount
import com.chasewhiterabbit.dicengine.data.local.RollFrequency
import com.chasewhiterabbit.dicengine.domain.engine.DiceEngine
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing dice roll data
 */
@Singleton
class DiceRollRepository @Inject constructor(
    private val diceRollDao: DiceRollDao,
    private val diceEngine: DiceEngine
) {
    
    /**
     * Log a new dice roll
     */
    suspend fun logRoll(
        diceType: String,
        result: Int,
        modifier: Int = 0,
        sessionId: String? = null,
        context: String? = null,
        characterName: String? = null
    ): Long {
        val totalResult = result + modifier
        val rollEntity = DiceRollEntity(
            diceType = diceType,
            result = result,
            modifier = modifier,
            totalResult = totalResult,
            timestamp = LocalDateTime.now(),
            sessionId = sessionId,
            context = context,
            characterName = characterName
        )
        return diceRollDao.insertRoll(rollEntity)
    }
    
    /**
     * Get paginated roll history
     */
    fun getRollHistory(): Flow<PagingData<DiceRollEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { diceRollDao.getAllRollsPaged() }
        ).flow
    }
    
    /**
     * Get recent rolls (limited)
     */
    suspend fun getRecentRolls(limit: Int = 50): List<DiceRollEntity> {
        return diceRollDao.getRecentRolls(limit)
    }
    
    /**
     * Get rolls after a specific date
     */
    fun getRollsAfterDate(startDate: LocalDateTime): Flow<List<DiceRollEntity>> {
        return diceRollDao.getRollsAfterDate(startDate)
    }
    
    /**
     * Get rolls by dice type
     */
    fun getRollsByDiceType(diceType: String): Flow<List<DiceRollEntity>> {
        return diceRollDao.getRollsByDiceType(diceType)
    }
    
    /**
     * Get rolls by session
     */
    fun getRollsBySession(sessionId: String): Flow<List<DiceRollEntity>> {
        return diceRollDao.getRollsBySession(sessionId)
    }
    
    /**
     * Get rolls by character
     */
    fun getRollsByCharacter(characterName: String): Flow<List<DiceRollEntity>> {
        return diceRollDao.getRollsByCharacter(characterName)
    }
    
    /**
     * Get rolls by context
     */
    fun getRollsByContext(context: String): Flow<List<DiceRollEntity>> {
        return diceRollDao.getRollsByContext(context)
    }
    
    // Statistics methods
    suspend fun getTotalRollCount(): Int = diceRollDao.getTotalRollCount()
    
    suspend fun getAverageForDiceType(diceType: String): Double? = 
        diceRollDao.getAverageForDiceType(diceType)
    
    suspend fun getMaxForDiceType(diceType: String): Int? = 
        diceRollDao.getMaxForDiceType(diceType)
    
    suspend fun getMinForDiceType(diceType: String): Int? = 
        diceRollDao.getMinForDiceType(diceType)
    
    suspend fun getDiceTypeDistribution(): List<DiceTypeCount> = 
        diceRollDao.getDiceTypeDistribution()
    
    suspend fun getRollDistribution(diceType: String): List<RollFrequency> = 
        diceRollDao.getRollDistribution(diceType)
    
    // Data management
    suspend fun deleteRollsOlderThan(cutoffDate: LocalDateTime): Int {
        return diceRollDao.deleteRollsOlderThan(cutoffDate)
    }
    
    suspend fun clearAllRolls() {
        diceRollDao.clearAllRolls()
    }
    
    suspend fun deleteRoll(roll: DiceRollEntity) {
        diceRollDao.deleteRoll(roll)
    }
    
    suspend fun getRollById(id: Long): DiceRollEntity? {
        return diceRollDao.getRollById(id)
    }
}
