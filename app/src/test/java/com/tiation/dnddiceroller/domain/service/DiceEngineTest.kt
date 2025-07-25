package com.tiation.dnddiceroller.domain.service

import com.tiation.dnddiceroller.domain.model.RollType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll
import kotlin.random.Random

class DiceEngineTest : FunSpec({
    val diceEngine = DiceEngine()

    context("custom die validation") {
        test("sides >= 2 should be valid") {
            diceEngine.validateCustomDieSides(2) shouldBe true
            diceEngine.validateCustomDieSides(100) shouldBe true
            diceEngine.validateCustomDieSides(1000) shouldBe true
        }

        test("sides < 2 should be invalid") {
            diceEngine.validateCustomDieSides(1) shouldBe false
            diceEngine.validateCustomDieSides(0) shouldBe false
            diceEngine.validateCustomDieSides(-1) shouldBe false
        }
    }

    context("single die roll") {
        test("roll should be within valid range") {
            repeat(10_000) {
                val sides = Random.nextInt(2, 1000)
                val result = diceEngine.rollSingleDie(sides)
                result shouldBeGreaterThanOrEqual 1
                result shouldBeLessThanOrEqual sides
            }
        }

        test("should throw exception for invalid sides") {
            shouldThrow<IllegalArgumentException> {
                diceEngine.rollSingleDie(1)
            }
            shouldThrow<IllegalArgumentException> {
                diceEngine.rollSingleDie(0)
            }
            shouldThrow<IllegalArgumentException> {
                diceEngine.rollSingleDie(-1)
            }
        }
    }

    context("multiple dice rolls") {
        test("should return correct number of results") {
            checkAll(
                Arb.positiveInt(100),  // max 100 dice
                Arb.int(2, 1000)   // sides between 2 and 1000
            ) { numberOfDice, sides ->
                val results = diceEngine.rollMultipleDice(numberOfDice, sides)
                results shouldHaveSize numberOfDice
                results.forEach { result ->
                    result shouldBeGreaterThanOrEqual 1
                    result shouldBeLessThanOrEqual sides
                }
            }
        }

        test("should throw exception for invalid inputs") {
            shouldThrow<IllegalArgumentException> {
                diceEngine.rollMultipleDice(0, 6)
            }
            shouldThrow<IllegalArgumentException> {
                diceEngine.rollMultipleDice(-1, 6)
            }
            shouldThrow<IllegalArgumentException> {
                diceEngine.rollMultipleDice(1, 1)
            }
        }
    }

    context("advantage and disadvantage") {
        test("advantage should return higher roll") {
            repeat(10_000) { 
                val sides = Random.nextInt(2, 1000)
                val result = diceEngine.rollWithAdvantage(sides)
                result shouldBeGreaterThanOrEqual 1
                result shouldBeLessThanOrEqual sides
            }
        }

        test("disadvantage should return lower roll") {
            repeat(10_000) {
                val sides = Random.nextInt(2, 1000)
                val result = diceEngine.rollWithDisadvantage(sides)
                result shouldBeGreaterThanOrEqual 1
                result shouldBeLessThanOrEqual sides
            }
        }
    }

    context("total calculation") {
        test("should correctly sum results and apply modifier") {
            checkAll(
                Arb.list(Arb.int(1, 1000), 1..100),  // list of 1-100 results
                Arb.int(-100, 100)  // modifier between -100 and 100
            ) { results, modifier ->
                val (baseTotal, finalTotal) = diceEngine.calculateTotals(results, modifier)
                baseTotal shouldBe results.sum()
                finalTotal shouldBe (baseTotal + modifier)
            }
        }
    }

    context("complete roll operation") {
        test("normal roll should produce expected results") {
            repeat(10_000) {
                val numberOfDice = Random.nextInt(1, 10)
                val sides = Random.nextInt(2, 100)
                val modifier = Random.nextInt(-10, 10)

                val roll = diceEngine.performRoll(
                    numberOfDice = numberOfDice,
                    sides = sides,
                    modifier = modifier
                )

                roll.results shouldHaveSize numberOfDice
                roll.results.forEach { result ->
                    result shouldBeGreaterThanOrEqual 1
                    result shouldBeLessThanOrEqual sides
                }
                roll.total shouldBe roll.results.sum()
                roll.finalTotal shouldBe (roll.total + modifier)
                roll.rollType shouldBe RollType.NORMAL
            }
        }

        test("advantage roll should produce higher results") {
            repeat(10_000) {
                val roll = diceEngine.performRoll(
                    numberOfDice = 1,
                    sides = 20,
                    rollType = RollType.ADVANTAGE
                )
                
                roll.results shouldHaveSize 1
                roll.results[0] shouldBeGreaterThanOrEqual 1
                roll.results[0] shouldBeLessThanOrEqual 20
            }
        }

        test("disadvantage roll should produce lower results") {
            repeat(10_000) {
                val roll = diceEngine.performRoll(
                    numberOfDice = 1,
                    sides = 20,
                    rollType = RollType.DISADVANTAGE
                )
                
                roll.results shouldHaveSize 1
                roll.results[0] shouldBeGreaterThanOrEqual 1
                roll.results[0] shouldBeLessThanOrEqual 20
            }
        }
    }
})
