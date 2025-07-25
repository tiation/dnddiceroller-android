# DnD Dice Roller - Testing Implementation

## Overview

This document provides a comprehensive overview of the testing implementation for the DnD Dice Roller Android application. The testing strategy follows enterprise-grade practices with comprehensive coverage across unit, integration, and UI testing layers.

## Test Structure

### 1. Unit Tests (`app/src/test/`)

#### ViewModel Tests
- **`RollHistoryViewModelTest.kt`** - Comprehensive ViewModel testing
  - Statistics loading and calculations
  - Filter updates and state management
  - Export functionality
  - History management operations
  - Error handling scenarios
  - Edge cases and concurrent operations
  - Uses Mockito, Turbine, and Coroutines Test for async testing

#### Repository Tests  
- **`DiceRollRepositoryTest.kt`** - Data layer testing
  - Roll logging functionality
  - Data retrieval methods
  - Statistics calculations
  - Data management operations
  - Flow-based data access
  - Error handling

#### Engine Tests
- **`DiceEngineTest.kt`** - Core business logic testing
  - Random number generation validation
  - Advantage/disadvantage mechanics
  - Multiple dice rolling
  - Input validation
  - Statistical distribution validation

### 2. Integration Tests (`app/src/androidTest/`)

#### Database Integration
- **`DiceRollDaoTest.kt`** - Room database testing
  - CRUD operations
  - Query functionality
  - Statistics aggregation
  - Data filtering
  - Pagination testing
  - Transaction handling

### 3. UI Tests (`app/src/androidTest/`)

#### Compose UI Tests
- **`RollHistoryScreenTest.kt`** - Jetpack Compose UI testing
  - Screen rendering with different states
  - User interactions and gestures
  - Filter functionality
  - Export workflows
  - Navigation testing
  - Accessibility validation
  - Scrolling behavior

## Testing Technologies

### Core Testing Framework
- **JUnit 4/5** - Primary testing framework
- **Mockito** - Mocking framework for unit tests
- **Mockito-Kotlin** - Kotlin-specific Mockito extensions

### Android Testing
- **AndroidX Test** - Android testing libraries
- **Espresso** - UI testing framework
- **Compose Test** - Jetpack Compose testing tools
- **Room Testing** - Database testing utilities
- **Hilt Testing** - Dependency injection testing

### Asynchronous Testing
- **Coroutines Test** - Kotlin coroutines testing
- **Turbine** - Flow testing library
- **InstantTaskExecutorRule** - LiveData testing

### Property-Based Testing
- **Kotest** - Property-based testing framework
- **Statistical validation** - Random distribution testing

### Coverage Tools
- **JaCoCo** - Code coverage measurement
- **Custom coverage script** - Enterprise reporting

## Test Configuration

### Dependencies
```kotlin
// Unit Testing
testImplementation("junit:junit:4.13.2")
testImplementation("org.mockito:mockito-core:5.7.0")
testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
testImplementation("androidx.arch.core:core-testing:2.2.0")
testImplementation("app.cash.turbine:turbine:1.0.0")

// Integration Testing
androidTestImplementation("androidx.test.ext:junit:1.1.5")
androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
androidTestImplementation("androidx.room:room-testing:2.6.1")

// UI Testing
androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.11")
androidTestImplementation("androidx.test.espresso:espresso-accessibility:3.5.1")

// Hilt Testing
testImplementation("com.google.dagger:hilt-android-testing:2.48.1")
androidTestImplementation("com.google.dagger:hilt-android-testing:2.48.1")
```

### Test Runner Configuration
```kotlin
android {
    defaultConfig {
        testInstrumentationRunner = "com.tiation.dnddiceroller.HiltTestRunner"
    }
    
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
        animationsDisabled = true
    }
}
```

## Coverage Goals

### Minimum Requirements
- **Overall Coverage**: 80% line coverage minimum
- **Critical Path Testing**: 100% coverage
- **Edge Case Validation**: 95% coverage
- **Error Handling**: 90% coverage

### Coverage Exclusions
- Generated code (Hilt, Room, etc.)
- UI Activities/Fragments
- Data classes and models
- Dependency injection modules

## Test Execution

### Running Tests
```bash
# Run all unit tests
./gradlew test

# Run all integration tests  
./gradlew connectedAndroidTest

# Run all tests
./gradlew testAll

# Generate coverage report
./gradlew coverageReport
```

### Coverage Reports
- **HTML Report**: `build/reports/jacoco/jacocoTestReport/html/index.html`
- **XML Report**: `build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml`

## Test Patterns

### Unit Test Pattern
```kotlin
class ViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    @Mock
    private lateinit var repository: Repository
    
    private lateinit var viewModel: ViewModel
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = ViewModel(repository)
    }
    
    @Test
    fun `test async operation`() = runTest {
        // Given
        whenever(repository.getData()).thenReturn(expectedData)
        
        // When
        viewModel.loadData()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(expectedData, state.data)
        }
    }
}
```

### Integration Test Pattern  
```kotlin
@RunWith(AndroidJUnit4::class)
class DaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: Dao
    
    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        
        dao = database.dao()
    }
    
    @Test
    fun insertAndRetrieve() = runTest {
        // Given
        val entity = TestEntity(data = "test")
        
        // When
        val id = dao.insert(entity)
        val retrieved = dao.getById(id)
        
        // Then
        assertNotNull(retrieved)
        assertEquals(entity.data, retrieved!!.data)
    }
}
```

### UI Test Pattern
```kotlin
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)
    
    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()
    
    @Test
    fun screenDisplaysCorrectContent() {
        composeTestRule.setContent {
            ScreenComposable(testData)
        }
        
        composeTestRule
            .onNodeWithText("Expected Text")
            .assertIsDisplayed()
    }
}
```

## Quality Assurance

### Test Organization
- Clear test structure with Given/When/Then patterns
- Descriptive test names following naming conventions
- Comprehensive edge case coverage
- Performance testing for critical operations

### Maintainability
- Reusable test utilities and helpers
- Mock factories for consistent test data
- Test-specific dependency injection
- Isolated test environments

### Documentation
- Each test class includes purpose documentation
- Complex test scenarios are well-commented
- Contact information for maintainers
- Testing strategy documentation

## Accessibility Testing

### Features Tested
- Screen reader compatibility
- Touch target sizing
- Color contrast validation
- Navigation accessibility
- Content descriptions

### Tools Used
- Espresso Accessibility APIs
- Android Accessibility Scanner integration
- Semantic testing in Compose

## Continuous Integration

### Pipeline Integration
- Automated test execution on PR/merge
- Coverage reporting in CI/CD
- Test result notifications
- Quality gate enforcement

### Performance Monitoring
- Test execution time tracking
- Flaky test identification
- Resource usage monitoring
- Test stability metrics

## Contact Information

**Development Team:**
- Garrett Dillman: garrett.dillman@gmail.com, garrett@sxc.codes
- Tia: tiatheone@protonmail.com

**Organization:** ChaseWhiteRabbit NGO

---

This testing implementation ensures enterprise-grade quality with comprehensive coverage across all application layers, following Android development best practices and DevOps standards.
