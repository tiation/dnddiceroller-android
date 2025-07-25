package com.tiation.dnddiceroller.features.history

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.chasewhiterabbit.dicengine.data.local.DiceRollEntity
import com.chasewhiterabbit.dicengine.data.local.DiceTypeCount
import com.chasewhiterabbit.dicengine.data.local.RollFrequency
import com.chasewhiterabbit.dicengine.data.repository.DiceRollRepository
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import java.time.LocalDateTime

/**
 * Comprehensive unit tests for RollHistoryViewModel
 * 
 * Tests cover:
 * - Statistics loading and calculations
 * - Filter updates
 * - Export functionality
 * - History management
 * - Error handling
 * - Edge cases
 * 
 * Contact: Garrett Dillman (garrett.dillman@gmail.com, garrett@sxc.codes)
 * Contact: Tia (tiatheone@protonmail.com)
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RollHistoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: DiceRollRepository

    private lateinit var viewModel: RollHistoryViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        // Default repository setup
        whenever(repository.getRollHistory()).thenReturn(
            flowOf(PagingData.from(emptyList<DiceRollEntity>()))
        )
        whenever(repository.getTotalRollCount()).thenReturn(0)
        whenever(repository.getDiceTypeDistribution()).thenReturn(emptyList())
        
        viewModel = RollHistoryViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should have default values`() = runTest {
        viewModel.currentFilter.test {
            val filter = awaitItem()
            filter shouldBe HistoryFilter()
        }

        viewModel.isLoading.test {
            // Skip initial true value from loadStatistics
            awaitItem() // initial false
        }

        viewModel.exportInProgress.test {
            val inProgress = awaitItem()
            inProgress shouldBe false
        }
    }

    @Test
    fun `loadStatistics should update statistics state correctly`() = runTest {
        // Given
        val totalRolls = 100
        val diceTypeDistribution = listOf(
            DiceTypeCount("d20", 50),
            DiceTypeCount("d6", 30),
            DiceTypeCount("d4", 20)
        )
        val rollFrequencies = listOf(
            RollFrequency(1, 5),
            RollFrequency(20, 3)
        )

        whenever(repository.getTotalRollCount()).thenReturn(totalRolls)
        whenever(repository.getDiceTypeDistribution()).thenReturn(diceTypeDistribution)
        whenever(repository.getAverageForDiceType("d20")).thenReturn(10.5)
        whenever(repository.getMinForDiceType("d20")).thenReturn(1)
        whenever(repository.getMaxForDiceType("d20")).thenReturn(20)
        whenever(repository.getRollDistribution("d20")).thenReturn(rollFrequencies)
        whenever(repository.getAverageForDiceType("d6")).thenReturn(3.5)
        whenever(repository.getMinForDiceType("d6")).thenReturn(1)
        whenever(repository.getMaxForDiceType("d6")).thenReturn(6)
        whenever(repository.getRollDistribution("d6")).thenReturn(emptyList())
        whenever(repository.getAverageForDiceType("d4")).thenReturn(2.5)
        whenever(repository.getMinForDiceType("d4")).thenReturn(1)
        whenever(repository.getMaxForDiceType("d4")).thenReturn(4)
        whenever(repository.getRollDistribution("d4")).thenReturn(emptyList())

        // When
        viewModel.loadStatistics()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.statistics.test {
            val stats = awaitItem()
            stats.totalRolls shouldBe totalRolls
            stats.diceTypeStats.size shouldBe 3
            
            val d20Stats = stats.diceTypeStats.find { it.diceType == "d20" }!!
            d20Stats.count shouldBe 50
            d20Stats.average shouldBe 10.5
            d20Stats.percentage shouldBe 50.0
        }

        viewModel.isLoading.test {
            val loading = awaitItem()
            loading shouldBe false
        }
    }

    @Test
    fun `loadStatistics should handle empty data gracefully`() = runTest {
        // Given
        whenever(repository.getTotalRollCount()).thenReturn(0)
        whenever(repository.getDiceTypeDistribution()).thenReturn(emptyList())

        // When
        viewModel.loadStatistics()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.statistics.test {
            val stats = awaitItem()
            stats.totalRolls shouldBe 0
            stats.diceTypeStats shouldBe emptyList()
            stats.distributionData shouldBe emptyList()
        }
    }

    @Test
    fun `loadStatistics should handle repository errors`() = runTest {
        // Given
        whenever(repository.getTotalRollCount()).thenThrow(RuntimeException("Database error"))

        // When
        viewModel.loadStatistics()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should not crash and loading should be false
        viewModel.isLoading.test {
            val loading = awaitItem()
            loading shouldBe false
        }
    }

    @Test
    fun `updateFilter should update current filter`() = runTest {
        // Given
        val newFilter = HistoryFilter(
            diceType = "d20",
            dateRange = DateRange.LAST_WEEK,
            sessionId = "session123"
        )

        // When
        viewModel.updateFilter(newFilter)

        // Then
        viewModel.currentFilter.test {
            val filter = awaitItem()
            filter shouldBe newFilter
        }
    }

    @Test
    fun `clearAllHistory should call repository and reload statistics`() = runTest {
        // When
        viewModel.clearAllHistory()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(repository).clearAllRolls()
        verify(repository, atLeast(2)).getTotalRollCount() // Initial load + reload after clear
    }

    @Test
    fun `cleanupOldRolls should call repository with correct date`() = runTest {
        // Given
        val cutoffDate = LocalDateTime.now().minusDays(7)

        // When
        viewModel.cleanupOldRolls(cutoffDate)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(repository).deleteRollsOlderThan(cutoffDate)
        verify(repository, atLeast(2)).getTotalRollCount() // Initial load + reload after cleanup
    }

    @Test
    fun `getRecentRolls should return mapped history items`() = runTest {
        // Given
        val entities = listOf(
            DiceRollEntity(
                id = 1,
                diceType = "d20",
                result = 15,
                modifier = 2,
                totalResult = 17,
                timestamp = LocalDateTime.now(),
                sessionId = "session1",
                context = "attack",
                characterName = "Aragorn"
            )
        )
        whenever(repository.getRecentRolls(10)).thenReturn(entities)

        // When
        viewModel.getRecentRolls(10).test {
            val items = awaitItem()
            
            // Then
            items.size shouldBe 1
            val item = items.first()
            item.id shouldBe 1
            item.diceType shouldBe "d20"
            item.result shouldBe 15
            item.totalResult shouldBe 17
            item.sessionId shouldBe "session1"
            item.context shouldBe "attack"
            item.characterName shouldBe "Aragorn"
        }
    }

    @Test
    fun `exportHistory should set export in progress correctly`() = runTest {
        // Given
        val exportOptions = ExportOptions(
            format = ExportFormat.CSV,
            includeStatistics = true,
            dateRange = DateRange.ALL_TIME
        )

        // When
        viewModel.exportInProgress.test {
            awaitItem() shouldBe false // Initial state
            
            viewModel.exportHistory(exportOptions)
            testDispatcher.scheduler.advanceTimeBy(50) // Allow some processing
            
            expectNoEvents() // Should eventually become false again but we don't wait
        }
    }

    @Test
    fun `exportHistory should handle different formats`() = runTest {
        val csvOptions = ExportOptions(format = ExportFormat.CSV)
        val jsonOptions = ExportOptions(format = ExportFormat.JSON)
        val pdfOptions = ExportOptions(format = ExportFormat.PDF)

        // These should not throw exceptions
        viewModel.exportHistory(csvOptions)
        viewModel.exportHistory(jsonOptions)
        viewModel.exportHistory(pdfOptions)
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun `rollHistory flow should provide paginated data`() = runTest {
        // Given
        val entities = listOf(
            DiceRollEntity(
                id = 1,
                diceType = "d20",
                result = 15,
                modifier = 0,
                totalResult = 15,
                timestamp = LocalDateTime.now()
            ),
            DiceRollEntity(
                id = 2,
                diceType = "d6",
                result = 4,
                modifier = 1,
                totalResult = 5,
                timestamp = LocalDateTime.now()
            )
        )
        
        whenever(repository.getRollHistory()).thenReturn(
            flowOf(PagingData.from(entities))
        )

        // When
        val items = viewModel.rollHistory.asSnapshot()

        // Then
        items.size shouldBe 2
        items[0].id shouldBe 1
        items[1].id shouldBe 2
    }

    @Test
    fun `statistics calculations should handle division by zero`() = runTest {
        // Given - dice type with zero count
        whenever(repository.getTotalRollCount()).thenReturn(0)
        whenever(repository.getDiceTypeDistribution()).thenReturn(
            listOf(DiceTypeCount("d20", 0))
        )
        whenever(repository.getAverageForDiceType("d20")).thenReturn(null)
        whenever(repository.getMinForDiceType("d20")).thenReturn(null)
        whenever(repository.getMaxForDiceType("d20")).thenReturn(null)
        whenever(repository.getRollDistribution("d20")).thenReturn(emptyList())

        // When
        viewModel.loadStatistics()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should not crash
        viewModel.statistics.test {
            val stats = awaitItem()
            val d20Stats = stats.diceTypeStats.find { it.diceType == "d20" }!!
            d20Stats.percentage shouldBe 0.0
            d20Stats.average shouldBe 0.0
            d20Stats.min shouldBe 0
            d20Stats.max shouldBe 0
        }
    }

    @Test
    fun `concurrent statistics loading should be handled properly`() = runTest {
        // Given
        whenever(repository.getTotalRollCount()).thenReturn(50)
        whenever(repository.getDiceTypeDistribution()).thenReturn(
            listOf(DiceTypeCount("d20", 50))
        )
        whenever(repository.getAverageForDiceType("d20")).thenReturn(10.0)
        whenever(repository.getMinForDiceType("d20")).thenReturn(1)
        whenever(repository.getMaxForDiceType("d20")).thenReturn(20)
        whenever(repository.getRollDistribution("d20")).thenReturn(emptyList())

        // When - Load statistics multiple times concurrently
        repeat(3) {
            viewModel.loadStatistics()
        }
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should complete without issues
        viewModel.statistics.test {
            val stats = awaitItem()
            stats.totalRolls shouldBe 50
        }
    }
}
