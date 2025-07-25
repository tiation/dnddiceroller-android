# Accessibility Guidelines

The D&D Dice Roller Android app is designed with accessibility as a core principle, ensuring that all users, including those with disabilities, can fully enjoy the dice rolling experience. This document outlines our accessibility implementation and guidelines.

## 🎯 Accessibility Goals

Our app strives to meet and exceed the following standards:
- **WCAG 2.1 AA compliance**
- **Android Accessibility Guidelines**
- **Section 508 compliance**
- **Inclusive design principles**

## ♿ Core Principles

### 1. Perceivable
Information and UI components must be presentable to users in ways they can perceive.

### 2. Operable
UI components and navigation must be operable by all users.

### 3. Understandable
Information and UI operation must be understandable.

### 4. Robust
Content must be robust enough to be interpreted by a wide variety of user agents, including assistive technologies.

## 🔍 Screen Reader Support

### TalkBack Optimization
Our app is fully compatible with Android's TalkBack screen reader:

```kotlin
// Example: Proper content descriptions
Button(
    onClick = { rollDice(DiceType.D20) },
    modifier = Modifier.semantics {
        contentDescription = "Roll twenty-sided die"
        role = Role.Button
    }
) {
    Text("Roll d20")
}
```

### Content Descriptions
- **All interactive elements** have meaningful content descriptions
- **Images and icons** include descriptive alt text
- **Dice results** are announced with context
- **Error messages** are clearly communicated

### Semantic Markup
```kotlin
// Example: Semantic roles and properties
LazyColumn(
    modifier = Modifier.semantics {
        role = Role.List
        contentDescription = "Roll history"
    }
) {
    items(rollHistory) { roll ->
        RollHistoryItem(
            roll = roll,
            modifier = Modifier.semantics {
                role = Role.ListItem
                contentDescription = "Rolled ${roll.diceType.displayName}, result: ${roll.result}"
            }
        )
    }
}
```

## 🎨 Visual Accessibility

### Color and Contrast
- **4.5:1 contrast ratio** for normal text
- **3:1 contrast ratio** for large text and UI elements
- **Color is never the only way** to convey information
- **Dark mode support** with appropriate contrast adjustments

```kotlin
// Example: High contrast color definitions
object AccessibleColors {
    val Primary = Color(0xFF1976D2) // Meets contrast requirements
    val OnPrimary = Color(0xFFFFFFFF)
    val Error = Color(0xFFD32F2F) // High contrast red
    val OnError = Color(0xFFFFFFFF)
    
    // Alternative patterns for color-blind users
    val Success = Color(0xFF388E3C) // Green with sufficient contrast
    val Warning = Color(0xFFF57C00) // Orange with pattern support
}
```

### Typography and Readability
- **Scalable text** supporting Android's font size settings
- **Clear font choices** with good readability
- **Sufficient line spacing** for easy reading
- **Consistent text hierarchy**

```kotlin
// Example: Accessible typography
@Composable
fun AccessibleText(
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = style.copy(
            lineHeight = style.fontSize * 1.5, // Improved line spacing
            letterSpacing = 0.01.em // Slight letter spacing for readability
        ),
        modifier = modifier.semantics {
            // Additional semantic information if needed
        }
    )
}
```

### Motion and Animation
- **Respect system animation preferences**
- **Reduced motion support** for users with vestibular disorders
- **Essential animations only** with meaningful purpose
- **Option to disable animations**

```kotlin
// Example: Respecting system animation preferences
@Composable
fun AnimatedDiceRoll(
    isRolling: Boolean,
    result: Int?
) {
    val animationSpec = if (LocalAccessibilityManager.current.isReduceMotionEnabled) {
        tween(durationMillis = 0) // No animation
    } else {
        tween(durationMillis = 500, easing = EaseInOutCubic)
    }
    
    AnimatedVisibility(
        visible = result != null,
        enter = if (LocalAccessibilityManager.current.isReduceMotionEnabled) {
            EnterTransition.None
        } else {
            slideInVertically(animationSpec = animationSpec)
        }
    ) {
        DiceResultDisplay(result = result ?: 0)
    }
}
```

## 📱 Touch and Navigation

### Touch Targets
- **Minimum 48dp touch targets** for all interactive elements
- **Adequate spacing** between touch targets
- **Clear visual feedback** for touch interactions

```kotlin
// Example: Accessible touch targets
@Composable
fun DiceButton(
    diceType: DiceType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .size(56.dp) // Minimum touch target size
            .padding(4.dp) // Spacing between buttons
            .semantics {
                contentDescription = "Roll ${diceType.displayName}"
            }
    ) {
        Icon(
            imageVector = diceType.icon,
            contentDescription = null, // Handled by button's content description
            modifier = Modifier.size(24.dp)
        )
    }
}
```

### Keyboard Navigation
- **Full keyboard support** for all functionality
- **Logical focus order** through the interface
- **Visible focus indicators** for keyboard users
- **Skip navigation** for repetitive content

```kotlin
// Example: Keyboard navigation support
@Composable
fun AccessibleDiceRoller() {
    var focusedDiceType by remember { mutableStateOf(DiceType.D20) }
    
    LazyRow(
        modifier = Modifier.focusable(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(DiceType.values()) { diceType ->
            DiceButton(
                diceType = diceType,
                onClick = { rollDice(diceType) },
                modifier = Modifier
                    .focusable()
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            focusedDiceType = diceType
                        }
                    }
            )
        }
    }
}
```

### Gesture Support
- **Alternative input methods** for gesture-based interactions
- **Voice commands** where appropriate
- **Switch control** compatibility

## 🔊 Audio Feedback

### Sound Design
- **Meaningful audio cues** for important actions
- **Volume controls** within the app
- **Visual alternatives** for all audio information
- **Respect system sound settings**

```kotlin
// Example: Accessible audio feedback
class AccessibleSoundManager @Inject constructor(
    private val context: Context
) {
    fun playDiceRollSound(diceType: DiceType) {
        if (isAudioEnabled() && !isReduceMotionEnabled()) {
            // Play appropriate sound for dice type
            soundPool.play(getDiceSoundId(diceType), volume, volume, 1, 0, 1f)
        }
    }
    
    private fun isAudioEnabled(): Boolean {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.ringerMode != AudioManager.RINGER_MODE_SILENT
    }
}
```

### Haptic Feedback
- **Tactile feedback** for dice rolls
- **Configurable vibration** settings
- **Meaningful haptic patterns** for different dice types

```kotlin
// Example: Accessible haptic feedback
@Composable
fun rememberHapticFeedback(): HapticFeedback {
    val context = LocalContext.current
    val isHapticEnabled = remember {
        Settings.System.getInt(
            context.contentResolver,
            Settings.System.HAPTIC_FEEDBACK_ENABLED,
            1
        ) == 1
    }
    
    return if (isHapticEnabled) {
        LocalHapticFeedback.current
    } else {
        NoOpHapticFeedback
    }
}
```

## 🧭 Error Handling and Feedback

### Accessible Error Messages
- **Clear error descriptions** with suggested solutions
- **Error announcements** for screen readers
- **Visual error indicators** that don't rely on color alone
- **Form validation** with accessible feedback

```kotlin
// Example: Accessible error handling
@Composable
fun AccessibleErrorMessage(
    error: UiText?,
    modifier: Modifier = Modifier
) {
    error?.let {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .semantics {
                    role = Role.AlertDialog
                    liveRegion = LiveRegionMode.Assertive
                },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    text = error.asString(),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
```

## 🌐 Internationalization and Localization

### Text and Content
- **RTL (Right-to-Left) language support**
- **Locale-specific number formatting**
- **Cultural considerations** for dice terminology
- **Accessible font selection** for different languages

```kotlin
// Example: RTL support
@Composable
fun AccessibleRollHistory(
    rolls: List<DiceRoll>,
    modifier: Modifier = Modifier
) {
    val layoutDirection = LocalLayoutDirection.current
    
    LazyColumn(
        modifier = modifier,
        reverseLayout = layoutDirection == LayoutDirection.Rtl,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(rolls) { roll ->
            RollHistoryItem(
                roll = roll,
                modifier = Modifier.semantics {
                    contentDescription = buildAccessibleRollDescription(roll)
                }
            )
        }
    }
}
```

## 🧪 Testing for Accessibility

### Automated Testing
```kotlin
// Example: Accessibility testing
@Test
fun diceRollerScreen_hasProperAccessibilityLabels() {
    composeTestRule.setContent {
        DiceRollerScreen()
    }
    
    // Test that all dice buttons have content descriptions
    DiceType.values().forEach { diceType ->
        composeTestRule
            .onNodeWithContentDescription("Roll ${diceType.displayName}")
            .assertExists()
            .assertIsEnabled()
    }
}

@Test
fun rollResult_isAnnouncedToScreenReader() {
    composeTestRule.setContent {
        DiceResultDisplay(result = 15, diceType = DiceType.D20)
    }
    
    composeTestRule
        .onNodeWithText("15")
        .assertIsDisplayed()
        .assert(hasContentDescription("Rolled d20, result: 15"))
}
```

### Manual Testing Checklist

#### Screen Reader Testing
- [ ] Navigate entire app using TalkBack only
- [ ] Verify all content is announced correctly
- [ ] Check reading order is logical
- [ ] Test with different TalkBack settings

#### Keyboard Navigation Testing
- [ ] Navigate using external keyboard only
- [ ] Verify all interactive elements are reachable
- [ ] Check focus indicators are visible
- [ ] Test tab order is logical

#### Visual Testing
- [ ] Test with maximum font size settings
- [ ] Verify app works in high contrast mode
- [ ] Check color blind accessibility (protanopia, deuteranopia, tritanopia)
- [ ] Test in bright sunlight conditions

#### Motor Accessibility Testing
- [ ] Test with switch control
- [ ] Verify touch targets meet minimum size requirements
- [ ] Check app works with voice control
- [ ] Test with reduced dexterity simulation

## 📋 Accessibility Settings

### User Preferences
```kotlin
// Example: Accessibility preferences
data class AccessibilityPreferences(
    val isHapticFeedbackEnabled: Boolean = true,
    val isSoundEnabled: Boolean = true,
    val isReducedMotionEnabled: Boolean = false,
    val textScale: Float = 1.0f,
    val highContrastMode: Boolean = false
)

@Composable
fun AccessibilitySettingsScreen(
    preferences: AccessibilityPreferences,
    onPreferencesChange: (AccessibilityPreferences) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            AccessibilityToggle(
                title = "Haptic Feedback",
                description = "Feel vibrations when rolling dice",
                checked = preferences.isHapticFeedbackEnabled,
                onCheckedChange = { 
                    onPreferencesChange(preferences.copy(isHapticFeedbackEnabled = it))
                }
            )
        }
        
        item {
            AccessibilityToggle(
                title = "Sound Effects",
                description = "Play sounds when rolling dice",
                checked = preferences.isSoundEnabled,
                onCheckedChange = { 
                    onPreferencesChange(preferences.copy(isSoundEnabled = it))
                }
            )
        }
        
        item {
            AccessibilityToggle(
                title = "Reduce Motion",
                description = "Minimize animations and motion effects",
                checked = preferences.isReducedMotionEnabled,
                onCheckedChange = { 
                    onPreferencesChange(preferences.copy(isReducedMotionEnabled = it))
                }
            )
        }
    }
}
```

## 🎓 Accessibility Resources

### Training Materials
- [Android Accessibility Developer Guide](https://developer.android.com/guide/topics/ui/accessibility)
- [Material Design Accessibility](https://material.io/design/usability/accessibility.html)
- [WCAG 2.1 Guidelines](https://www.w3.org/WAI/WCAG21/quickref/)

### Testing Tools
- **Accessibility Scanner** - Automated accessibility testing
- **TalkBack** - Screen reader testing
- **Switch Access** - Switch control testing
- **Accessibility Test Framework** - Automated testing library

### Community Resources
- **Android Accessibility Community** - Developer forums and discussions
- **Disability Advocacy Groups** - User feedback and testing partners
- **Accessibility Consulting Services** - Professional accessibility reviews

## 🔄 Continuous Improvement

### User Feedback
- **Accessibility feedback channels** in app settings
- **User testing sessions** with disability community
- **Regular accessibility audits** by experts
- **Bug reporting** for accessibility issues

### Updates and Maintenance
- **Regular accessibility testing** in CI/CD pipeline
- **Accessibility regression testing** for new features
- **Stay updated** with latest accessibility guidelines
- **Monitor platform accessibility changes**

---

By following these comprehensive accessibility guidelines, the D&D Dice Roller app ensures that everyone can enjoy rolling dice, regardless of their abilities or how they interact with their devices. Accessibility is not just about compliance—it's about creating an inclusive experience that welcomes all users to the D&D community.
