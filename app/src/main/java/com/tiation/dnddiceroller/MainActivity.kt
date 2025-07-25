package com.tiation.dnddiceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DnDDiceRollerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DiceRollerScreen()
                }
            }
        }
    }
}

@Composable
fun DiceRollerScreen(modifier: Modifier = Modifier) {
    var diceResult by remember { mutableStateOf(1) }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "D&D Dice Roller",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Text(
            text = "🎲",
            fontSize = 120.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Text(
            text = "Result: $diceResult",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Button(
            onClick = { 
                diceResult = Random.nextInt(1, 21) // D20 roll
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Roll D20",
                fontSize = 18.sp
            )
        }
        
        Button(
            onClick = { 
                diceResult = Random.nextInt(1, 7) // D6 roll
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Roll D6",
                fontSize = 18.sp
            )
        }
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

@Preview(showBackground = true)
@Composable
fun DiceRollerScreenPreview() {
    DnDDiceRollerTheme {
        DiceRollerScreen()
    }
}
