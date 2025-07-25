package com.tiation.dnddiceroller.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tiation.dnddiceroller.ui.theme.LocalDiceRollerCustomTheme

@Composable
fun DiceButton(
    diceType: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val customTheme = LocalDiceRollerCustomTheme.current
    val accessibility = customTheme.isAccessibilityMode
    
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(if (accessibility) 8.dp else 12.dp),
        modifier = modifier
            .semantics {
                contentDescription = "Roll $diceType dice"
                role = Role.Button
            }
            .border(
                width = if (accessibility) 2.dp else 1.dp,
                color = customTheme.accentBorderColor,
                shape = RoundedCornerShape(if (accessibility) 8.dp else 12.dp)
            )
    ) {
        Text(
            text = diceType,
            fontSize = if (accessibility) 20.sp else 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RollResult(
    result: Int,
    diceType: String,
    modifier: Modifier = Modifier
) {
    val customTheme = LocalDiceRollerCustomTheme.current
    
    Card(
        modifier = modifier
            .semantics {
                contentDescription = "Roll result: $result on $diceType"
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = result.toString(),
                color = customTheme.resultTextColor,
                fontSize = if (customTheme.isAccessibilityMode) 32.sp else 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = diceType,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = if (customTheme.isAccessibilityMode) 18.sp else 14.sp
            )
        }
    }
}

@Composable
fun ModeToggleButton(
    isDMMode: Boolean,
    onModeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val customTheme = LocalDiceRollerCustomTheme.current
    
    Switch(
        checked = isDMMode,
        onCheckedChange = onModeChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.primary,
            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
            uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
            uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = modifier
            .semantics {
                contentDescription = if (isDMMode) "Switch to Player Mode" else "Switch to DM Mode"
            }
    )
}

@Composable
fun AccessibilityToggle(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .semantics {
                contentDescription = "Accessibility mode toggle"
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Accessibility Mode",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
}
