package com.tiation.dnddiceroller.features.role

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Role Selection Screen for choosing between Player and Dungeon Master roles.
 * Features large, accessible buttons and stores the selection in DataStore.
 * 
 * @param onRoleSelected Callback when a role is selected
 * @param onNavigateBack Callback for back navigation
 * @param viewModel ViewModel for managing role selection state
 * 
 * Built by Garrett Dillman (garrett@sxc.codes) & Tia (tiatheone@protonmail.com)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleSelectionScreen(
    onRoleSelected: (UserRole) -> Unit,
    onNavigateBack: () -> Unit = {},
    viewModel: RoleSelectionViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
            text = "Choose Your Role",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 48.dp)
                .semantics {
                    contentDescription = "Choose your role screen title"
                }
        )
        
        // Player Button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(bottom = 24.dp)
                .semantics {
                    contentDescription = "Select Player role button. Choose this if you are playing the game."
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            onClick = {
                viewModel.saveRole(UserRole.PLAYER)
                onRoleSelected(UserRole.PLAYER)
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "\uD83C\uDFB2", // Dice emoji
                        fontSize = 32.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Player",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Roll dice for your character",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        // Dungeon Master Button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .semantics {
                    contentDescription = "Select Dungeon Master role button. Choose this if you are running the game as a DM."
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            onClick = {
                viewModel.saveRole(UserRole.DUNGEON_MASTER)
                onRoleSelected(UserRole.DUNGEON_MASTER)
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "\uD83D\uDC09", // Dragon emoji
                        fontSize = 32.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Dungeon Master",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "Manage the game and NPCs",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Helper text
        Text(
            text = "You can change this later in settings",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.semantics {
                contentDescription = "Note: You can change this role selection later in settings"
            }
        )
    }
}

/**
 * Enum representing the user's role in the D&D game
 */
enum class UserRole {
    PLAYER,
    DUNGEON_MASTER
}
