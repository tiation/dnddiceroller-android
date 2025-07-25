# Development Guide - D&D Dice Roller Android

This comprehensive guide provides everything developers need to know to contribute to, maintain, and extend the D&D Dice Roller Android application.

## 🚀 Quick Start

### Prerequisites
- **Android Studio**: Iguana | 2023.2.1 or later
- **JDK**: 17 or later (Amazon Corretto recommended)
- **Android SDK**: API 24+ (minimum), API 34+ (target)
- **Git**: Version control
- **Gradle**: Included via wrapper

### Environment Setup

#### 1. Clone the Repository
```bash
git clone git@github.com:tiation/dnddiceroller-android.git
cd dnddiceroller-android
```

#### 2. Android Studio Configuration
```bash
# Install required SDK components
sdkmanager "platforms;android-34"
sdkmanager "build-tools;34.0.0"
sdkmanager "extras;android;m2repository"
sdkmanager "extras;google;m2repository"
```

#### 3. Gradle Configuration
```bash
# Set up gradle.properties (create if doesn't exist)
echo "android.useAndroidX=true" >> gradle.properties
echo "android.enableJetifier=true" >> gradle.properties
echo "kotlin.code.style=official" >> gradle.properties
echo "org.gradle.parallel=true" >> gradle.properties
echo "org.gradle.caching=true" >> gradle.properties
```

#### 4. Build and Verify
```bash
./gradlew clean build
./gradlew test
./gradlew connectedAndroidTest
```

## 🏗️ Project Architecture

### Module Structure

```
dnddiceroller-android/
├── app/                    # Application module (Presentation layer)
│   ├── src/main/
│   │   ├── java/com/tiation/dnddiceroller/
│   │   │   ├── features/          # Feature-specific UI components
│   │   │   ├── navigation/        # Navigation configuration
│   │   │   ├── di/               # Dependency injection modules
│   │   │   └── performance/      # Performance monitoring
│   │   └── res/                  # Android resources
│   ├── src/test/                 # Unit tests
│   └── src/androidTest/          # Instrumentation tests
├── domain/                 # Business logic module (Pure Kotlin)
│   ├── src/main/java/com/chasewhiterabbit/dicengine/domain/
│   │   ├── engine/              # Core dice engine
│   │   ├── model/               # Domain models
│   │   ├── usecase/             # Business use cases
│   │   └── repository/          # Repository interfaces
│   └── src/test/                # Domain unit tests
├── data/                   # Data layer module
│   ├── src/main/java/com/chasewhiterabbit/dicengine/data/
│   │   ├── local/               # Room database
│   │   ├── repository/          # Repository implementations
│   │   └── di/                  # Data dependency injection
│   └── src/test/                # Data layer tests
├── core-ui/               # Design system and shared UI components
│   ├── src/main/java/com/tiation/core/ui/
│   │   ├── components/          # Reusable Compose components
│   │   ├── theme/               # Material Design 3 theme
│   │   └── resources/           # Shared UI resources
│   └── src/test/                # UI component tests
├── common-test/           # Shared testing utilities
│   └── src/main/java/com/tiation/common/test/
│       ├── fixtures/            # Test data fixtures
│       ├── rules/               # Custom test rules
│       └── utils/               # Testing utilities
├── docs/                  # Project documentation
├── config/                # Configuration files
│   ├── detekt/                  # Static analysis config
│   └── spotless/                # Code formatting config
├── monitoring/            # Monitoring and observability
└── scripts/               # Build and deployment scripts
```

### Layer Responsibilities

#### Presentation Layer (app/)
- **Activities & Fragments**: Android UI entry points
- **Compose Screens**: Modern declarative UI
- **ViewModels**: UI state management with MVVM pattern
- **Navigation**: Screen navigation and deep linking
- **Dependency Injection**: Hilt modules for presentation dependencies

#### Domain Layer (domain/)
- **Use Cases**: Single-purpose business operations
- **Models**: Core business entities and value objects
- **Repository Interfaces**: Abstract data access contracts
- **Business Logic**: Pure Kotlin business rules

#### Data Layer (data/)
- **Repository Implementations**: Concrete data access implementations
- **Room Database**: Local persistence
- **Network Services**: Remote API communication (future)
- **DataStore**: Modern preferences storage
- **Mappers**: Data transformation between layers

## 🔧 Development Workflow

### Branch Strategy (GitFlow)

```
main/
├── develop/
│   ├── feature/dice-animation-improvements
│   ├── feature/voice-commands
│   └── feature/campaign-sharing
├── release/v1.2.0
└── hotfix/critical-crash-fix
```

#### Branch Types
- **main**: Production-ready code
- **develop**: Integration branch for features
- **feature/***: New feature development
- **release/***: Release preparation
- **hotfix/***: Critical production fixes

### Development Process

#### 1. Feature Development
```bash
# Start from develop
git checkout develop
git pull origin develop

# Create feature branch
git checkout -b feature/new-dice-type

# Develop with frequent commits
git add .
git commit -m "feat: add d30 dice type support"

# Keep up to date with develop
git rebase develop

# Push and create PR
git push origin feature/new-dice-type
```

#### 2. Code Quality Checks
```bash
# Run before committing
./gradlew ktlintCheck
./gradlew detekt
./gradlew lint
./gradlew test
```

#### 3. Testing Strategy
```bash
# Unit tests
./gradlew testDebugUnitTest

# Integration tests
./gradlew testDebugUnitTest -Pandroid.testInstrumentationRunnerArguments.class=com.tiation.dnddiceroller.integration

# UI tests
./gradlew connectedDebugAndroidTest

# Test coverage
./gradlew jacocoTestReport
```

## 🎨 UI Development

### Compose Architecture

#### State Management
```kotlin
// ViewModel pattern with StateFlow
class DiceRollerViewModel @Inject constructor(
    private val rollDiceUseCase: RollDiceUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DiceRollerUiState())
    val uiState: StateFlow<DiceRollerUiState> = _uiState.asStateFlow()
    
    fun rollDice(diceType: DiceType) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRolling = true)
            
            rollDiceUseCase(diceType)
                .onSuccess { result ->
                    _uiState.value = _uiState.value.copy(
                        currentRoll = result,
                        isRolling = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.toUiText(),
                        isRolling = false
                    )
                }
        }
    }
}
```

#### Compose Components
```kotlin
// Reusable component pattern
@Composable
fun DiceButton(
    diceType: DiceType,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .size(72.dp)
            .semantics {
                contentDescription = "Roll ${diceType.displayName}"
                role = Role.Button
            },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Icon(
            imageVector = diceType.icon,
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
    }
}
```

### Material Design 3 Implementation

#### Theme System
```kotlin
// Theme configuration
@Composable
fun DiceRollerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

## 🗄️ Data Layer Development

### Room Database

#### Entity Design
```kotlin
@Entity(
    tableName = "dice_rolls",
    indices = [
        Index(value = ["campaign_id"]),
        Index(value = ["timestamp"]),
        Index(value = ["dice_type"])
    ]
)
data class DiceRollEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "dice_type") val diceType: String,
    val result: Int,
    val timestamp: Long,
    @ColumnInfo(name = "campaign_id") val campaignId: String? = null,
    @ColumnInfo(name = "character_name") val characterName: String? = null,
    @ColumnInfo(name = "roll_reason") val rollReason: String? = null
)
```

#### DAO Implementation
```kotlin
@Dao
interface DiceRollDao {
    @Query("""
        SELECT * FROM dice_rolls 
        WHERE (:campaignId IS NULL OR campaign_id = :campaignId)
        ORDER BY timestamp DESC 
        LIMIT :limit OFFSET :offset
    """)
    fun getRollHistory(
        campaignId: String?,
        limit: Int,
        offset: Int
    ): Flow<List<DiceRollEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiceRoll(diceRoll: DiceRollEntity)
    
    @Query("DELETE FROM dice_rolls WHERE id = :id")
    suspend fun deleteDiceRoll(id: String)
}
```

#### Repository Pattern
```kotlin
class DiceRollRepositoryImpl @Inject constructor(
    private val diceRollDao: DiceRollDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : DiceRollRepository {
    
    override suspend fun saveDiceRoll(diceRoll: DiceRoll): Result<Unit> = withContext(dispatcher) {
        try {
            val entity = diceRoll.toEntity()
            diceRollDao.insertDiceRoll(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getRollHistory(
        campaignId: String?,
        limit: Int,
        offset: Int
    ): Flow<List<DiceRoll>> = diceRollDao
        .getRollHistory(campaignId, limit, offset)
        .map { entities -> entities.map { it.toDomainModel() } }
        .flowOn(dispatcher)
}
```

## 🧪 Testing Guidelines

### Unit Testing

#### Domain Layer Tests
```kotlin
@Test
fun `when rolling d20 then result should be between 1 and 20`() = runTest {
    // Given
    val fakeRandom = FakeRandomGenerator(15)
    val useCase = RollDiceUseCase(fakeRepository, fakeRandom)
    
    // When
    val result = useCase(DiceType.D20)
    
    // Then
    assertTrue(result.isSuccess)
    assertEquals(15, result.getOrNull()?.result)
    assertEquals(DiceType.D20, result.getOrNull()?.diceType)
}
```

#### ViewModel Tests
```kotlin
@Test
fun `when rollDice called then uiState should show loading then result`() = runTest {
    // Given
    val testScheduler = TestCoroutineScheduler()
    val testDispatcher = StandardTestDispatcher(testScheduler)
    val viewModel = DiceRollerViewModel(rollDiceUseCase)
    
    // When
    viewModel.rollDice(DiceType.D20)
    
    // Then
    val states = viewModel.uiState.test {
        assertEquals(true, awaitItem().isRolling)
        testScheduler.advanceUntilIdle()
        val finalState = awaitItem()
        assertEquals(false, finalState.isRolling)
        assertNotNull(finalState.currentRoll)
    }
}
```

### UI Testing

#### Compose Tests
```kotlin
@Test
fun diceButton_whenClicked_triggersCallback() {
    var clickCount = 0
    
    composeTestRule.setContent {
        DiceButton(
            diceType = DiceType.D20,
            onClick = { clickCount++ }
        )
    }
    
    composeTestRule
        .onNodeWithContentDescription("Roll d20")
        .performClick()
    
    assertEquals(1, clickCount)
}
```

#### Integration Tests
```kotlin
@Test
fun rollDiceFlow_endToEnd_showsResult() {
    composeTestRule.setContent {
        DiceRollerApp()
    }
    
    // Tap d20 button
    composeTestRule
        .onNodeWithContentDescription("Roll d20")
        .performClick()
    
    // Wait for result
    composeTestRule
        .onNodeWithText("Result:")
        .assertIsDisplayed()
    
    // Check result is valid
    composeTestRule
        .onAllNodesWithText(Regex("""^(1|2|3|...|20)$"""))
        .onFirst()
        .assertIsDisplayed()
}
```

## 🔍 Performance Optimization

### Build Performance

#### Gradle Optimization
```groovy
// gradle.properties
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=true
org.gradle.jvmargs=-Xmx4096m -XX:+UseParallelGC
android.enableBuildCache=true
android.useAndroidX=true
kotlin.incremental=true
```

#### Compose Compiler Metrics
```bash
# Enable Compose metrics
./gradlew assembleRelease -Pandroid.experimental.enableComposeCompilerMetrics=true -Pandroid.experimental.enableComposeCompilerReports=true

# Analyze metrics
find . -name "compose_metrics.txt" -exec cat {} \;
```

### Runtime Performance

#### Memory Management
```kotlin
// Efficient state management
@Composable
fun DiceRollerScreen(
    viewModel: DiceRollerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // Use derivedStateOf for expensive calculations
    val rollStatistics by remember(uiState.rollHistory) {
        derivedStateOf {
            calculateRollStatistics(uiState.rollHistory)
        }
    }
    
    DiceRollerContent(
        uiState = uiState,
        statistics = rollStatistics,
        onRollDice = viewModel::rollDice
    )
}
```

#### Database Optimization
```kotlin
// Efficient queries with proper indexing
@Query("""
    SELECT * FROM dice_rolls 
    WHERE campaign_id = :campaignId 
    AND timestamp BETWEEN :startTime AND :endTime
    ORDER BY timestamp DESC 
    LIMIT 50
""")
suspend fun getRecentRolls(
    campaignId: String,
    startTime: Long,
    endTime: Long
): List<DiceRollEntity>
```

## 📊 Monitoring and Analytics

### Performance Monitoring

#### Custom Performance Tracker
```kotlin
@Singleton
class PerformanceMonitor @Inject constructor() {
    
    fun trackDiceRoll(diceType: DiceType, duration: Long) {
        // Track roll performance
        Timber.d("Dice roll ${diceType.name} took ${duration}ms")
        
        // Report to analytics if enabled
        if (duration > SLOW_ROLL_THRESHOLD) {
            reportSlowRoll(diceType, duration)
        }
    }
    
    fun trackScreenLoad(screenName: String, loadTime: Long) {
        Timber.d("Screen $screenName loaded in ${loadTime}ms")
    }
}
```

#### Memory Leak Detection
```kotlin
// In debug builds only
class DebugApplication : DiceRollerApplication() {
    override fun onCreate() {
        super.onCreate()
        
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        
        LeakCanary.install(this)
    }
}
```

## 🚀 Release Process

### Version Management

#### Semantic Versioning
```
MAJOR.MINOR.PATCH[-PRERELEASE][+BUILD]

Examples:
1.0.0       - Initial release
1.1.0       - New features
1.1.1       - Bug fixes
1.2.0-beta  - Beta release
```

#### Build Configuration
```kotlin
// app/build.gradle.kts
android {
    defaultConfig {
        versionCode = 1001  // Increment for each release
        versionName = "1.0.1"
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

### CI/CD Pipeline

#### GitHub Actions Workflow
The project uses GitHub Actions for continuous integration:

1. **Code Quality**: Linting, static analysis
2. **Unit Tests**: Domain and data layer tests
3. **Build**: APK/AAB generation
4. **UI Tests**: Instrumented tests on emulators
5. **Security**: Dependency vulnerability scanning
6. **Release**: Automated release builds

#### Manual Release Steps
```bash
# 1. Create release branch
git checkout develop
git pull origin develop
git checkout -b release/v1.1.0

# 2. Update version numbers
# - app/build.gradle.kts
# - CHANGELOG.md

# 3. Build and test
./gradlew clean build
./gradlew test
./gradlew connectedAndroidTest

# 4. Create release PR
git add .
git commit -m "chore: prepare release v1.1.0"
git push origin release/v1.1.0

# 5. After PR approval and merge
git checkout main
git pull origin main
git tag v1.1.0
git push origin v1.1.0
```

## 🔧 Debugging and Troubleshooting

### Common Issues

#### Build Issues
```bash
# Clean build
./gradlew clean

# Clear gradle cache
rm -rf ~/.gradle/caches

# Invalidate Android Studio caches
# File > Invalidate Caches and Restart
```

#### Testing Issues
```bash
# Run specific test
./gradlew :app:testDebugUnitTest --tests="*DiceRollerViewModelTest*"

# Debug test with more logging
./gradlew test --debug --stacktrace
```

#### Performance Issues
```bash
# Profile build performance
./gradlew assembleDebug --profile

# Analyze APK
./gradlew analyzeDebugBundle
```

### Development Tools

#### Useful Android Studio Plugins
- **Kotlin Multiplatform Mobile**
- **Detekt**
- **Rainbow Brackets**
- **GitToolBox**
- **Compose Preview Helper**

#### Command Line Tools
```bash
# ADB useful commands
adb logcat | grep "DiceRoller"
adb shell dumpsys activity com.tiation.dnddiceroller
adb shell am start -n com.tiation.dnddiceroller/.MainActivity

# Profiling
adb shell simpleperf record -p $(adb shell pidof com.tiation.dnddiceroller)
```

## 📚 Additional Resources

### Documentation Links
- [Android Developer Guide](https://developer.android.com/guide)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)
- [Material Design 3](https://m3.material.io/)

### Team Communication
- **GitHub Issues**: Bug reports and feature requests
- **Pull Requests**: Code review and discussion
- **Email**: tiatheone@protonmail.com (Tia), garrett@sxc.codes (Garrett)

### Contributing Guidelines
See [CONTRIBUTING.md](../CONTRIBUTING.md) for detailed contribution instructions.

---

**Happy coding! 🎲** Remember to write tests, follow the architecture patterns, and keep accessibility in mind for all new features.

*Built with ❤️ by the ChaseWhiteRabbit NGO development team*
