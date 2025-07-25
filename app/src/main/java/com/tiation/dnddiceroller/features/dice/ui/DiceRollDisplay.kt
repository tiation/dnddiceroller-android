package com.tiation.dnddiceroller.features.dice.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tiation.dnddiceroller.features.dice.*

data class RollResult(
    val slot: DiceSlotConfiguration,
    val result: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Composable
fun DiceRollDisplay(
    roleConfiguration: RoleConfiguration,
    modifier: Modifier = Modifier
) {
    var rollResults by remember { mutableStateOf(emptyList<RollResult>()) }
    var isRolling by remember { mutableStateOf(false) }
    val diceRoller = remember { DiceRoller() }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "${roleConfiguration.name} - Dice Roller",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Dice Grid
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            // Group dice slots into rows of 5
            val chunkedSlots = roleConfiguration.slots.chunked(5)
            
            items(chunkedSlots) { rowSlots ->
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(rowSlots) { slot ->
                        DiceRollCard(
                            slot = slot,
                            isRolling = isRolling,
                            lastResult = rollResults.find { it.slot.id == slot.id }?.result,
                            onRoll = { rollResult ->
                                rollResults = rollResults.filter { it.slot.id != slot.id } + rollResult
                            },
                            diceRoller = diceRoller,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
        
        // Roll History
        if (rollResults.isNotEmpty()) {
            Text(
                text = "Recent Rolls",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            LazyColumn(
                modifier = Modifier.height(120.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(rollResults.sortedByDescending { it.timestamp }.take(5)) { result ->
                    RollHistoryItem(result = result)
                }
            }
        }
        
        // Roll All Button
        Button(
            onClick = {
                isRolling = true
                val newResults = roleConfiguration.slots.map { slot ->
                    val result = diceRoller.roll(
                        diceType = slot.diceType,
                        rollType = slot.rollType,
                        modifiers = slot.modifiers
                    )
                    RollResult(slot, result)
                }
                rollResults = newResults
                isRolling = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = !isRolling
        ) {
            Text(
                text = if (isRolling) "Rolling..." else "Roll All Dice"
            )
        }
    }
}

@Composable
private fun DiceRollCard(
    slot: DiceSlotConfiguration,
    isRolling: Boolean,
    lastResult: Int?,
    onRoll: (RollResult) -> Unit,
    diceRoller: DiceRoller,
    modifier: Modifier = Modifier
) {
    var isIndividualRolling by remember { mutableStateOf(false) }
    
    Card(
        onClick = {
            if (!isRolling && !isIndividualRolling) {
                isIndividualRolling = true
                val result = diceRoller.roll(
                    diceType = slot.diceType,
                    rollType = slot.rollType,
                    modifiers = slot.modifiers
                )
                onRoll(RollResult(slot, result))
                isIndividualRolling = false
            }
        },
        modifier = modifier
            .aspectRatio(1f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            DiceAnimation(
                isRolling = isRolling || isIndividualRolling,
                diceType = slot.diceType,
                onRollComplete = { /* Animation complete */ }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = slot.diceType.name,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    AnimatedVisibility(
                        visible = lastResult != null,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        lastResult?.let { result ->
                            Text(
                                text = result.toString(),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    
                    Text(
                        text = slot.name,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                    
                    // Show roll type indicator
                    when (slot.rollType) {
                        RollType.ADVANTAGE -> Text(
                            text = "ADV",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        RollType.DISADVANTAGE -> Text(
                            text = "DIS",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        RollType.NORMAL -> { /* No indicator */ }
                    }
                }
            }
        }
    }
}

@Composable
private fun RollHistoryItem(
    result: RollResult,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = result.slot.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = result.slot.diceType.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Text(
                text = result.result.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
