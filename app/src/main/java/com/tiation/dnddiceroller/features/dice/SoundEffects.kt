package com.tiation.dnddiceroller.features.dice

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.annotation.RawRes

class DiceSoundEffects(private val context: Context) {
    
    private var soundPool: SoundPool? = null
    private var diceRollSoundId: Int = 0
    private var diceLandSoundId: Int = 0
    private var criticalHitSoundId: Int = 0
    private var criticalFailSoundId: Int = 0
    
    init {
        initializeSoundPool()
    }
    
    private fun initializeSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        
        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(audioAttributes)
            .build()
        
        // Load sound effects (these would need to be actual sound files in res/raw/)
        // For now, we'll use system sounds or generated tones
        loadSounds()
    }
    
    private fun loadSounds() {
        soundPool?.let { pool ->
            // These would be actual sound files in res/raw/
            // For demonstration, we'll handle the case where they don't exist
            try {
                // diceRollSoundId = pool.load(context, R.raw.dice_roll, 1)
                // diceLandSoundId = pool.load(context, R.raw.dice_land, 1)
                // criticalHitSoundId = pool.load(context, R.raw.critical_hit, 1)
                // criticalFailSoundId = pool.load(context, R.raw.critical_fail, 1)
            } catch (e: Exception) {
                // Sound files not found, use system sounds or silence
            }
        }
    }
    
    fun playDiceRollSound() {
        soundPool?.play(diceRollSoundId, 0.5f, 0.5f, 1, 0, 1.0f)
    }
    
    fun playDiceLandSound() {
        soundPool?.play(diceLandSoundId, 0.7f, 0.7f, 1, 0, 1.0f)
    }
    
    fun playCriticalHitSound() {
        soundPool?.play(criticalHitSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }
    
    fun playCriticalFailSound() {
        soundPool?.play(criticalFailSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }
    
    fun playResultSound(result: Int, diceType: DiceType) {
        when {
            isCriticalHit(result, diceType) -> playCriticalHitSound()
            isCriticalFail(result, diceType) -> playCriticalFailSound()
            else -> playDiceLandSound()
        }
    }
    
    private fun isCriticalHit(result: Int, diceType: DiceType): Boolean {
        return when (diceType) {
            DiceType.D20 -> result == 20
            DiceType.D12 -> result == 12
            DiceType.D10 -> result == 10
            DiceType.D8 -> result == 8
            DiceType.D6 -> result == 6
            DiceType.D4 -> result == 4
            DiceType.D100 -> result >= 95
        }
    }
    
    private fun isCriticalFail(result: Int, diceType: DiceType): Boolean {
        return result == 1
    }
    
    fun release() {
        soundPool?.release()
        soundPool = null
    }
}

// Extension function to easily use sound effects in composables
@androidx.compose.runtime.Composable
fun rememberDiceSoundEffects(): DiceSoundEffects {
    val context = androidx.compose.ui.platform.LocalContext.current
    return androidx.compose.runtime.remember { DiceSoundEffects(context) }
}

// Cleanup when composable is disposed
@androidx.compose.runtime.Composable
fun DiceSoundEffectsEffect(soundEffects: DiceSoundEffects) {
    androidx.compose.runtime.DisposableEffect(soundEffects) {
        onDispose {
            soundEffects.release()
        }
    }
}
