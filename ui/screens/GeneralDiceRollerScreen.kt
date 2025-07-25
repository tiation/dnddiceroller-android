package com.tiation.dnddiceroller.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tiation.dnddiceroller.DiceEngine
import com.tiation.dnddiceroller.RollLogger

@Composable
fun GeneralDiceRollerScreen(
    diceEngine: DiceEngine,
    rollLogger: RollLogger,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Standard dice grid
        val standardDice = listOf(4, 6, 8, 10, 12, 20, 100)
        
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 100.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(standardDice) { sides ->
                Button(
                    onClick = {
                        val result = diceEngine.rollDie(sides)
                        rollLogger.logRoll("d$sides", result)
                    },
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxSize()
                ) {
                    Text("d$sides")
                }
            }
        }

        // Custom die roller
        var customSides by remember { mutableStateOf(2) }
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { if (customSides > 2) customSides-- },
                    enabled = customSides > 2
                ) {
                    Text("-")
                }
                
                Text(
                    text = "d$customSides",
                    style = MaterialTheme.typography.titleLarge
                )
                
                IconButton(
                    onClick = { customSides++ }
                ) {
                    Text("+")
                }
                
                Button(
                    onClick = {
                        val result = diceEngine.rollDie(customSides)
                        rollLogger.logRoll("d$customSides", result)
                    }
                ) {
                    Text("Roll")
                }
            }
        }
    }
}
