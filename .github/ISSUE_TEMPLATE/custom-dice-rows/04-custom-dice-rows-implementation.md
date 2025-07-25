---
name: "🚀 FEATURE: Custom Dice Rows Implementation"
about: Implement the core custom dice rows feature
title: "[FEATURE] Custom Dice Rows - Core Implementation"
labels: ["enhancement", "feature", "custom-dice-rows"]
assignees: []
---

## 🎯 **Objective**
Implement the custom dice rows feature allowing users to create, edit, and manage custom dice configurations with drag-and-drop functionality.

## ✨ **Feature Requirements**
- Users can create custom rows of dice
- Each row can contain multiple dice slots with different configurations
- Drag and drop functionality for reordering dice
- Save/load custom configurations
- Quick access to frequently used configurations

## 🛠️ **Tasks**

### Phase 1: Data Layer Extensions
- [ ] Create `CustomDiceRow` entity
- [ ] Create `DiceSlotConfiguration` entity
- [ ] Add database migration for new entities
- [ ] Extend `DiceRollRepository` with custom row methods
- [ ] Add Room DAOs for custom row management
- [ ] Create repository tests

### Phase 2: Domain Layer Extensions
- [ ] Create `CreateCustomRowUseCase`
- [ ] Create `UpdateCustomRowUseCase`
- [ ] Create `DeleteCustomRowUseCase`
- [ ] Create `GetCustomRowsUseCase`
- [ ] Add validation logic for dice configurations
- [ ] Create domain model for custom row management
- [ ] Add use case tests

### Phase 3: Presentation Layer Implementation
- [ ] Create `CustomRowViewModel`
- [ ] Create `DiceConfigurationViewModel`
- [ ] Implement `CustomRowEditorScreen`
- [ ] Implement `DiceSlotConfigurationDialog`
- [ ] Add drag-and-drop functionality
- [ ] Create custom row UI components
- [ ] Add presentation layer tests

### Phase 4: Integration & Navigation
- [ ] Add navigation routes for custom row screens
- [ ] Integrate with main dice roller screen
- [ ] Add custom row selection UI
- [ ] Implement quick-access functionality
- [ ] Add proper state management
- [ ] Create integration tests

## 📋 **Acceptance Criteria**
- [ ] Users can create new custom dice rows
- [ ] Users can edit existing custom dice rows
- [ ] Users can delete custom dice rows
- [ ] Drag and drop works smoothly for reordering
- [ ] Custom rows are persisted between app sessions
- [ ] Quick access to custom rows from main screen
- [ ] Proper error handling and validation
- [ ] Comprehensive test coverage (>80%)

## 🎨 **UI/UX Requirements**
- Intuitive drag-and-drop interface
- Clear visual feedback during interactions
- Accessible design following Material Design 3
- Smooth animations and transitions
- Error states and loading indicators

## 🔗 **Related**
- Architecture Audit Document: `docs/architecture-audit-custom-dice-rows.md`
- Depends on: All previous refactoring issues
- Feature Specification: `docs/features/custom-dice-rows.md` (to be created)

## 📝 **Technical Notes**
```kotlin
// Example data models
@Entity(tableName = "custom_dice_rows")
data class CustomDiceRow(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val slots: List<DiceSlotConfiguration>,
    val createdAt: LocalDateTime,
    val lastUsed: LocalDateTime?
)

@Entity(tableName = "dice_slot_configurations")
data class DiceSlotConfiguration(
    @PrimaryKey val id: String,
    val rowId: String,
    val position: Int,
    val diceType: Int,
    val modifier: Int,
    val rollType: RollType,
    val label: String?
)
```

## 🚀 **Implementation Strategy**
1. Start with data layer to establish persistence
2. Build domain layer use cases with proper validation
3. Create presentation layer components incrementally
4. Add comprehensive testing throughout
5. Integrate with existing navigation and UI

## 🧪 **Testing Strategy**
- Unit tests for all use cases and ViewModels
- Integration tests for database operations
- UI tests for drag-and-drop functionality
- End-to-end tests for complete user workflows

---
**Priority:** 🚀 Feature  
**Effort:** High (1-2 weeks)  
**Feature:** Custom Dice Rows Core
