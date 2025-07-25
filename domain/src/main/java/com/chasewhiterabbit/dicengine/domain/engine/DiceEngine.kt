package com.chasewhiterabbit.dicengine.domain.engine

import kotlin.random.Random

/**
 * Core dice engine for generating random rolls
 * 
 * Contact: Garrett Dillman (garrett.dillman@gmail.com, garrett@sxc.codes)
 * Contact: Tia (tiatheone@protonmail.com)
 */
class DiceEngine {
    
    /**
     * Roll a single die with specified number of sides
     */
    fun rollDie(sides: Int): Int {
        require(sides >= 2) { "Dice must have at least 2 sides" }
        return Random.nextInt(1, sides + 1)
    }
    
    /**
     * Roll multiple dice of the same type
     */
    fun rollDice(numberOfDice: Int, sides: Int): List<Int> {
        require(numberOfDice > 0) { "Must roll at least one die" }
        require(sides >= 2) { "Dice must have at least 2 sides" }
        
        return (1..numberOfDice).map { rollDie(sides) }
    }
    
    /**
     * Roll with advantage (roll twice, take higher)
     */
    fun rollWithAdvantage(sides: Int): Int {
        val roll1 = rollDie(sides)
        val roll2 = rollDie(sides)
        return maxOf(roll1, roll2)
    }
    
    /**
     * Roll with disadvantage (roll twice, take lower)
     */
    fun rollWithDisadvantage(sides: Int): Int {
        val roll1 = rollDie(sides)
        val roll2 = rollDie(sides)
        return minOf(roll1, roll2)
    }
}
