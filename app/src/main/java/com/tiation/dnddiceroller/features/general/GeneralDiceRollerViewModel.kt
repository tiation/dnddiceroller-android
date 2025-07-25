package com.tiation.dnddiceroller.features.general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chasewhiterabbit.dicengine.domain.engine.DiceEngine
import com.tiation.dnddiceroller.RollLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for General Dice Roller Screen with persistent logging
 */
@HiltViewModel
class GeneralDiceRollerViewModel @Inject constructor(
    private val diceEngine: DiceEngine,
    private val rollLogger: RollLogger
) : ViewModel() {

    /**
     * Roll a standard die and log the result
     */
    fun rollStandardDie(sides: Int) {
        viewModelScope.launch {
            val result = diceEngine.rollDie(sides)
            rollLogger.log("d$sides", result)
        }
    }

    /**
     * Roll a custom die and log the result
     */
    fun rollCustomDie(sides: Int) {
        viewModelScope.launch {
            val result = diceEngine.rollDie(sides)
            rollLogger.log("d$sides", result)
        }
    }

    /**
     * Roll a die with a modifier and log the result
     */
    fun rollWithModifier(sides: Int, modifier: Int) {
        viewModelScope.launch {
            val result = diceEngine.rollDie(sides)
            rollLogger.log("d$sides", result, modifier)
        }
    }
}
