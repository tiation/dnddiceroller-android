package com.tiation.dnddiceroller.features.dice

enum class DiceType {
    D4, D6, D8, D10, D12, D20, D100
}

enum class RollType {
    NORMAL, ADVANTAGE, DISADVANTAGE
}

data class DiceModifier(
    val value: Int,
    val type: ModifierType
)

enum class ModifierType {
    FLAT, // Flat bonus/penalty
    MULTIPLY, // Multiply result
    REROLL_ONES, // Reroll 1s
    MIN_VALUE // Minimum result value
}

data class DiceSlotConfiguration(
    val id: Int,
    val name: String,
    val diceType: DiceType,
    val rollType: RollType = RollType.NORMAL,
    val modifiers: List<DiceModifier> = emptyList()
)

data class RoleConfiguration(
    val id: Int,
    val name: String,
    val slots: List<DiceSlotConfiguration> = emptyList()
) {
    init {
        require(slots.size <= 25) { "Role cannot have more than 25 dice slots" }
    }
}
