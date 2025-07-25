package com.tiation.dnddiceroller.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tiation.dnddiceroller.ui.components.*
import com.tiation.dnddiceroller.ui.theme.DiceRollerTheme
import com.tiation.dnddiceroller.ui.theme.LocalDiceRollerCustomTheme

@Composable
fun MainScreen() {
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

                // Dice Buttons
                val diceTypes = if (isDMMode) {
                    listOf("D4", "D6", "D8", "D10", "D12", "D20", "D100")
                } else {
                    listOf("D20", "D6", "D8", "D10")
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        // Dice Grid
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            diceTypes.chunked(3).forEach { rowDice ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    rowDice.forEach { diceType ->
                                        DiceButton(
                                            diceType = diceType,
                                            onClick = {
                                                val max = diceType.removePrefix("D").toInt()
                                                val result = (1..max).random()
                                                rollResults = listOf(result to diceType) + rollResults
                                            },
                                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                                        )
                                    }
                                    // Fill empty slots with spacers for alignment
                                    repeat(3 - rowDice.size) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }

                    // Roll Results
                    items(rollResults) { (result, diceType) ->
                        RollResult(
                            result = result,
                            diceType = diceType,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

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
