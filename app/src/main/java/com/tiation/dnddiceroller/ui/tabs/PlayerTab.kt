package com.tiation.dnddiceroller.ui.tabs

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.tiation.dnddiceroller.model.DiceSlot
import com.tiation.dnddiceroller.ui.screens.RoleDiceScreen
import com.tiation.dnddiceroller.viewmodel.RoleDiceViewModel

@Composable
fun PlayerTab(
    dataStore: DataStore<Preferences>
) {
    val viewModel = remember { RoleDiceViewModel(dataStore, "player") }
    
    RoleDiceScreen(
        viewModel = viewModel,
        onRoll = { diceSlot ->
            // TODO: Implement roll logic
        }
    )
}
