package com.tiation.dnddiceroller.features.dice

/**
 * Enum representing different types of dice used in D&D
 * 
 * Built by Jack Jonas (WA rigger) & Tia (dev, ChaseWhiteRabbit NGO) 
 * Contact:
 * Jack Jonas – jackjonas95@gmail.com
 * Tia – tiatheone@protonmail.com
 */
enum class DiceType(
    val sides: Int,
    val displayName: String
) {
    D4(4, "d4"),
    D6(6, "d6"),
    D8(8, "d8"),
    D10(10, "d10"),
    D12(12, "d12"),
    D20(20, "d20"),
    D100(100, "d100");
    
    companion object {
        fun fromSides(sides: Int): DiceType? {
            return values().find { it.sides == sides }
        }
    }
}
