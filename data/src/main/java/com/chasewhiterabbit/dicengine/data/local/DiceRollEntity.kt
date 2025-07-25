package com.chasewhiterabbit.dicengine.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * Database entity representing a dice roll record
 */
@Entity(tableName = "dice_rolls")
data class DiceRollEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val diceType: String,
    val result: Int,
    val modifier: Int = 0,
    val totalResult: Int,
    val timestamp: LocalDateTime,
    val sessionId: String? = null,
    val context: String? = null, // e.g., "attack", "damage", "skill check"
    val characterName: String? = null
)
