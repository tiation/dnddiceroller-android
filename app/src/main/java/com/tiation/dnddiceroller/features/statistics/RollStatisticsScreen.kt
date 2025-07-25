package com.tiation.dnddiceroller.features.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tiation.dnddiceroller.DiceRoll

/**
 * Placeholder screen for roll statistics and analytics
 * This will be implemented in future iterations with charts and data visualization
 * 
 * @param rollHistory List of dice rolls to analyze
 * @param onNavigateBack Callback to handle back navigation
 * 
 * Built by Garrett Dillman (garrett@sxc.codes) & Tia (tiatheone@protonmail.com)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RollStatisticsScreen(
    rollHistory: List<DiceRoll>,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text("Roll Statistics") 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
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
                            text = "No data to analyze!",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Roll some dice to see statistics and analytics here.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Statistics",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // Basic statistics cards
                    BasicStatisticsCard(rollHistory = rollHistory)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Coming Soon",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Text(
                        text = "• Detailed roll distribution charts\n" +
                              "• Dice type frequency analysis\n" +
                              "• Lucky/unlucky streak tracking\n" +
                              "• Rolling patterns over time\n" +
                              "• Export statistics data",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun BasicStatisticsCard(rollHistory: List<DiceRoll>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Basic Statistics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Divider()
            
            val totalRolls = rollHistory.size
            val averageRoll = if (totalRolls > 0) rollHistory.map { it.result }.average() else 0.0
            val highestRoll = rollHistory.maxOfOrNull { it.result } ?: 0
            val lowestRoll = rollHistory.minOfOrNull { it.result } ?: 0
            val mostUsedDie = rollHistory.groupBy { it.sides }
                .maxByOrNull { it.value.size }?.key ?: 0
            
            StatisticRow("Total Rolls", totalRolls.toString())
            StatisticRow("Average Roll", String.format("%.1f", averageRoll))
            StatisticRow("Highest Roll", highestRoll.toString())
            StatisticRow("Lowest Roll", lowestRoll.toString())
            StatisticRow("Most Used Die", "d$mostUsedDie")
        }
    }
}

@Composable
private fun StatisticRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
