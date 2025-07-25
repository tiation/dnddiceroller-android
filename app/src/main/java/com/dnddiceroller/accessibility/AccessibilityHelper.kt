package com.dnddiceroller.accessibility

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import androidx.core.content.ContextCompat

/**
 * Helper class to manage accessibility features throughout the app
 */
class AccessibilityHelper(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("accessibility_prefs", Context.MODE_PRIVATE)
    private val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager

    companion object {
        const val KEY_HIGH_CONTRAST = "high_contrast_mode"
        const val KEY_FONT_SCALE = "font_scale"
        const val DEFAULT_FONT_SCALE = 1.0f
    }

    /**
     * High Contrast Mode Management
     */
    fun isHighContrastEnabled(): Boolean {
        return prefs.getBoolean(KEY_HIGH_CONTRAST, false)
    }

    fun setHighContrastMode(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_HIGH_CONTRAST, enabled).apply()
        // Theme updates should be handled by the activity/fragment
    }

    /**
     * Font Scaling Management
     */
    fun getFontScale(): Float {
        return prefs.getFloat(KEY_FONT_SCALE, DEFAULT_FONT_SCALE)
    }

    fun setFontScale(scale: Float) {
        prefs.edit().putFloat(KEY_FONT_SCALE, scale).apply()
    }

    /**
     * Apply font scaling to a TextView
     */
    fun applyFontScale(textView: TextView) {
        textView.textSize = textView.textSize * getFontScale()
    }

    /**
     * TalkBack Announcements
     */
    fun announceForAccessibility(view: View, text: String) {
        if (accessibilityManager.isEnabled) {
            val event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_ANNOUNCEMENT)
            event.text.add(text)
            view.sendAccessibilityEvent(event)
        }
    }

    /**
     * Dice Roll Announcement
     */
    fun announceDiceRoll(view: View, diceType: String, result: Int, modifier: Int, total: Int) {
        val announcement = context.getString(
            R.string.cd_roll_result,
            result,
            diceType,
            modifier,
            total
        )
        announceForAccessibility(view, announcement)
    }

    /**
     * Apply high contrast colors if enabled
     */
    fun applyHighContrastIfEnabled(view: View) {
        if (isHighContrastEnabled()) {
            when (view) {
                is TextView -> {
                    view.setTextColor(ContextCompat.getColor(context, R.color.high_contrast_on_background))
                }
                else -> {
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.high_contrast_background))
                }
            }
        }
    }

    /**
     * Setup accessibility focus for important views
     */
    fun setupAccessibilityFocus(view: View, description: String) {
        view.contentDescription = description
        view.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
    }

    /**
     * Setup accessibility actions for custom interactions
     */
    fun setupCustomAction(view: View, actionLabel: String, action: () -> Unit) {
        view.accessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun performAccessibilityAction(host: View, action: Int, args: android.os.Bundle?): Boolean {
                if (action == AccessibilityNodeInfo.ACTION_CLICK) {
                    action()
                    return true
                }
                return super.performAccessibilityAction(host, action, args)
            }
        }
    }
    
    /**
     * Update accessibility state announcements
     */
    fun announceStateChange(view: View, newState: String) {
        if (accessibilityManager.isEnabled) {
            view.announceForAccessibility(newState)
        }
    }

    /**
     * Setup accessibility heading
     */
    fun setAsHeading(view: View) {
        view.isAccessibilityHeading = true
    }
}
