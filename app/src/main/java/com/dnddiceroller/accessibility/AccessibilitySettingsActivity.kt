package com.dnddiceroller.accessibility

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.dnddiceroller.R
import com.google.android.material.button.MaterialButton

class AccessibilitySettingsActivity : AppCompatActivity() {
    private lateinit var accessibilityHelper: AccessibilityHelper
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accessibility_settings)
        
        accessibilityHelper = AccessibilityHelper(this)
        
        setupHighContrastSwitch()
        setupFontSizeControl()
        setupVoiceControl()
    }
    
    private fun setupHighContrastSwitch() {
        val highContrastSwitch = findViewById<SwitchCompat>(R.id.switchHighContrast)
        
        // Set initial state
        highContrastSwitch.isChecked = accessibilityHelper.isHighContrastEnabled()
        
        // Setup accessibility
        accessibilityHelper.setupAccessibilityFocus(
            highContrastSwitch,
            getString(R.string.cd_high_contrast_switch)
        )
        
        highContrastSwitch.setOnCheckedChangeListener { _, isChecked ->
            accessibilityHelper.setHighContrastMode(isChecked)
            // Recreate activity to apply theme changes
            recreate()
        }
    }
    
    private fun setupFontSizeControl() {
        val fontSizeSeekBar = findViewById<SeekBar>(R.id.seekBarFontSize)
        val currentScale = accessibilityHelper.getFontScale() * 100
        fontSizeSeekBar.progress = currentScale.toInt()
        
        // Setup accessibility
        accessibilityHelper.setupAccessibilityFocus(
            fontSizeSeekBar,
            getString(R.string.cd_font_size_slider)
        )
        
        fontSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val scale = progress / 100f
                    accessibilityHelper.setFontScale(scale)
                    // Update text sizes in real-time
                    updateTextSizes()
                    
                    // Announce change for accessibility
                    accessibilityHelper.announceStateChange(
                        seekBar,
                        getString(R.string.cd_font_size_changed, progress)
                    )
                }
            }
            
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }
    
    private fun setupVoiceControl() {
        val voiceControlButton = findViewById<MaterialButton>(R.id.btnVoiceControl)
        
        // Setup accessibility
        accessibilityHelper.setupAccessibilityFocus(
            voiceControlButton,
            getString(R.string.cd_voice_control_button)
        )
        
        voiceControlButton.setOnClickListener {
            // Launch voice control configuration
            // This would be implemented based on your voice control implementation
        }
    }
    
    private fun updateTextSizes() {
        // Recursively update text sizes in the current view hierarchy
        val rootView = window.decorView.findViewById<android.view.View>(android.R.id.content)
        updateTextSizesRecursively(rootView)
    }
    
    private fun updateTextSizesRecursively(view: android.view.View) {
        if (view is android.widget.TextView) {
            accessibilityHelper.applyFontScale(view)
        } else if (view is android.view.ViewGroup) {
            for (i in 0 until view.childCount) {
                updateTextSizesRecursively(view.getChildAt(i))
            }
        }
    }
}
