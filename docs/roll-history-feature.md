# Roll History and Statistics Feature

This feature provides comprehensive roll tracking, filtering, and statistical analysis for the D&D Dice Roller app.

## Architecture Overview

### Database Layer
- **DiceRollEntity**: Room entity storing individual roll records
- **DiceRollDao**: Data Access Object with queries for rolls and statistics
- **AppDatabase**: Room database configuration with type converters
- **Converters**: Type converters for LocalDateTime serialization

### Repository Layer
- **DiceRollRepository**: Manages roll data operations and provides clean API
- Handles pagination, statistics aggregation, and data cleanup

### Presentation Layer
- **RollHistoryViewModel**: Manages UI state and coordinates data operations
- **RollHistoryModels**: UI data models and filter/export configurations

## Key Components

### 1. RollHistoryScreen
Main screen showing chronological roll history with:
- Paginated list of rolls with infinite scrolling
- Search and filter capabilities
- Export functionality
- Data cleanup options

**Features:**
- Real-time roll display with timestamps
- Dice type badges and modifier calculations
- Context information (character, session, roll type)
- Pull-to-refresh and error handling
- Empty states and loading indicators

### 2. RollStatistics
Statistical analysis screen featuring:
- Overview metrics (total rolls, most used dice)
- Per-dice-type statistics (count, average, min/max)
- Roll distribution charts
- Streak analysis (critical hits, failures)

**Visualizations:**
- Simple progress bar charts for roll distributions
- Percentage breakdowns by dice type
- Statistical summaries with color coding

### 3. HistoryFilter
Advanced filtering dialog with:
- Dice type selection (multi-select chips)
- Date range filtering with quick presets
- Session and character filtering
- Context-based filtering (attack, damage, etc.)

**Filter Options:**
- Multiple dice types simultaneously
- Date ranges (last 24h, week, month, custom)
- Session ID matching
- Character name filtering
- Roll context filtering

### 4. Export Functionality
Data export with multiple formats:
- **CSV**: Spreadsheet-compatible format
- **JSON**: Developer-friendly structured data
- **PDF**: Printable formatted reports

**Export Options:**
- Include/exclude statistics
- Date range selection
- Dice type filtering
- Statistics summary inclusion

## Database Schema

### DiceRollEntity Table
```sql
CREATE TABLE dice_rolls (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    diceType TEXT NOT NULL,
    result INTEGER NOT NULL,
    modifier INTEGER DEFAULT 0,
    totalResult INTEGER NOT NULL,
    timestamp TEXT NOT NULL,
    sessionId TEXT,
    context TEXT,
    characterName TEXT
);
```

**Indexes:**
- timestamp (for chronological queries)
- diceType (for type-specific statistics)
- sessionId (for session filtering)

## Data Management

### Pagination
- Room PagingSource for efficient large dataset handling
- 20 items per page with placeholder support
- Automatic loading states and error handling

### Statistics Calculation
- Real-time aggregation queries
- Cached results for performance
- Incremental updates on new rolls

### Data Cleanup
- Automatic cleanup of old rolls (configurable retention)
- Manual clear all functionality
- Bulk delete operations by date range

## Implementation Details

### Key Queries
- `getAllRollsPaged()`: Paginated roll history
- `getDiceTypeDistribution()`: Usage statistics per dice type
- `getRollDistribution()`: Frequency analysis for specific dice
- `getAverageForDiceType()`: Statistical calculations

### Performance Considerations
- Indexed queries for fast filtering
- Pagination to handle large datasets
- Background statistics calculation
- Efficient Room type converters

### Error Handling
- Network-style error states for database operations
- Retry mechanisms for failed operations
- Graceful degradation for missing data

## Integration Points

### With Dice Engine
- Automatic roll logging through DiceRollRepository
- Context information from roll operations
- Session tracking integration

### With Main App
- Navigation integration for history access
- Settings for data retention policies
- Export sharing through Android intents

## Testing Strategy

### Unit Tests
- Repository layer tests with fake DAO
- ViewModel tests with fake repository
- Statistics calculation verification

### UI Tests
- Screen navigation and interaction
- Filter application and results
- Export dialog functionality

### Integration Tests
- Database operations end-to-end
- Pagination behavior verification
- Statistics accuracy validation

## Future Enhancements

### Advanced Analytics
- Roll distribution analysis (fairness testing)
- Luck streak notifications
- Campaign-level statistics

### Social Features
- Share interesting rolls
- Compare statistics with friends
- Roll challenges and achievements

### Enhanced Visualizations
- Interactive charts with zoom/pan
- Roll timeline visualization
- Heat maps for roll patterns

### Export Improvements
- Cloud backup integration
- Automated scheduled exports
- More format options (Excel, Google Sheets)

## Configuration

### Database Settings
- Retention period: 90 days (configurable)
- Maximum rolls stored: 10,000 (with cleanup)
- Statistics refresh interval: Real-time

### UI Settings
- Pagination size: 20 items
- Statistics cache duration: 5 minutes
- Auto-refresh on app resume

## Dependencies

```gradle
// Paging
implementation "androidx.paging:paging-runtime:3.2.1"
implementation "androidx.paging:paging-compose:3.2.1"

// Room (existing)
implementation "androidx.room:room-runtime:2.6.1"
implementation "androidx.room:room-ktx:2.6.1"

// Hilt (existing)
implementation "com.google.dagger:hilt-android:2.50"
```

This feature provides a complete roll tracking solution that enhances the D&D gaming experience with detailed history and meaningful statistics.
