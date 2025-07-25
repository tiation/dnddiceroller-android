package com.dnddiceroller.accessibility

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log

class VoiceCommandManager(private val context: Context) {
    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening = false
    private var commandListener: VoiceCommandListener? = null

    companion object {
        private const val TAG = "VoiceCommandManager"
    }

    interface VoiceCommandListener {
        fun onRollDice()
        fun onIncreaseModifier()
        fun onDecreaseModifier()
        fun onSwitchDice()
        fun onError(error: String)
    }

    fun initialize(listener: VoiceCommandListener) {
        commandListener = listener
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
            setupRecognitionListener()
        } else {
            commandListener?.onError("Voice recognition not available on this device")
        }
    }

    fun startListening() {
        if (!isListening && speechRecognizer != null) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            }
            
            speechRecognizer?.startListening(intent)
            isListening = true
        }
    }

    fun stopListening() {
        if (isListening) {
            speechRecognizer?.stopListening()
            isListening = false
        }
    }

    private fun setupRecognitionListener() {
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Log.d(TAG, "Ready for speech")
            }

            override fun onBeginningOfSpeech() {
                Log.d(TAG, "Speech began")
            }

            override fun onRmsChanged(rmsdB: Float) {
                // Handle audio level changes if needed
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // Handle buffer if needed
            }

            override fun onEndOfSpeech() {
                isListening = false
            }

            override fun onError(error: Int) {
                isListening = false
                val errorMessage = getErrorMessage(error)
                commandListener?.onError(errorMessage)
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.get(0)?.let { command ->
                    processCommand(command.toLowerCase())
                }
                isListening = false
            }

            override fun onPartialResults(partialResults: Bundle?) {
                // Handle partial results if needed
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // Handle events if needed
            }
        })
    }

    private fun processCommand(command: String) {
        when {
            command.contains("roll") && command.contains("dice") -> {
                commandListener?.onRollDice()
            }
            command.contains("increase") && command.contains("modifier") -> {
                commandListener?.onIncreaseModifier()
            }
            command.contains("decrease") && command.contains("modifier") -> {
                commandListener?.onDecreaseModifier()
            }
            command.contains("switch") && command.contains("dice") -> {
                commandListener?.onSwitchDice()
            }
        }
    }

    private fun getErrorMessage(error: Int): String {
        return when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> "No match found"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognition service busy"
            SpeechRecognizer.ERROR_SERVER -> "Server error"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Unknown error"
        }
    }

    fun release() {
        speechRecognizer?.destroy()
        speechRecognizer = null
        commandListener = null
    }
}
