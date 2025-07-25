package com.tiation.dnddiceroller.features.dice.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tiation.dnddiceroller.features.dice.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiceConfigurationScreen(
    modifier: Modifier = Modifier,
    onRoleSelected: (RoleConfiguration) -> Unit = {}
) {
    var selectedRole by remember { mutableStateOf<RoleConfiguration?>(null) }
    var diceSlots by remember { mutableStateOf(emptyList<DiceSlotConfiguration>()) }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Dice Configuration",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Role Selection
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Character Role",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                // Role selector (simplified for now)
                val roles = listOf("Warrior", "Mage", "Rogue", "Cleric", "Ranger")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(roles) { role ->
                        FilterChip(
                            onClick = { 
                                selectedRole = RoleConfiguration(
                                    id = roles.indexOf(role),
                                    name = role
                                )
                            },
                            label = { Text(role) },
                            selected = selectedRole?.name == role
                        )
                    }
                }
            }
        }
        
        selectedRole?.let { role ->
            // Dice Slots Grid (25 slots max)
            Text(
                text = "Dice Slots (${diceSlots.size}/25)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Existing dice slots
                items(diceSlots) { slot ->
                    DiceSlotCard(
                        slot = slot,
                        onEdit = { editedSlot ->
                            diceSlots = diceSlots.map { 
                                if (it.id == editedSlot.id) editedSlot else it 
                            }
                        },
                        onRemove = { removedSlot ->
                            diceSlots = diceSlots.filter { it.id != removedSlot.id }
                        }
                    )
                }
                
                // Add new slot button (if under 25 limit)
                if (diceSlots.size < 25) {
                    item {
                        AddDiceSlotCard(
                            onAdd = { newSlot ->
                                diceSlots = diceSlots + newSlot.copy(id = diceSlots.size)
                            }
                        )
                    }
                }
            }
            
            // Save Configuration Button
            Button(
                onClick = { 
                    onRoleSelected(role.copy(slots = diceSlots))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Save Configuration")
            }
        }
    }
}

@Composable
private fun DiceSlotCard(
    slot: DiceSlotConfiguration,
    onEdit: (DiceSlotConfiguration) -> Unit,
    onRemove: (DiceSlotConfiguration) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    
    Card(
        onClick = { showDialog = true },
        modifier = modifier
            .aspectRatio(1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = slot.diceType.name,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = slot.name,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
            if (slot.modifiers.isNotEmpty()) {
                Text(
                    text = "+${slot.modifiers.sumOf { it.value }}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
    
    if (showDialog) {
        DiceSlotEditDialog(
            slot = slot,
            onSave = { editedSlot ->
                onEdit(editedSlot)
                showDialog = false
            },
            onDelete = {
                onRemove(slot)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
private fun AddDiceSlotCard(
    onAdd: (DiceSlotConfiguration) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    
    Card(
        onClick = { showDialog = true },
        modifier = modifier
            .aspectRatio(1f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
    
    if (showDialog) {
        DiceSlotEditDialog(
            slot = DiceSlotConfiguration(
                id = 0,
                name = "New Dice",
                diceType = DiceType.D20
            ),
            onSave = { newSlot ->
                onAdd(newSlot)
                showDialog = false
            },
            onDelete = null,
            onDismiss = { showDialog = false }
        )
    }
}
