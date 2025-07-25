package com.tiation.dnddiceroller.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tiation.dnddiceroller.ui.components.*
import com.tiation.dnddiceroller.ui.theme.DiceRollerTheme
import com.tiation.dnddiceroller.ui.theme.LocalDiceRollerCustomTheme
import com.tiation.dnddiceroller.DiceEngine
import com.tiation.dnddiceroller.RollLogger
import com.tiation.dnddiceroller.navigation.Destinations

@Composable
fun MainScreen(
    navController: NavController? = null
) {
    var isDMMode by remember { mutableStateOf(false) }
    var isAccessibilityMode by remember { mutableStateOf(false) }
    var rollResults by remember { mutableStateOf(listOf<Pair<Int, String>>()) }

    DiceRollerTheme(
        isDMMode = isDMMode,
        isAccessibilityMode = isAccessibilityMode
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top Bar with Mode Toggles
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isDMMode) "DM Mode" else "Player Mode",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // History Button next to mode toggles
                        IconButton(onClick = { navController?.navigate(Destinations.History.route) }) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "History"
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        AccessibilityToggle(
                            isEnabled = isAccessibilityMode,
                            onToggle = { isAccessibilityMode = it }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        ModeToggleButton(
                            isDMMode = isDMMode,
                            onModeChange = { isDMMode = it }
                        )
                    }
                }

                // Render General Dice Roller Screen
                val diceEngine = DiceEngine()
                val rollLogger = RollLogger { result, diceType ->
                    rollResults = listOf(result to diceType) + rollResults
                }
                
                GeneralDiceRollerScreen(
                    diceEngine = diceEngine,
                    rollLogger = rollLogger,
                    modifier = Modifier.weight(1f)
                )

                // DM Mode Special Features
                if (isDMMode) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { rollResults = emptyList() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Clear History")
                        }
                        Button(
                            onClick = {
                                // Implement hidden roll functionality
                                val d20Result = (1..20).random()
                                // Store result but don't display it
                            }
                        ) {
                            Text("Hidden Roll")
                        }
                    }
                }
            }
        }
    }
}
