package com.tiation.dnddiceroller.navigation

/**
 * Sealed class representing all navigation destinations in the app.
 * Each destination contains its route string for navigation.
 * 
 * Built by Garrett Dillman (garrett@sxc.codes) & Tia (tiatheone@protonmail.com)
 */
sealed class NavDestinations(val route: String) {
    /**
     * Role selection screen - choose between Player and Dungeon Master
     */
    object RoleSelection : NavDestinations("role_selection")
    
    /**
     * Main dice roller screen - the primary screen of the app
     */
    object DiceRoller : NavDestinations("dice_roller")
    
    /**
     * Roll history screen - shows list of all previous dice rolls
     */
    object RollHistory : NavDestinations("roll_history")
    
    /**
     * Roll statistics screen - shows analytics and statistics about dice rolls
     */
    object RollStatistics : NavDestinations("roll_statistics")
}

/**
 * Alias object to match task requirements for Destinations.History and Destinations.Statistics
 * Built by Garrett Dillman (garrett@sxc.codes) & Tia (tiatheone@protonmail.com)
 */
object Destinations {
    val History = NavDestinations.RollHistory
    val Statistics = NavDestinations.RollStatistics
}
