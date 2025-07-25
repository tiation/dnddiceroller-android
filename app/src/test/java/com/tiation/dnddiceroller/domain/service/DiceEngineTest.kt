package com.tiation.dnddiceroller.domain.service

import com.chasewhiterabbit.dicengine.domain.engine.DiceEngine
import org.junit.Test
import org.junit.Assert.*
import kotlin.random.Random

/**
 * Unit tests for DiceEngine
 * 
 * Contact: Garrett Dillman (garrett.dillman@gmail.com, garrett@sxc.codes)
 * Contact: Tia (tiatheone@protonmail.com)
 */
class DiceEngineTest {
    
    private val diceEngine = DiceEngine()

    @Test
    fun `rollDie should return value within range`() {
        repeat(1000) {
            val sides = Random.nextInt(2, 21)
            val result = diceEngine.rollDie(sides)
            
            assertTrue("Roll result $result should be >= 1", result >= 1)
            assertTrue("Roll result $result should be <= $sides", result <= sides)
        }
    }

    @Test
    fun `rollDie should throw exception for invalid sides`() {
        assertThrows(IllegalArgumentException::class.java) {
            diceEngine.rollDie(1)
        }
        
        assertThrows(IllegalArgumentException::class.java) {
            diceEngine.rollDie(0)
        }
        
        assertThrows(IllegalArgumentException::class.java) {
            diceEngine.rollDie(-1)
        }
    }

    @Test
    fun `rollDice should return correct number of results`() {
        val numberOfDice = 5
        val sides = 6
        
        val results = diceEngine.rollDice(numberOfDice, sides)
        
        assertEquals("Should return $numberOfDice results", numberOfDice, results.size)
        results.forEach { result ->
            assertTrue("Each result should be >= 1", result >= 1)
            assertTrue("Each result should be <= $sides", result <= sides)
        }
    }

    @Test
    fun `rollDice should throw exception for invalid inputs`() {
        assertThrows(IllegalArgumentException::class.java) {
            diceEngine.rollDice(0, 6)
        }
        
        assertThrows(IllegalArgumentException::class.java) {
            diceEngine.rollDice(-1, 6)
        }
        
        assertThrows(IllegalArgumentException::class.java) {
            diceEngine.rollDice(1, 1)
        }
    }

    @Test
    fun `rollWithAdvantage should return valid result`() {
        repeat(100) {
            val sides = 20
            val result = diceEngine.rollWithAdvantage(sides)
            
            assertTrue("Advantage roll should be >= 1", result >= 1)
            assertTrue("Advantage roll should be <= $sides", result <= sides)
        }
    }

    @Test
    fun `rollWithDisadvantage should return valid result`() {
        repeat(100) {
            val sides = 20
            val result = diceEngine.rollWithDisadvantage(sides)
            
            assertTrue("Disadvantage roll should be >= 1", result >= 1)
            assertTrue("Disadvantage roll should be <= $sides", result <= sides)
        }
    }

    @Test
    fun `advantage should tend to be higher than disadvantage over many rolls`() {
        var advantageTotal = 0
        var disadvantageTotal = 0
        val iterations = 1000
        val sides = 20
        
        repeat(iterations) {
            advantageTotal += diceEngine.rollWithAdvantage(sides)
            disadvantageTotal += diceEngine.rollWithDisadvantage(sides)
        }
        
        val advantageAverage = advantageTotal.toDouble() / iterations
        val disadvantageAverage = disadvantageTotal.toDouble() / iterations
        
        assertTrue(
            "Advantage average ($advantageAverage) should be higher than disadvantage average ($disadvantageAverage)",
            advantageAverage > disadvantageAverage
        )
    }
}
