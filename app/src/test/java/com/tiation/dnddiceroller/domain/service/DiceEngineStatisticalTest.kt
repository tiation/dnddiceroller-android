package com.tiation.dnddiceroller.domain.service

import com.tiation.dnddiceroller.domain.model.RollType
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import kotlin.math.abs

class DiceEngineStatisticalTest : FunSpec({
    val diceEngine = DiceEngine()
    val sampleSize = 100_000
    val marginOfError = 0.02 // 2% margin of error for statistical tests

    context("statistical validation") {
        test("d20 average should be close to 10.5") {
            val rolls = List(sampleSize) { diceEngine.rollSingleDie(20) }
            val average = rolls.average()
            average shouldBe (10.5 plusOrMinus 0.2)
        }

        test("advantage roll average should be higher than normal") {
            // Theoretical average for advantage on d20:
            // P(max(X,Y) = k) where X,Y are uniform on [1,20]
            // E[max(X,Y)] = Σ(k * P(max(X,Y) = k)) ≈ 13.825
            val advantageRolls = List(sampleSize) {
                diceEngine.performRoll(
                    numberOfDice = 1,
                    sides = 20,
                    rollType = RollType.ADVANTAGE
                ).results[0]
            }
            val advantageAvg = advantageRolls.average()
            
            // Should be around 13.825 (theoretical)
            advantageAvg shouldBe (13.825 plusOrMinus 0.2)
        }

        test("disadvantage roll average should be lower than normal") {
            // Theoretical average for disadvantage on d20:
            // P(min(X,Y) = k) where X,Y are uniform on [1,20]
            // E[min(X,Y)] = Σ(k * P(min(X,Y) = k)) ≈ 7.175
            val disadvantageRolls = List(sampleSize) {
                diceEngine.performRoll(
                    numberOfDice = 1,
                    sides = 20,
                    rollType = RollType.DISADVANTAGE
                ).results[0]
            }
            val disadvantageAvg = disadvantageRolls.average()
            
            // Should be around 7.175 (theoretical)
            disadvantageAvg shouldBe (7.175 plusOrMinus 0.2)
        }

        test("distribution of results should be uniform") {
            val sides = 20
            val expectedFrequency = sampleSize.toDouble() / sides
            val tolerance = expectedFrequency * marginOfError

            val results = List(sampleSize) { diceEngine.rollSingleDie(sides) }
            val frequencies = results.groupingBy { it }.eachCount()

            frequencies.forEach { (_, count) ->
                abs(count - expectedFrequency) shouldBe (0.0 plusOrMinus tolerance)
            }
        }

        test("multiple dice sum distribution should follow expected pattern") {
            // Rolling 3d6 - sum should average to 10.5
            val rolls = List(sampleSize) {
                diceEngine.rollMultipleDice(3, 6).sum()
            }
            val average = rolls.average()
            
            // Expected average for 3d6 = 3 * 3.5 = 10.5
            average shouldBe (10.5 plusOrMinus 0.2)
        }
    }
})
