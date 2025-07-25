package com.tiation.dnddiceroller.data.repository

import com.tiation.dnddiceroller.data.local.entity.DiceRollHistory
import com.tiation.dnddiceroller.data.local.entity.QuickRollPreset
import com.tiation.dnddiceroller.domain.repository.DiceRollerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeDiceRollerRepository : DiceRollerRepository {
    private val rollHistory = MutableStateFlow<List<DiceRollHistory>>(emptyList())
    private val presets = MutableStateFlow<List<QuickRollPreset>>(emptyList())
    
    override fun getRecentRolls(): Flow<List<DiceRollHistory>> = rollHistory
    
    override suspend fun saveRoll(roll: DiceRollHistory) {
        rollHistory.update { currentList ->
            (currentList + roll)
                .sortedByDescending { it.timestamp }
                .take(100)
        }
    }
    
    override suspend fun clearHistory() {
        rollHistory.value = emptyList()
    }
    
    override fun getAllPresets(): Flow<List<QuickRollPreset>> = presets
    
    override suspend fun savePreset(preset: QuickRollPreset) {
        presets.update { currentList ->
            currentList + preset.copy(
                id = (currentList.maxOfOrNull { it.id } ?: 0) + 1,
                sortOrder = currentList.size
            )
        }
    }
    
    override suspend fun updatePreset(preset: QuickRollPreset) {
        presets.update { currentList ->
            currentList.map {
                if (it.id == preset.id) preset else it
            }
        }
    }
    
    override suspend fun deletePreset(preset: QuickRollPreset) {
        presets.update { currentList ->
            currentList.filter { it.id != preset.id }
        }
    }
}
