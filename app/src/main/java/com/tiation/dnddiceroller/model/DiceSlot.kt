package com.tiation.dnddiceroller.model

enum class RollType {
    NORMAL,
    ADVANTAGE,
    DISADVANTAGE
}

data class DiceSlot(
    val id: String,
    val name: String,
    val diceType: Int,  // e.g. 4, 6, 8, 10, 12, 20, 100
    val modifier: Int,  // -10 to +10
    val rollType: RollType,
    val isExpanded: Boolean = false
)
