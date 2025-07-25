package com.tiation.dnddiceroller

class DiceEngine {
    fun rollDie(sides: Int): Int {
        require(sides >= 2) { "Dice must have at least 2 sides" }
        return (1..sides).random()
    }
}
