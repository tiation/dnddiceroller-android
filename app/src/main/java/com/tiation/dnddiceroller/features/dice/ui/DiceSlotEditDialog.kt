package com.tiation.dnddiceroller.features.dice.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tiation.dnddiceroller.features.dice.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiceSlotEditDialog(
    slot: DiceSlotConfiguration,
    onSave: (DiceSlotConfiguration) -> Unit,
    onDelete: (() -> Unit)?,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(slot.name) }
    var selectedDiceType by remember { mutableStateOf(slot.diceType) }
    var selectedRollType by remember { mutableStateOf(slot.rollType) }
    var modifiers by remember { mutableStateOf(slot.modifiers) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (onDelete != null) "Edit Dice Slot" else "Add Dice Slot")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Name Input
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Dice Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Dice Type Selection
                Text(
                    text = "Dice Type",
                    style = MaterialTheme.typography.labelMedium
                )
                
                var diceTypeExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = diceTypeExpanded,
                    onExpandedChange = { diceTypeExpanded = !diceTypeExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedDiceType.name,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = diceTypeExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = diceTypeExpanded,
                        onDismissRequest = { diceTypeExpanded = false }
                    ) {
                        DiceType.values().forEach { diceType ->
                            DropdownMenuItem(
                                text = { Text(diceType.name) },
                                onClick = {
                                    selectedDiceType = diceType
                                    diceTypeExpanded = false
                                }
                            )
                        }
                    }
                }
                
                // Roll Type Selection
                Text(
                    text = "Roll Type",
                    style = MaterialTheme.typography.labelMedium
                )
                
                var rollTypeExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = rollTypeExpanded,
                    onExpandedChange = { rollTypeExpanded = !rollTypeExpanded }
                ) {
                    OutlinedTextField(
                        value = getRollTypeDisplayName(selectedRollType),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = rollTypeExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = rollTypeExpanded,
                        onDismissRequest = { rollTypeExpanded = false }
                    ) {
                        RollType.values().forEach { rollType ->
                            DropdownMenuItem(
                                text = { Text(getRollTypeDisplayName(rollType)) },
                                onClick = {
                                    selectedRollType = rollType
                                    rollTypeExpanded = false
                                }
                            )
                        }
                    }
                }
                
                // Modifiers Section
                ModifierSelector(
                    modifiers = modifiers,
                    onModifiersChanged = { modifiers = it }
                )
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (onDelete != null) {
                    TextButton(
                        onClick = onDelete,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete")
                    }
                }
                
                TextButton(
                    onClick = {
                        val updatedSlot = slot.copy(
                            name = name.ifBlank { "Unnamed Dice" },
                            diceType = selectedDiceType,
                            rollType = selectedRollType,
                            modifiers = modifiers
                        )
                        onSave(updatedSlot)
                    }
                ) {
                    Text("Save")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun getRollTypeDisplayName(rollType: RollType): String {
    return when (rollType) {
        RollType.NORMAL -> "Normal"
        RollType.ADVANTAGE -> "Advantage"
        RollType.DISADVANTAGE -> "Disadvantage"
    }
}
