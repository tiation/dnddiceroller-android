package com.tiation.dnddiceroller.features.dice

import kotlin.random.Random

class DiceRoller(private val random: Random = Random.Default) {
    
    fun roll(diceType: DiceType, rollType: RollType = RollType.NORMAL, modifiers: List<DiceModifier> = emptyList()): Int {
        val baseResult = when (rollType) {
            RollType.NORMAL -> rollDice(diceType)
            RollType.ADVANTAGE -> maxOf(rollDice(diceType), rollDice(diceType))
            RollType.DISADVANTAGE -> minOf(rollDice(diceType), rollDice(diceType))
        }
        
        return applyModifiers(baseResult, modifiers)
    }
    
    private fun rollDice(diceType: DiceType): Int {
        val maxValue = when (diceType) {
            DiceType.D4 -> 4
            DiceType.D6 -> 6
            DiceType.D8 -> 8
            DiceType.D10 -> 10
            DiceType.D12 -> 12
            DiceType.D20 -> 20
            DiceType.D100 -> 100
        }
        return random.nextInt(1, maxValue + 1)
    }
    
    private fun applyModifiers(result: Int, modifiers: List<DiceModifier>): Int {
        var finalResult = result
        
        modifiers.forEach { modifier ->
            finalResult = when (modifier.type) {
                ModifierType.FLAT -> finalResult + modifier.value
                ModifierType.MULTIPLY -> finalResult * modifier.value
                ModifierType.REROLL_ONES -> if (finalResult == 1) rollDice(DiceType.D20) else finalResult
                ModifierType.MIN_VALUE -> maxOf(finalResult, modifier.value)
            }
        }
        
        return finalResult
    }
}
