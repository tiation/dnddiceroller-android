package com.tiation.dnddiceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.tiation.dnddiceroller.features.dice.RoleConfiguration
import com.tiation.dnddiceroller.features.dice.ui.DiceConfigurationScreen
import com.tiation.dnddiceroller.features.dice.ui.DiceRollDisplay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DnDDiceRollerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DiceRollerApp()
                }
            }
        }
    }
}

@Composable
fun DiceRollerApp() {
    var currentRole by remember { mutableStateOf<RoleConfiguration?>(null) }
    
    if (currentRole == null) {
        DiceConfigurationScreen(
            onRoleSelected = { role ->
                currentRole = role
            }
        )
    } else {
        DiceRollDisplay(
            roleConfiguration = currentRole!!
        )
    }
}

@Composable
fun DnDDiceRollerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}
