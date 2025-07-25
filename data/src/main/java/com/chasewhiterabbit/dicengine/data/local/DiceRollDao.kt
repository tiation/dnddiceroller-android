package com.chasewhiterabbit.dicengine.data.local

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Data Access Object for dice roll operations
 */
@Dao
interface DiceRollDao {
    
    @Insert
    suspend fun insertRoll(roll: DiceRollEntity): Long
    
    @Query("SELECT * FROM dice_rolls ORDER BY timestamp DESC")
    fun getAllRollsPaged(): PagingSource<Int, DiceRollEntity>
    
    @Query("SELECT * FROM dice_rolls ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentRolls(limit: Int = 50): List<DiceRollEntity>
    
    @Query("SELECT * FROM dice_rolls WHERE timestamp >= :startDate ORDER BY timestamp DESC")
    fun getRollsAfterDate(startDate: LocalDateTime): Flow<List<DiceRollEntity>>
    
    @Query("SELECT * FROM dice_rolls WHERE diceType = :diceType ORDER BY timestamp DESC")
    fun getRollsByDiceType(diceType: String): Flow<List<DiceRollEntity>>
    
    @Query("SELECT * FROM dice_rolls WHERE sessionId = :sessionId ORDER BY timestamp DESC")
    fun getRollsBySession(sessionId: String): Flow<List<DiceRollEntity>>
    
    @Query("SELECT * FROM dice_rolls WHERE characterName = :characterName ORDER BY timestamp DESC")
    fun getRollsByCharacter(characterName: String): Flow<List<DiceRollEntity>>
    
    @Query("SELECT * FROM dice_rolls WHERE context = :context ORDER BY timestamp DESC")
    fun getRollsByContext(context: String): Flow<List<DiceRollEntity>>
    
    // Statistics queries
    @Query("SELECT COUNT(*) FROM dice_rolls")
    suspend fun getTotalRollCount(): Int
    
    @Query("SELECT AVG(CAST(result AS REAL)) FROM dice_rolls WHERE diceType = :diceType")
    suspend fun getAverageForDiceType(diceType: String): Double?
    
    @Query("SELECT MAX(result) FROM dice_rolls WHERE diceType = :diceType")
    suspend fun getMaxForDiceType(diceType: String): Int?
    
    @Query("SELECT MIN(result) FROM dice_rolls WHERE diceType = :diceType")
    suspend fun getMinForDiceType(diceType: String): Int?
    
    @Query("SELECT diceType, COUNT(*) as count FROM dice_rolls GROUP BY diceType ORDER BY count DESC")
    suspend fun getDiceTypeDistribution(): List<DiceTypeCount>
    
    @Query("SELECT result, COUNT(*) as frequency FROM dice_rolls WHERE diceType = :diceType GROUP BY result ORDER BY result")
    suspend fun getRollDistribution(diceType: String): List<RollFrequency>
    
    @Query("DELETE FROM dice_rolls WHERE timestamp < :cutoffDate")
    suspend fun deleteRollsOlderThan(cutoffDate: LocalDateTime): Int
    
    @Query("DELETE FROM dice_rolls")
    suspend fun clearAllRolls()
    
    @Query("SELECT * FROM dice_rolls WHERE id = :id")
    suspend fun getRollById(id: Long): DiceRollEntity?
    
    @Delete
    suspend fun deleteRoll(roll: DiceRollEntity)
}

/**
 * Data class for dice type statistics
 */
data class DiceTypeCount(
    val diceType: String,
    val count: Int
)

/**
 * Data class for roll frequency statistics
 */
data class RollFrequency(
    val result: Int,
    val frequency: Int
)
