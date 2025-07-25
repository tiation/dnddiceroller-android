package com.tiation.dnddiceroller.navigation

import androidx.compose.runtime.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tiation.dnddiceroller.DiceRoll
import com.tiation.dnddiceroller.DiceRollerApp
import com.tiation.dnddiceroller.features.history.RollHistoryScreen
import com.tiation.dnddiceroller.features.role.RoleSelectionScreen
import com.tiation.dnddiceroller.features.role.RoleSelectionViewModel
import com.tiation.dnddiceroller.features.statistics.RollStatisticsScreen

/**
 * Main navigation graph for the DnD Dice Roller app.
 * Hosts all screens and manages navigation between them.
 * Conditionally shows Role Selection if no role is stored, otherwise goes directly to Dice screen.
 * 
 * @param navController The navigation controller for handling navigation actions
 * @param rollHistory Shared roll history state across all screens
 * @param onRollHistoryUpdate Callback to update the roll history from dice roller screen
 * @param startDestination The initial destination route
 * 
 * Built by Garrett Dillman (garrett@sxc.codes) & Tia (tiatheone@protonmail.com)
 */
@Composable
fun DiceRollerNavGraph(
    navController: NavHostController,
    rollHistory: List<DiceRoll>,
    onRollHistoryUpdate: (List<DiceRoll>) -> Unit,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Role selection screen
        composable(route = NavDestinations.RoleSelection.route) {
            RoleSelectionScreen(
                onRoleSelected = { role ->
                    // Navigate to dice roller after role selection
                    navController.navigate(NavDestinations.DiceRoller.route) {
                        // Clear the role selection from back stack
                        popUpTo(NavDestinations.RoleSelection.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
        
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
                },
                navController = navController
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
