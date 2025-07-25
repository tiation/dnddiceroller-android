package com.tiation.dnddiceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.tiation.dnddiceroller.navigation.DiceRollerNavGraph
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DnDDiceRollerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DiceRollerNavRoot()
                }
            }
        }
    }
}

/**
 * Main navigation root composable that creates nav controller and manages shared state
 * Built by Garrett Dillman (garrett@sxc.codes) & Tia (tiatheone@protonmail.com)
 */
@Composable
fun DiceRollerNavRoot() {
    val navController = rememberNavController()
    var rollHistory by remember { mutableStateOf(emptyList<DiceRoll>()) }
    
    DiceRollerNavGraph(
        navController = navController,
        rollHistory = rollHistory,
        onRollHistoryUpdate = { newHistory ->
            rollHistory = newHistory
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiceRollerApp(
    rollHistory: List<DiceRoll> = emptyList(),
    onRollHistoryUpdate: ((List<DiceRoll>) -> Unit)? = null,
    onNavigateToHistory: (() -> Unit)? = null,
    onNavigateToStatistics: (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text("D&D Dice Roller") 
                },
                actions = {
                    // History button
                    IconButton(
                        onClick = { onNavigateToHistory?.invoke() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Roll History"
                        )
                    }
                    // Statistics button
                    IconButton(
                        onClick = { onNavigateToStatistics?.invoke() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Roll Statistics"
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
            // Dice buttons
            val diceTypes = listOf(4, 6, 8, 10, 12, 20, 100)
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Select a die to roll:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                
                items(diceTypes.chunked(3)) { rowDice ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowDice.forEach { sides ->
                            Button(
                                onClick = {
                                    val roll = Random.nextInt(1, sides + 1)
                                    val newHistory = listOf(DiceRoll(sides, roll)) + rollHistory
                                    onRollHistoryUpdate?.invoke(newHistory)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("d$sides")
                            }
                        }
                        // Fill remaining space if row is not full
                        repeat(3 - rowDice.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
                
                item {
                    if (rollHistory.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Recent Rolls:",
                                style = MaterialTheme.typography.titleMedium
                            )
                            TextButton(
                                onClick = { onNavigateToHistory?.invoke() }
                            ) {
                                Text("View All")
                            }
                        }
                    }
                }
                
                items(rollHistory.take(10)) { roll ->
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
                            Text(
                                text = "d${roll.sides}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = roll.result.toString(),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

data class DiceRoll(
    val sides: Int,
    val result: Int
)

@Composable
fun DnDDiceRollerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}
