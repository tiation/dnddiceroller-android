# API Documentation

This document outlines the internal APIs and data models used within the D&D Dice Roller Android application. While this is a standalone mobile app, understanding the API structure helps with development, testing, and potential future integrations.

## 🎲 Core Domain Models

### DiceType
Represents the different types of dice available in D&D.

```kotlin
enum class DiceType(
    val sides: Int,
    val displayName: String,
    val description: String
) {
    D4(4, "d4", "Four-sided die (tetrahedron)"),
    D6(6, "d6", "Six-sided die (cube)"),
    D8(8, "d8", "Eight-sided die (octahedron)"),
    D10(10, "d10", "Ten-sided die (pentagonal trapezohedron)"),
    D12(12, "d12", "Twelve-sided die (dodecahedron)"),
    D20(20, "d20", "Twenty-sided die (icosahedron)"),
    D100(100, "d100", "Hundred-sided die (percentile)")
}
```

### DiceRoll
Represents a single dice roll with its result and metadata.

```kotlin
data class DiceRoll(
    val id: String = UUID.randomUUID().toString(),
    val diceType: DiceType,
    val result: Int,
    val timestamp: Instant = Clock.System.now(),
    val campaignId: String? = null,
    val characterName: String? = null,
    val rollReason: String? = null
) {
    val isValid: Boolean
        get() = result in 1..diceType.sides
}
```

### DiceSet
Represents a collection of dice to be rolled together.

```kotlin
data class DiceSet(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val dice: List<DiceConfiguration>,
    val modifier: Int = 0,
    val description: String? = null,
    val campaignId: String? = null
) {
    val totalPossibleMinimum: Int
        get() = dice.sumOf { it.count } + modifier
    
    val totalPossibleMaximum: Int
        get() = dice.sumOf { it.count * it.diceType.sides } + modifier
}

data class DiceConfiguration(
    val diceType: DiceType,
    val count: Int
)
```

### DiceSetRoll
Represents the result of rolling a complete dice set.

```kotlin
data class DiceSetRoll(
    val id: String = UUID.randomUUID().toString(),
    val diceSet: DiceSet,
    val individualRolls: List<DiceRoll>,
    val modifier: Int,
    val timestamp: Instant = Clock.System.now(),
    val campaignId: String? = null,
    val characterName: String? = null,
    val rollReason: String? = null
) {
    val total: Int
        get() = individualRolls.sumOf { it.result } + modifier
    
    val isValid: Boolean
        get() = individualRolls.all { it.isValid }
}
```

### Campaign
Represents a D&D campaign for organizing dice rolls and sets.

```kotlin
data class Campaign(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String? = null,
    val createdAt: Instant = Clock.System.now(),
    val lastUsed: Instant = Clock.System.now(),
    val isActive: Boolean = true
)
```

## 🔧 Use Cases API

### RollDiceUseCase
Handles individual dice rolling operations.

```kotlin
class RollDiceUseCase @Inject constructor(
    private val diceRepository: DiceRepository,
    private val randomGenerator: RandomGenerator = SecureRandom()
) {
    suspend operator fun invoke(
        diceType: DiceType,
        campaignId: String? = null,
        characterName: String? = null,
        rollReason: String? = null
    ): Result<DiceRoll> {
        // Implementation details
    }
}
```

**Parameters:**
- `diceType`: The type of dice to roll
- `campaignId`: Optional campaign identifier
- `characterName`: Optional character name
- `rollReason`: Optional reason for the roll

**Returns:** `Result<DiceRoll>` - Success with roll result or failure with error

### RollDiceSetUseCase
Handles rolling multiple dice as a set.

```kotlin
class RollDiceSetUseCase @Inject constructor(
    private val rollDiceUseCase: RollDiceUseCase,
    private val diceRepository: DiceRepository
) {
    suspend operator fun invoke(
        diceSet: DiceSet,
        campaignId: String? = null,
        characterName: String? = null,
        rollReason: String? = null
    ): Result<DiceSetRoll>
}
```

### GetRollHistoryUseCase
Retrieves roll history with filtering options.

```kotlin
class GetRollHistoryUseCase @Inject constructor(
    private val diceRepository: DiceRepository
) {
    operator fun invoke(
        campaignId: String? = null,
        limit: Int = 50,
        offset: Int = 0
    ): Flow<List<DiceRoll>>
}
```

### CreateCampaignUseCase
Creates a new campaign.

```kotlin
class CreateCampaignUseCase @Inject constructor(
    private val campaignRepository: CampaignRepository
) {
    suspend operator fun invoke(
        name: String,
        description: String? = null
    ): Result<Campaign>
}
```

## 🗄️ Repository Interfaces

### DiceRepository
Manages dice rolls and dice sets.

```kotlin
interface DiceRepository {
    suspend fun saveDiceRoll(diceRoll: DiceRoll): Result<Unit>
    suspend fun saveDiceSetRoll(diceSetRoll: DiceSetRoll): Result<Unit>
    suspend fun getDiceRoll(id: String): Result<DiceRoll?>
    fun getRollHistory(
        campaignId: String? = null,
        limit: Int = 50,
        offset: Int = 0
    ): Flow<List<DiceRoll>>
    suspend fun deleteDiceRoll(id: String): Result<Unit>
    suspend fun clearRollHistory(campaignId: String? = null): Result<Unit>
    
    suspend fun saveDiceSet(diceSet: DiceSet): Result<Unit>
    suspend fun getDiceSet(id: String): Result<DiceSet?>
    fun getDiceSets(campaignId: String? = null): Flow<List<DiceSet>>
    suspend fun deleteDiceSet(id: String): Result<Unit>
}
```

### CampaignRepository
Manages campaigns.

```kotlin
interface CampaignRepository {
    suspend fun createCampaign(campaign: Campaign): Result<Unit>
    suspend fun updateCampaign(campaign: Campaign): Result<Unit>
    suspend fun getCampaign(id: String): Result<Campaign?>
    fun getAllCampaigns(): Flow<List<Campaign>>
    fun getActiveCampaigns(): Flow<List<Campaign>>
    suspend fun deleteCampaign(id: String): Result<Unit>
    suspend fun archiveCampaign(id: String): Result<Unit>
}
```

## 📊 Data Layer APIs

### Room Database Entities

#### DiceRollEntity
```kotlin
@Entity(
    tableName = "dice_rolls",
    indices = [
        Index(value = ["campaign_id"]),
        Index(value = ["timestamp"])
    ]
)
data class DiceRollEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "dice_type") val diceType: String,
    val result: Int,
    val timestamp: Long,
    @ColumnInfo(name = "campaign_id") val campaignId: String?,
    @ColumnInfo(name = "character_name") val characterName: String?,
    @ColumnInfo(name = "roll_reason") val rollReason: String?
)
```

#### DiceSetEntity
```kotlin
@Entity(
    tableName = "dice_sets",
    indices = [Index(value = ["campaign_id"])]
)
data class DiceSetEntity(
    @PrimaryKey val id: String,
    val name: String,
    val modifier: Int,
    val description: String?,
    @ColumnInfo(name = "campaign_id") val campaignId: String?
)
```

### Room DAOs

#### DiceRollDao
```kotlin
@Dao
interface DiceRollDao {
    @Query("SELECT * FROM dice_rolls WHERE id = :id")
    suspend fun getDiceRoll(id: String): DiceRollEntity?
    
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
    
    @Delete
    suspend fun deleteDiceRoll(diceRoll: DiceRollEntity)
    
    @Query("DELETE FROM dice_rolls WHERE campaign_id = :campaignId OR :campaignId IS NULL")
    suspend fun clearRollHistory(campaignId: String?)
}
```

## 🎨 UI State APIs

### DiceRollerUiState
```kotlin
data class DiceRollerUiState(
    val selectedDiceType: DiceType = DiceType.D20,
    val currentRoll: DiceRoll? = null,
    val rollHistory: List<DiceRoll> = emptyList(),
    val isRolling: Boolean = false,
    val error: UiText? = null
)
```

### DiceSetUiState
```kotlin
data class DiceSetUiState(
    val availableDiceSets: List<DiceSet> = emptyList(),
    val selectedDiceSet: DiceSet? = null,
    val currentSetRoll: DiceSetRoll? = null,
    val isRolling: Boolean = false,
    val isLoading: Boolean = false,
    val error: UiText? = null
)
```

### CampaignUiState
```kotlin
data class CampaignUiState(
    val campaigns: List<Campaign> = emptyList(),
    val selectedCampaign: Campaign? = null,
    val isLoading: Boolean = false,
    val isCreatingCampaign: Boolean = false,
    val error: UiText? = null
)
```

## 🔄 ViewModel APIs

### DiceRollerViewModel
```kotlin
class DiceRollerViewModel @Inject constructor(
    private val rollDiceUseCase: RollDiceUseCase,
    private val getRollHistoryUseCase: GetRollHistoryUseCase
) : ViewModel() {
    
    val uiState: StateFlow<DiceRollerUiState>
    
    fun rollDice(diceType: DiceType)
    fun selectDiceType(diceType: DiceType)
    fun clearError()
    fun clearRollHistory()
}
```

## 🛠️ Utility APIs

### RandomGenerator
Interface for generating random numbers (testable).

```kotlin
interface RandomGenerator {
    fun nextInt(bound: Int): Int
    fun nextInt(from: Int, until: Int): Int
}

class SecureRandomGenerator : RandomGenerator {
    private val random = SecureRandom()
    
    override fun nextInt(bound: Int): Int = random.nextInt(bound)
    override fun nextInt(from: Int, until: Int): Int = random.nextInt(until - from) + from
}
```

### UiText
Sealed class for UI text that supports both string resources and direct strings.

```kotlin
sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    data class StringResource(@StringRes val id: Int, val args: Array<Any> = emptyArray()) : UiText()
}
```

## 🔍 Error Handling

### DiceRollerError
```kotlin
sealed class DiceRollerError : Exception() {
    object InvalidDiceType : DiceRollerError()
    object DatabaseError : DiceRollerError()
    object NetworkError : DiceRollerError()
    data class ValidationError(val field: String, val message: String) : DiceRollerError()
    data class UnknownError(val cause: Throwable) : DiceRollerError()
}
```

## 📋 Testing APIs

### Test Fixtures
```kotlin
object DiceRollTestFixtures {
    fun createDiceRoll(
        diceType: DiceType = DiceType.D20,
        result: Int = 15,
        campaignId: String? = null
    ): DiceRoll = DiceRoll(
        diceType = diceType,
        result = result,
        campaignId = campaignId
    )
}
```

### Mock Repositories
```kotlin
class FakeDiceRepository : DiceRepository {
    private val diceRolls = mutableListOf<DiceRoll>()
    private val diceSets = mutableListOf<DiceSet>()
    
    override suspend fun saveDiceRoll(diceRoll: DiceRoll): Result<Unit> {
        diceRolls.add(diceRoll)
        return Result.success(Unit)
    }
    
    // Additional implementations...
}
```

## 🔐 Security Considerations

### Input Validation
- All dice rolls are validated against their type's maximum sides
- Campaign names are sanitized to prevent injection attacks
- Roll reasons have character limits to prevent abuse

### Data Privacy
- No personal data is collected beyond optional character names
- All data remains local to the device
- Export functionality includes privacy warnings

---

This API documentation serves as a comprehensive guide for developers working on the D&D Dice Roller Android application. It defines clear contracts between layers and ensures consistent implementation across the codebase.
