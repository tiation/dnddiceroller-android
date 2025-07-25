package com.tiation.dnddiceroller.features.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tiation.dnddiceroller.DiceRoll
import com.tiation.dnddiceroller.navigation.Destinations

/**
 * Screen that displays the history of all dice rolls
 * 
 * @param rollHistory List of dice rolls to display
 * @param onNavigateBack Callback to handle back navigation
 * @param navController NavController for navigating to statistics screen
 * 
 * Built by Garrett Dillman (garrett@sxc.codes) & Tia (tiatheone@protonmail.com)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RollHistoryScreen(
    rollHistory: List<DiceRoll>,
    onNavigateBack: () -> Unit,
    navController: NavController? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text("Roll History") 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Statistics button
                    IconButton(
                        onClick = { navController?.navigate(Destinations.Statistics.route) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Statistics"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            // Alternative FAB for Statistics
            if (rollHistory.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { navController?.navigate(Destinations.Statistics.route) }
                ) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = "View Statistics"
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (rollHistory.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No rolls yet!",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Start rolling some dice to see your history here.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                // History list
                Text(
                    text = "Total Rolls: ${rollHistory.size}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(rollHistory) { roll ->
                        RollHistoryItem(roll = roll)
                    }
                }
            }
        }
    }
}

@Composable
private fun RollHistoryItem(roll: DiceRoll) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "d${roll.sides}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Roll: ${roll.result}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = roll.result.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
