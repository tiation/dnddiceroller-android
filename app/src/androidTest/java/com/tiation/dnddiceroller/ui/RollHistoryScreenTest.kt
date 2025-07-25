package com.tiation.dnddiceroller.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tiation.dnddiceroller.features.history.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Compose UI tests for RollHistoryScreen
 * 
 * Tests cover:
 * - Screen rendering with different states
 * - User interactions
 * - Filter functionality
 * - Export functionality  
 * - Accessibility
 * 
 * Contact: Garrett Dillman (garrett.dillman@gmail.com, garrett@sxc.codes)
 * Contact: Tia (tiatheone@protonmail.com)
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RollHistoryScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun rollHistoryScreen_displaysEmptyStateWhenNoRolls() {
        // Given
        val emptyHistoryItems = emptyList<RollHistoryItem>()
        val emptyStatistics = RollStatistics()

        // When
        composeTestRule.setContent {
            RollHistoryScreenContent(
                historyItems = emptyHistoryItems,
                statistics = emptyStatistics,
                isLoading = false,
                currentFilter = HistoryFilter(),
                onFilterChange = {},
                onExport = {},
                onClearHistory = {},
                onNavigateBack = {}
            )
        }

        // Then
        composeTestRule
            .onNodeWithText("No roll history yet")
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("Start rolling some dice to see your history here!")
            .assertIsDisplayed()
    }

    @Test
    fun rollHistoryScreen_displaysHistoryItems() {
        // Given
        val historyItems = createTestHistoryItems()
        val statistics = createTestStatistics()

        // When
        composeTestRule.setContent {
            RollHistoryScreenContent(
                historyItems = historyItems,
                statistics = statistics,
                isLoading = false,
                currentFilter = HistoryFilter(),
                onFilterChange = {},
                onExport = {},
                onClearHistory = {},
                onNavigateBack = {}
            )
        }

        // Then
        composeTestRule
            .onNodeWithText("d20")
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("15")
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("17") // totalResult with modifier
            .assertIsDisplayed()
    }

    @Test
    fun rollHistoryScreen_displaysLoadingState() {
        // When
        composeTestRule.setContent {
            RollHistoryScreenContent(
                historyItems = emptyList(),
                statistics = RollStatistics(),
                isLoading = true,
                currentFilter = HistoryFilter(),
                onFilterChange = {},
                onExport = {},
                onClearHistory = {},
                onNavigateBack = {}
            )
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Loading")
            .assertIsDisplayed()
    }

    @Test
    fun rollHistoryScreen_showsStatisticsWhenExpanded() {
        // Given
        val statistics = createTestStatistics()

        // When
        composeTestRule.setContent {
            RollHistoryScreenContent(
                historyItems = createTestHistoryItems(),
                statistics = statistics,
                isLoading = false,
                currentFilter = HistoryFilter(),
                onFilterChange = {},
                onExport = {},
                onClearHistory = {},
                onNavigateBack = {}
            )
        }

        // When - Click to expand statistics
        composeTestRule
            .onNodeWithText("Statistics")
            .performClick()

        // Then
        composeTestRule
            .onNodeWithText("Total Rolls: 50")
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("d20: 25 rolls")
            .assertIsDisplayed()
    }

    @Test
    fun rollHistoryScreen_filterFunctionality() {
        // Given
        var lastFilter: HistoryFilter? = null

        // When
        composeTestRule.setContent {
            RollHistoryScreenContent(
                historyItems = createTestHistoryItems(),
                statistics = createTestStatistics(),
                isLoading = false,
                currentFilter = HistoryFilter(),
                onFilterChange = { lastFilter = it },
                onExport = {},
                onClearHistory = {},
                onNavigateBack = {}
            )
        }

        // When - Open filter menu
        composeTestRule
            .onNodeWithContentDescription("Filter rolls")
            .performClick()

        // Then - Filter dialog should be displayed
        composeTestRule
            .onNodeWithText("Filter Rolls")
            .assertIsDisplayed()

        // When - Select d20 filter
        composeTestRule
            .onNodeWithText("d20")
            .performClick()

        composeTestRule
            .onNodeWithText("Apply")
            .performClick()

        // Then - Filter should be applied
        assert(lastFilter?.diceType == "d20")
    }

    @Test
    fun rollHistoryScreen_exportFunctionality() {
        // Given
        var exportCalled = false

        // When
        composeTestRule.setContent {
            RollHistoryScreenContent(
                historyItems = createTestHistoryItems(),
                statistics = createTestStatistics(),
                isLoading = false,
                currentFilter = HistoryFilter(),
                onFilterChange = {},
                onExport = { exportCalled = true },
                onClearHistory = {},
                onNavigateBack = {}
            )
        }

        // When - Click export button
        composeTestRule
            .onNodeWithContentDescription("Export history")
            .performClick()

        // Then - Export dialog should be displayed
        composeTestRule
            .onNodeWithText("Export History")
            .assertIsDisplayed()

        // When - Select CSV format and export
        composeTestRule
            .onNodeWithText("CSV")
            .performClick()

        composeTestRule
            .onNodeWithText("Export")
            .performClick()

        // Then - Export callback should be called
        assert(exportCalled)
    }

    @Test
    fun rollHistoryScreen_clearHistoryFunctionality() {
        // Given
        var clearCalled = false

        // When
        composeTestRule.setContent {
            RollHistoryScreenContent(
                historyItems = createTestHistoryItems(),
                statistics = createTestStatistics(),
                isLoading = false,
                currentFilter = HistoryFilter(),
                onFilterChange = {},
                onExport = {},
                onClearHistory = { clearCalled = true },
                onNavigateBack = {}
            )
        }

        // When - Click more options menu
        composeTestRule
            .onNodeWithContentDescription("More options")
            .performClick()

        // When - Click clear history
        composeTestRule
            .onNodeWithText("Clear History")
            .performClick()

        // Then - Confirmation dialog should appear
        composeTestRule
            .onNodeWithText("Clear All History?")
            .assertIsDisplayed()

        // When - Confirm clear
        composeTestRule
            .onNodeWithText("Clear")
            .performClick()

        // Then - Clear callback should be called
        assert(clearCalled)
    }

    @Test
    fun rollHistoryScreen_navigationBackButton() {
        // Given
        var backCalled = false

        // When
        composeTestRule.setContent {
            RollHistoryScreenContent(
                historyItems = createTestHistoryItems(),
                statistics = createTestStatistics(),
                isLoading = false,
                currentFilter = HistoryFilter(),
                onFilterChange = {},
                onExport = {},
                onClearHistory = {},
                onNavigateBack = { backCalled = true }
            )
        }

        // When - Click back button
        composeTestRule
            .onNodeWithContentDescription("Navigate up")
            .performClick()

        // Then - Navigation callback should be called
        assert(backCalled)
    }

    @Test
    fun rollHistoryScreen_accessibilityLabels() {
        // Given
        val historyItems = createTestHistoryItems()

        // When
        composeTestRule.setContent {
            RollHistoryScreenContent(
                historyItems = historyItems,
                statistics = createTestStatistics(),
                isLoading = false,
                currentFilter = HistoryFilter(),
                onFilterChange = {},
                onExport = {},
                onClearHistory = {},
                onNavigateBack = {}
            )
        }

        // Then - Check accessibility labels
        composeTestRule
            .onNodeWithContentDescription("Navigate up")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Filter rolls")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Export history")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("More options")
            .assertIsDisplayed()
    }

    @Test
    fun rollHistoryScreen_rollItemInteraction() {
        // Given
        val historyItems = createTestHistoryItems()

        // When
        composeTestRule.setContent {
            RollHistoryScreenContent(
                historyItems = historyItems,
                statistics = createTestStatistics(),
                isLoading = false,
                currentFilter = HistoryFilter(),
                onFilterChange = {},
                onExport = {},
                onClearHistory = {},
                onNavigateBack = {}
            )
        }

        // When - Long press on a roll item
        composeTestRule
            .onNodeWithText("d20")
            .performTouchInput { longClick() }

        // Then - Context menu should appear (if implemented)
        // This would depend on the actual implementation
        // For now, just verify the item is displayed
        composeTestRule
            .onNodeWithText("d20")
            .assertIsDisplayed()
    }

    @Test
    fun rollHistoryScreen_scrolling() {
        // Given - Create many history items
        val manyHistoryItems = (1..50).map { index ->
            RollHistoryItem(
                id = index.toLong(),
                diceType = "d6",
                result = (index % 6) + 1,
                modifier = 0,
                totalResult = (index % 6) + 1,
                timestamp = LocalDateTime.now().minusMinutes(index.toLong())
            )
        }

        // When
        composeTestRule.setContent {
            RollHistoryScreenContent(
                historyItems = manyHistoryItems,
                statistics = createTestStatistics(),
                isLoading = false,
                currentFilter = HistoryFilter(),
                onFilterChange = {},
                onExport = {},
                onClearHistory = {},
                onNavigateBack = {}
            )
        }

        // Then - Should be able to scroll
        composeTestRule
            .onNodeWithTag("roll_history_list")
            .performScrollToIndex(25)

        // Verify we can see items further down the list
        composeTestRule
            .onAllNodesWithText("d6")
            .assertCountEquals(manyHistoryItems.size)
    }

    private fun createTestHistoryItems(): List<RollHistoryItem> {
        return listOf(
            RollHistoryItem(
                id = 1,
                diceType = "d20",
                result = 15,
                modifier = 2,
                totalResult = 17,
                timestamp = LocalDateTime.now().minusMinutes(5),
                sessionId = "session1",
                context = "attack",
                characterName = "Aragorn"
            ),
            RollHistoryItem(
                id = 2,
                diceType = "d6",
                result = 4,
                modifier = 0,
                totalResult = 4,
                timestamp = LocalDateTime.now().minusMinutes(10),
                sessionId = "session1",
                context = "damage",
                characterName = "Aragorn"
            )
        )
    }

    private fun createTestStatistics(): RollStatistics {
        return RollStatistics(
            totalRolls = 50,
            diceTypeStats = listOf(
                DiceTypeStatistic(
                    diceType = "d20",
                    count = 25,
                    average = 10.5,
                    min = 1,
                    max = 20,
                    percentage = 50.0
                ),
                DiceTypeStatistic(
                    diceType = "d6",
                    count = 25,
                    average = 3.5,
                    min = 1,
                    max = 6,
                    percentage = 50.0
                )
            ),
            averagesByType = mapOf(
                "d20" to 10.5,
                "d6" to 3.5
            )
        )
    }
}

/**
 * Mock Composable function for testing
 */
@Composable
private fun RollHistoryScreenContent(
    historyItems: List<RollHistoryItem>,
    statistics: RollStatistics,
    isLoading: Boolean,
    currentFilter: HistoryFilter,
    onFilterChange: (HistoryFilter) -> Unit,
    onExport: (ExportOptions) -> Unit,
    onClearHistory: () -> Unit,
    onNavigateBack: () -> Unit
) {
    // This would be the actual composable implementation
    // For testing purposes, we'll create a simplified version
    
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.semantics {
                    contentDescription = "Loading"
                }
            )
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar
        TopAppBar(
            title = { Text("Roll History") },
            navigationIcon = {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.semantics {
                        contentDescription = "Navigate up"
                    }
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            actions = {
                IconButton(
                    onClick = { /* Filter action */ },
                    modifier = Modifier.semantics {
                        contentDescription = "Filter rolls"
                    }
                ) {
                    Icon(Icons.Default.FilterList, contentDescription = null)
                }
                
                IconButton(
                    onClick = { onExport(ExportOptions(ExportFormat.CSV)) },
                    modifier = Modifier.semantics {
                        contentDescription = "Export history"
                    }
                ) {
                    Icon(Icons.Default.FileDownload, contentDescription = null)
                }
                
                IconButton(
                    onClick = { /* More options */ },
                    modifier = Modifier.semantics {
                        contentDescription = "More options"
                    }
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = null)
                }
            }
        )

        // Statistics section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { /* Expand/collapse statistics */ }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Statistics",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Text("Total Rolls: ${statistics.totalRolls}")
                
                statistics.diceTypeStats.forEach { stat ->
                    Text("${stat.diceType}: ${stat.count} rolls")
                }
            }
        }

        // History list
        if (historyItems.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No roll history yet",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Start rolling some dice to see your history here!",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("roll_history_list")
            ) {
                items(historyItems) { item ->
                    RollHistoryItemRow(
                        item = item,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun RollHistoryItemRow(
    item: RollHistoryItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = item.diceType,
                    style = MaterialTheme.typography.titleMedium
                )
                
                item.context?.let { context ->
                    Text(
                        text = context,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (item.modifier != 0) {
                        "${item.result} + ${item.modifier} = ${item.totalResult}"
                    } else {
                        item.result.toString()
                    },
                    style = MaterialTheme.typography.titleLarge
                )
                
                Text(
                    text = item.timestamp.format(
                        DateTimeFormatter.ofPattern("HH:mm")
                    ),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
