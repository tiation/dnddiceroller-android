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
import androidx.hilt.navigation.compose.hiltViewModel
import com.tiation.dnddiceroller.features.general.GeneralDiceRollerViewModel

@Composable
fun GeneralDiceRollerScreen(
    modifier: Modifier = Modifier,
    viewModel: GeneralDiceRollerViewModel = hiltViewModel()
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
                        viewModel.rollStandardDie(sides)
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
                        viewModel.rollCustomDie(customSides)
                    }
                ) {
                    Text("Roll")
                }
            }
        }
    }
}
