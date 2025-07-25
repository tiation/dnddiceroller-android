---
name: "🔴 HIGH: Presentation Layer Separation"
about: Extract ViewModels and separate UI logic from business logic
title: "[REFACTOR] Presentation Layer Separation - Extract ViewModels"
labels: ["enhancement", "architecture", "high-priority", "custom-dice-rows"]
assignees: []
---

## 🎯 **Objective**
Refactor the presentation layer to properly separate UI logic from business logic by extracting ViewModels and implementing proper state management.

## 🔍 **Current Issues**
- MainActivity contains mixed business logic and UI state management
- State is managed locally in Composables rather than ViewModels
- No proper separation between UI and business logic
- Difficult to test UI behavior

## 🛠️ **Tasks**

### Phase 1: MainActivity Refactoring
- [ ] Extract `MainScreenViewModel` from MainActivity
- [ ] Move roll history state management to ViewModel
- [ ] Implement proper state holders for UI state
- [ ] Add ViewModel tests

### Phase 2: Feature Screen ViewModels
- [ ] Create `RollHistoryViewModel`
- [ ] Create `RollStatisticsViewModel`
- [ ] Implement proper error handling in ViewModels
- [ ] Add loading states for asynchronous operations

### Phase 3: State Management
- [ ] Implement proper UI state classes
- [ ] Add state validation logic
- [ ] Implement proper event handling pattern
- [ ] Add state persistence where needed

## 📋 **Acceptance Criteria**
- [ ] All screens have dedicated ViewModels
- [ ] Business logic is moved out of Composables
- [ ] State management follows Android Architecture Guidelines
- [ ] All ViewModels have comprehensive unit tests
- [ ] UI state is properly typed and validated
- [ ] Error handling is consistent across all screens

## 🔗 **Related**
- Architecture Audit Document: `docs/architecture-audit-custom-dice-rows.md`
- Depends on: None
- Blocks: Feature Module Architecture (#next-issue)

## 📝 **Technical Notes**
```kotlin
// Example ViewModel structure
class MainScreenViewModel @Inject constructor(
    private val diceRollRepository: DiceRollRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()
    
    // Business logic methods
}

data class MainScreenUiState(
    val rollHistory: List<DiceRoll> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
```

## 🚀 **Implementation Strategy**
1. Start with MainActivity as it's the most complex
2. Create base ViewModel classes for common functionality
3. Extract state management incrementally
4. Add tests for each ViewModel before moving to the next

---
**Priority:** 🔴 High  
**Effort:** Medium (2-3 days)  
**Feature:** Custom Dice Rows Foundation
