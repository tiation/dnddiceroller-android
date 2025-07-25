package com.tiation.dnddiceroller.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tiation.dnddiceroller.DiceRoll
import com.tiation.dnddiceroller.DiceRollerApp
import com.tiation.dnddiceroller.features.history.RollHistoryScreen
import com.tiation.dnddiceroller.features.statistics.RollStatisticsScreen

/**
 * Main navigation graph for the DnD Dice Roller app.
 * Hosts all screens and manages navigation between them.
 * 
 * @param navController The navigation controller for handling navigation actions
 * @param rollHistory Shared roll history state across all screens
 * @param onRollHistoryUpdate Callback to update the roll history from dice roller screen
 * 
 * Built by Garrett Dillman (garrett@sxc.codes) & Tia (tiatheone@protonmail.com)
 */
@Composable
fun DiceRollerNavGraph(
    navController: NavHostController,
    rollHistory: List<DiceRoll>,
    onRollHistoryUpdate: (List<DiceRoll>) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = NavDestinations.DiceRoller.route
    ) {
        // Main dice roller screen
        composable(route = NavDestinations.DiceRoller.route) {
            DiceRollerApp(
                rollHistory = rollHistory,
                onRollHistoryUpdate = onRollHistoryUpdate,
                onNavigateToHistory = {
                    navController.navigate(NavDestinations.RollHistory.route)
                },
                onNavigateToStatistics = {
                    navController.navigate(NavDestinations.RollStatistics.route)
                }
            )
        }
        
        // Roll history screen
        composable(route = NavDestinations.RollHistory.route) {
            RollHistoryScreen(
                rollHistory = rollHistory,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
        
        // Roll statistics screen  
        composable(route = NavDestinations.RollStatistics.route) {
            RollStatisticsScreen(
                rollHistory = rollHistory,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
