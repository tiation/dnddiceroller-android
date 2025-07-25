package com.tiation.dnddiceroller.features.dice.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tiation.dnddiceroller.features.dice.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifierSelector(
    modifiers: List<DiceModifier>,
    onModifiersChanged: (List<DiceModifier>) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Modifiers",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            
            IconButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Modifier"
                )
            }
        }
        
        if (modifiers.isEmpty()) {
            Text(
                text = "No modifiers added",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                items(modifiers) { modifier ->
                    ModifierItem(
                        modifier = modifier,
                        onRemove = {
                            onModifiersChanged(modifiers - modifier)
                        }
                    )
                }
            }
        }
    }
    
    if (showAddDialog) {
        AddModifierDialog(
            onAddModifier = { newModifier ->
                onModifiersChanged(modifiers + newModifier)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
}

@Composable
private fun ModifierItem(
    modifier: DiceModifier,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = getModifierTypeDisplayName(modifier.type),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = getModifierDescription(modifier),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(
                onClick = onRemove
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove Modifier",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddModifierDialog(
    onAddModifier: (DiceModifier) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedType by remember { mutableStateOf(ModifierType.FLAT) }
    var value by remember { mutableStateOf("1") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Add Modifier")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Modifier Type Selection
                Text(
                    text = "Modifier Type",
                    style = MaterialTheme.typography.labelMedium
                )
                
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = getModifierTypeDisplayName(selectedType),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        ModifierType.values().forEach { type ->
                            DropdownMenuItem(
                                text = { Text(getModifierTypeDisplayName(type)) },
                                onClick = {
                                    selectedType = type
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                // Value Input (if applicable)
                if (selectedType in listOf(ModifierType.FLAT, ModifierType.MULTIPLY, ModifierType.MIN_VALUE)) {
                    Text(
                        text = "Value",
                        style = MaterialTheme.typography.labelMedium
                    )
                    
                    OutlinedTextField(
                        value = value,
                        onValueChange = { value = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                // Description
                Text(
                    text = getModifierTypeDescription(selectedType),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val modifierValue = if (selectedType == ModifierType.REROLL_ONES) 0 else value.toIntOrNull() ?: 1
                    onAddModifier(DiceModifier(modifierValue, selectedType))
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun getModifierTypeDisplayName(type: ModifierType): String {
    return when (type) {
        ModifierType.FLAT -> "Flat Bonus/Penalty"
        ModifierType.MULTIPLY -> "Multiply Result"
        ModifierType.REROLL_ONES -> "Reroll 1s"
        ModifierType.MIN_VALUE -> "Minimum Value"
    }
}

private fun getModifierTypeDescription(type: ModifierType): String {
    return when (type) {
        ModifierType.FLAT -> "Add or subtract a fixed number from the roll result"
        ModifierType.MULTIPLY -> "Multiply the roll result by this value"
        ModifierType.REROLL_ONES -> "Reroll any result of 1 (automatic)"
        ModifierType.MIN_VALUE -> "Set a minimum value for the roll result"
    }
}

private fun getModifierDescription(modifier: DiceModifier): String {
    return when (modifier.type) {
        ModifierType.FLAT -> if (modifier.value >= 0) "+${modifier.value}" else "${modifier.value}"
        ModifierType.MULTIPLY -> "×${modifier.value}"
        ModifierType.REROLL_ONES -> "Reroll 1s"
        ModifierType.MIN_VALUE -> "Min: ${modifier.value}"
    }
}
