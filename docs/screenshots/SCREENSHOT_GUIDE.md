# Screenshot Generation Guide - D&D Dice Roller

This guide provides detailed instructions for generating high-quality screenshots for the Google Play Store listing and promotional materials.

## 📱 Screenshot Requirements

### Technical Specifications

#### Phone Screenshots
- **Resolution**: 1080 x 1920 pixels (16:9 aspect ratio)
- **Format**: PNG or JPEG (PNG preferred for UI)
- **Color Space**: sRGB
- **File Size**: Under 8MB per image
- **Quantity**: 8 screenshots required

#### Tablet Screenshots
- **Resolution**: 1920 x 1200 pixels (16:10 aspect ratio)
- **Format**: PNG or JPEG
- **Color Space**: sRGB
- **File Size**: Under 8MB per image
- **Quantity**: 1 screenshot minimum

### Device Setup

#### Recommended Test Devices
- **Phone**: Pixel 7 Pro (1440 x 3120, scaled to 1080 x 1920)
- **Phone Alternative**: Samsung Galaxy S23 (1080 x 2340, cropped)
- **Tablet**: Pixel Tablet (2560 x 1600, scaled to 1920 x 1200)
- **Emulator**: Use API 34 with Google Play Store

#### Device Configuration
```bash
# Configure device for screenshots
adb shell settings put system screen_brightness 255
adb shell settings put secure enabled_accessibility_services ""
adb shell settings put global window_animation_scale 0
adb shell settings put global transition_animation_scale 0
adb shell settings put global animator_duration_scale 0

# Set system UI for clean screenshots
adb shell settings put secure ui_night_mode 1  # For dark mode shots
adb shell settings put secure ui_night_mode 2  # For light mode shots
```

## 🎨 Screenshot Composition

### Design Guidelines

#### Visual Hierarchy
1. **Primary Focus**: Main feature being demonstrated
2. **Secondary Elements**: Supporting UI that provides context
3. **Clean Background**: Minimal distractions
4. **Brand Consistency**: Consistent colors and styling

#### Typography and Text
- **Title Overlay**: Clear, readable fonts
- **Subtitle**: Descriptive text explaining the feature
- **UI Text**: Actual app interface text
- **Language**: English (US) for primary screenshots

### Color Scheme
- **Primary**: Material Design 3 dynamic colors
- **Background**: Pure white or pure black for contrast
- **Accent**: ChaseWhiteRabbit NGO brand colors
- **Text**: High contrast (4.5:1 minimum ratio)

## 📸 Screenshot Generation Process

### Automated Screenshot Generation

#### Espresso Screenshot Tests
```kotlin
// Screenshot test example
@RunWith(AndroidJUnit4::class)
class ScreenshotTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @get:Rule
    val screenshotRule = ScreenshotTestRule()
    
    @Test
    fun captureMainDiceInterface() {
        composeTestRule.setContent {
            DiceRollerTheme {
                MainDiceScreen(
                    uiState = createSampleUiState(),
                    onRollDice = { }
                )
            }
        }
        
        // Wait for UI to stabilize
        composeTestRule.waitForIdle()
        
        // Capture screenshot
        screenshotRule.takeScreenshot("01_main_dice_interface")
    }
    
    @Test
    fun captureRollResult() {
        val resultState = DiceRollerUiState(
            currentRoll = DiceRoll(
                diceType = DiceType.D20,
                result = 18,
                timestamp = Clock.System.now()
            )
        )
        
        composeTestRule.setContent {
            DiceRollerTheme {
                DiceRollResultDisplay(
                    roll = resultState.currentRoll!!,
                    isAnimating = true
                )
            }
        }
        
        screenshotRule.takeScreenshot("02_roll_result_display")
    }
}
```

#### Screenshot Automation Script
```bash
#!/bin/bash
# generate_screenshots.sh

echo "Generating Play Store screenshots..."

# 1. Setup device
./setup_device_for_screenshots.sh

# 2. Generate sample data
adb shell am broadcast -a com.tiation.dnddiceroller.GENERATE_SAMPLE_DATA

# 3. Run screenshot tests
./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.tiation.dnddiceroller.ScreenshotTest

# 4. Process and optimize images
./process_screenshots.sh

echo "Screenshots generated in screenshots/raw/"
```

### Manual Screenshot Capture

#### ADB Screen Capture
```bash
# Capture screenshot via ADB
adb shell screencap -p /sdcard/screenshot.png
adb pull /sdcard/screenshot.png screenshots/raw/
adb shell rm /sdcard/screenshot.png

# Batch capture with naming
for i in {1..8}; do
    echo "Capture screenshot $i (press enter when ready)"
    read
    adb shell screencap -p /sdcard/screenshot_$i.png
    adb pull /sdcard/screenshot_$i.png screenshots/raw/
    adb shell rm /sdcard/screenshot_$i.png
done
```

#### Using Android Studio
1. Open Device Manager
2. Launch emulator with desired specifications
3. Use "Camera" button in emulator controls
4. Save to `screenshots/raw/` directory

## 🖼️ Screenshot Content Guide

### Screenshot 1: Main Dice Interface
**Title**: "Roll with Style"
**Content**:
- All D&D dice types (d4, d6, d8, d10, d12, d20, d100) displayed
- Clean Material Design 3 interface
- Good lighting and contrast
- No roll results shown (clean state)

**Setup**:
```kotlin
val cleanState = DiceRollerUiState(
    selectedDiceType = DiceType.D20,
    currentRoll = null,
    isRolling = false
)
```

### Screenshot 2: Roll Result Display
**Title**: "Instant Results"
**Content**:
- Large, prominent dice result (18-20 for d20)
- Subtle animation effects visible
- Haptic feedback indicators
- Clear dice type identification

**Setup**:
```kotlin
val resultState = DiceRollerUiState(
    selectedDiceType = DiceType.D20,
    currentRoll = DiceRoll(DiceType.D20, 19),
    isRolling = false
)
```

### Screenshot 3: Roll History
**Title**: "Track Every Roll"
**Content**:
- 5-7 recent rolls displayed
- Mix of different dice types
- Timestamps visible
- Campaign tags shown
- Clean, organized list

**Sample Data**:
```kotlin
val historyRolls = listOf(
    DiceRoll(DiceType.D20, 15, campaignId = "Dragons of Autumn"),
    DiceRoll(DiceType.D8, 6, campaignId = "Dragons of Autumn"),
    DiceRoll(DiceType.D6, 4, campaignId = "Storm King's Thunder"),
    DiceRoll(DiceType.D12, 10, campaignId = "Dragons of Autumn"),
    DiceRoll(DiceType.D4, 3, campaignId = "Storm King's Thunder")
)
```

### Screenshot 4: Campaign Management
**Title**: "Organize Your Adventures"
**Content**:
- 3-4 campaign cards
- Descriptive campaign names
- Roll counts per campaign
- Easy switching interface
- Add new campaign button

**Sample Campaigns**:
```kotlin
val campaigns = listOf(
    Campaign("Dragons of Autumn Twilight", "Classic Dragonlance adventure"),
    Campaign("Storm King's Thunder", "Giants threaten the Sword Coast"),
    Campaign("Curse of Strahd", "Gothic horror in Barovia"),
    Campaign("One-Shot Adventures", "Quick games and experiments")
)
```

### Screenshot 5: Custom Dice Sets
**Title**: "Save Your Favorite Combinations"
**Content**:
- 4-5 saved dice sets
- Descriptive names (Attack Roll, Fireball Damage, etc.)
- Dice composition shown
- One-tap rolling interface

**Sample Dice Sets**:
```kotlin
val diceSets = listOf(
    DiceSet("Longsword Attack", listOf(
        DiceConfig(DiceType.D20, 1), // Attack roll
        DiceConfig(DiceType.D8, 1)   // Damage
    ), modifier = 3),
    DiceSet("Fireball Damage", listOf(
        DiceConfig(DiceType.D6, 8)   // 8d6 damage
    )),
    DiceSet("Healing Potion", listOf(
        DiceConfig(DiceType.D4, 2)   // 2d4+2
    ), modifier = 2)
)
```

### Screenshot 6: Dark Mode
**Title**: "Perfect for Late Night Gaming"
**Content**:
- Same interface as Screenshot 1 but in dark theme
- Proper contrast and readability
- Material Design 3 dark colors
- Accessibility compliance visible

### Screenshot 7: Accessibility Features
**Title**: "Built for Everyone"
**Content**:
- TalkBack overlay visible (if possible)
- Large touch targets highlighted
- High contrast elements
- Clear focus indicators
- Accessibility service indicators

**Setup**:
```bash
# Enable accessibility features for screenshot
adb shell settings put secure enabled_accessibility_services com.google.android.marvin.talkback/.TalkBackService
adb shell settings put secure accessibility_speak_password 1
```

### Screenshot 8: Statistics Dashboard
**Title**: "Analyze Your Luck"
**Content**:
- Roll distribution charts
- Campaign statistics
- Character performance
- Visual data representation

**Sample Stats**:
```kotlin
val rollStats = RollStatistics(
    totalRolls = 247,
    averageRoll = mapOf(
        DiceType.D20 to 11.2,
        DiceType.D8 to 4.7,
        DiceType.D6 to 3.8
    ),
    distribution = generateSampleDistribution(),
    campaignBreakdown = mapOf(
        "Dragons of Autumn" to 156,
        "Storm King's Thunder" to 91
    )
)
```

## 🎭 Frame Composition and Styling

### Visual Design Elements

#### Title Overlays
```css
/* Title styling specifications */
.screenshot-title {
    font-family: 'Google Sans', sans-serif;
    font-size: 32px;
    font-weight: 600;
    color: #1976D2;
    text-align: center;
    margin-bottom: 16px;
    text-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.screenshot-subtitle {
    font-family: 'Google Sans', sans-serif;
    font-size: 18px;
    font-weight: 400;
    color: #424242;
    text-align: center;
    line-height: 1.4;
}
```

#### Background Elements
- **Gradient Overlay**: Subtle gradient from app's primary color
- **Pattern**: Light geometric pattern (dice-themed)
- **Frame**: Minimal border or shadow for depth
- **Brand Elements**: ChaseWhiteRabbit NGO subtle branding

### Composition Guidelines

#### Rule of Thirds
- Place key UI elements along rule-of-thirds lines
- Center important buttons and text
- Balance visual weight across the frame

#### Visual Flow
1. **Top**: Title and branding
2. **Center**: Main app interface
3. **Bottom**: Subtle call-to-action or feature highlight

## 🔧 Post-Processing

### Image Optimization

#### Automated Processing Script
```bash
#!/bin/bash
# process_screenshots.sh

INPUT_DIR="screenshots/raw"
OUTPUT_DIR="screenshots/processed"

mkdir -p $OUTPUT_DIR

for file in $INPUT_DIR/*.png; do
    filename=$(basename "$file" .png)
    
    # Resize to exact specifications
    convert "$file" -resize 1080x1920! "$OUTPUT_DIR/${filename}_resized.png"
    
    # Optimize file size
    pngquant --quality=80-95 "$OUTPUT_DIR/${filename}_resized.png" --output "$OUTPUT_DIR/${filename}_optimized.png"
    
    # Add title overlay (if template exists)
    if [ -f "templates/${filename}_overlay.png" ]; then
        composite "templates/${filename}_overlay.png" "$OUTPUT_DIR/${filename}_optimized.png" "$OUTPUT_DIR/${filename}_final.png"
    else
        cp "$OUTPUT_DIR/${filename}_optimized.png" "$OUTPUT_DIR/${filename}_final.png"
    fi
    
    echo "Processed: $filename"
done

echo "All screenshots processed and saved to $OUTPUT_DIR"
```

#### Manual Optimization Checklist
- [ ] **Resolution**: Exactly 1080x1920 pixels
- [ ] **File Size**: Under 8MB each
- [ ] **Color Profile**: sRGB embedded
- [ ] **Compression**: Lossless PNG or high-quality JPEG
- [ ] **Metadata**: Remove EXIF data for privacy
- [ ] **Naming**: Consistent naming convention

### Quality Assurance

#### Screenshot Review Checklist
- [ ] **Visual Quality**: Crisp, clear, no artifacts
- [ ] **Content Accuracy**: App features represented correctly
- [ ] **Brand Consistency**: Colors and styling match brand
- [ ] **Text Readability**: All text clearly legible
- [ ] **UI Completeness**: No cut-off elements or loading states
- [ ] **Accessibility**: High contrast, clear focus indicators
- [ ] **Device Consistency**: Same device/emulator for all shots

## 📋 Screenshot Delivery

### File Organization
```
screenshots/
├── raw/                    # Original captures
│   ├── 01_main_interface.png
│   ├── 02_roll_result.png
│   └── ...
├── processed/              # Optimized versions
│   ├── 01_main_interface_final.png
│   ├── 02_roll_result_final.png
│   └── ...
├── templates/              # Overlay templates
│   ├── title_overlay.psd
│   └── brand_elements.png
└── tablet/                 # Tablet-specific screenshots
    └── tablet_landscape.png
```

### Delivery Format
- **Play Store Upload**: PNG files, exact specifications
- **Marketing Materials**: Both PNG and JPEG versions
- **Press Kit**: High-resolution versions available
- **Social Media**: Square and story format derivatives

### Version Control
- **Git LFS**: Store final screenshots in version control
- **Naming Convention**: `YY_feature_description_final.png`
- **Change Log**: Document changes and versions
- **Archive**: Keep previous versions for rollback

## 🎯 Screenshot Testing

### A/B Testing Preparation
Create variations for testing:
- Different title text
- Various UI states
- Feature emphasis changes
- Color scheme variations

### Performance Metrics
Track how screenshots affect:
- Install conversion rate
- User engagement
- Store listing CTR
- Feature adoption

---

## 📞 Contact and Support

### Screenshot Team
- **Visual Designer**: TBD
- **Technical Lead**: Garrett Dillman (garrett@sxc.codes)
- **Marketing**: ChaseWhiteRabbit NGO team
- **QA Review**: Tia (tiatheone@protonmail.com)

### Tools and Resources
- **Design Software**: Adobe Creative Suite, Figma
- **Screenshot Tools**: Android Studio, ADB, Espresso
- **Optimization**: ImageOptim, TinyPNG, pngquant
- **Review**: Google Play Console, App Store Preview

---

*This screenshot guide ensures consistent, high-quality visual representation of the D&D Dice Roller app across all marketing channels and store listings.*

**Built with ❤️ and attention to visual detail by Garrett Dillman and Tia**  
*Part of the ChaseWhiteRabbit NGO initiative for accessible gaming*
