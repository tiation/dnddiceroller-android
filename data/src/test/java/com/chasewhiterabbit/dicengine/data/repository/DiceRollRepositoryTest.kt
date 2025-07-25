package com.chasewhiterabbit.dicengine.data.repository

import androidx.paging.PagingSource
import app.cash.turbine.test
import com.chasewhiterabbit.dicengine.data.local.DiceRollDao
import com.chasewhiterabbit.dicengine.data.local.DiceRollEntity
import com.chasewhiterabbit.dicengine.data.local.DiceTypeCount
import com.chasewhiterabbit.dicengine.data.local.RollFrequency
import com.chasewhiterabbit.dicengine.domain.engine.DiceEngine
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import java.time.LocalDateTime

/**
 * Comprehensive unit tests for DiceRollRepository
 * 
 * Tests cover:
 * - Roll logging functionality
 * - Data retrieval methods
 * - Statistics calculations
 * - Data management operations
 * - Flow-based data access
 * - Error handling
 * 
 * Contact: Garrett Dillman (garrett.dillman@gmail.com, garrett@sxc.codes)
 * Contact: Tia (tiatheone@protonmail.com)
 */
class DiceRollRepositoryTest {

    @Mock
    private lateinit var diceRollDao: DiceRollDao

    @Mock
    private lateinit var diceEngine: DiceEngine

    @Mock
    private lateinit var mockPagingSource: PagingSource<Int, DiceRollEntity>

    private lateinit var repository: DiceRollRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = DiceRollRepository(diceRollDao, diceEngine)
    }

    @Test
    fun `logRoll should insert roll with correct data`() = runTest {
        // Given
        val expectedId = 123L
        whenever(diceRollDao.insertRoll(any())).thenReturn(expectedId)

        // When
        val result = repository.logRoll(
            diceType = "d20",
            result = 15,
            modifier = 2,
            sessionId = "session1",
            context = "attack",
            characterName = "Aragorn"
        )

        // Then
        result shouldBe expectedId
        
        argumentCaptor<DiceRollEntity>().apply {
            verify(diceRollDao).insertRoll(capture())
            
            val capturedEntity = firstValue
            capturedEntity.diceType shouldBe "d20"
            capturedEntity.result shouldBe 15
            capturedEntity.modifier shouldBe 2
            capturedEntity.totalResult shouldBe 17
            capturedEntity.sessionId shouldBe "session1"
            capturedEntity.context shouldBe "attack"
            capturedEntity.characterName shouldBe "Aragorn"
        }
    }

    @Test
    fun `logRoll should handle zero modifier correctly`() = runTest {
        // Given
        whenever(diceRollDao.insertRoll(any())).thenReturn(1L)

        // When
        repository.logRoll(
            diceType = "d6",
            result = 4,
            modifier = 0
        )

        // Then
        argumentCaptor<DiceRollEntity>().apply {
            verify(diceRollDao).insertRoll(capture())
            
            val capturedEntity = firstValue
            capturedEntity.result shouldBe 4
            capturedEntity.modifier shouldBe 0
            capturedEntity.totalResult shouldBe 4
        }
    }

    @Test
    fun `logRoll should handle negative modifier correctly`() = runTest {
        // Given
        whenever(diceRollDao.insertRoll(any())).thenReturn(1L)

        // When
        repository.logRoll(
            diceType = "d20",
            result = 10,
            modifier = -3
        )

        // Then
        argumentCaptor<DiceRollEntity>().apply {
            verify(diceRollDao).insertRoll(capture())
            
            val capturedEntity = firstValue
            capturedEntity.result shouldBe 10
            capturedEntity.modifier shouldBe -3
            capturedEntity.totalResult shouldBe 7
        }
    }

    @Test
    fun `getRollHistory should return paging flow`() = runTest {
        // Given
        whenever(diceRollDao.getAllRollsPaged()).thenReturn(mockPagingSource)

        // When
        val result = repository.getRollHistory()

        // Then
        // Verify that the flow is created (actual paging testing would require more setup)
        verify(diceRollDao).getAllRollsPaged()
    }

    @Test
    fun `getRecentRolls should return limited rolls`() = runTest {
        // Given
        val expectedRolls = listOf(
            createTestRollEntity(1, "d20", 15),
            createTestRollEntity(2, "d6", 4)
        )
        whenever(diceRollDao.getRecentRolls(10)).thenReturn(expectedRolls)

        // When
        val result = repository.getRecentRolls(10)

        // Then
        result shouldBe expectedRolls
        verify(diceRollDao).getRecentRolls(10)
    }

    @Test
    fun `getRecentRolls should use default limit when not specified`() = runTest {
        // Given
        whenever(diceRollDao.getRecentRolls(50)).thenReturn(emptyList())

        // When
        repository.getRecentRolls()

        // Then
        verify(diceRollDao).getRecentRolls(50)
    }

    @Test
    fun `getRollsAfterDate should return flow with correct date`() = runTest {
        // Given
        val startDate = LocalDateTime.now().minusDays(7)
        val expectedRolls = listOf(createTestRollEntity(1, "d20", 15))
        whenever(diceRollDao.getRollsAfterDate(startDate)).thenReturn(flowOf(expectedRolls))

        // When
        repository.getRollsAfterDate(startDate).test {
            val rolls = awaitItem()
            
            // Then
            rolls shouldBe expectedRolls
            awaitComplete()
        }
        
        verify(diceRollDao).getRollsAfterDate(startDate)
    }

    @Test
    fun `getRollsByDiceType should return flow with correct dice type`() = runTest {
        // Given
        val diceType = "d20"
        val expectedRolls = listOf(createTestRollEntity(1, diceType, 15))
        whenever(diceRollDao.getRollsByDiceType(diceType)).thenReturn(flowOf(expectedRolls))

        // When
        repository.getRollsByDiceType(diceType).test {
            val rolls = awaitItem()
            
            // Then
            rolls shouldBe expectedRolls
            awaitComplete()
        }
        
        verify(diceRollDao).getRollsByDiceType(diceType)
    }

    @Test
    fun `getRollsBySession should return flow with correct session`() = runTest {
        // Given
        val sessionId = "session123"
        val expectedRolls = listOf(createTestRollEntity(1, "d20", 15, sessionId = sessionId))
        whenever(diceRollDao.getRollsBySession(sessionId)).thenReturn(flowOf(expectedRolls))

        // When
        repository.getRollsBySession(sessionId).test {
            val rolls = awaitItem()
            
            // Then
            rolls shouldBe expectedRolls
            awaitComplete()
        }
        
        verify(diceRollDao).getRollsBySession(sessionId)
    }

    @Test
    fun `getRollsByCharacter should return flow with correct character`() = runTest {
        // Given
        val characterName = "Aragorn"
        val expectedRolls = listOf(createTestRollEntity(1, "d20", 15, characterName = characterName))
        whenever(diceRollDao.getRollsByCharacter(characterName)).thenReturn(flowOf(expectedRolls))

        // When
        repository.getRollsByCharacter(characterName).test {
            val rolls = awaitItem()
            
            // Then
            rolls shouldBe expectedRolls
            awaitComplete()
        }
        
        verify(diceRollDao).getRollsByCharacter(characterName)
    }

    @Test
    fun `getRollsByContext should return flow with correct context`() = runTest {
        // Given
        val context = "attack"
        val expectedRolls = listOf(createTestRollEntity(1, "d20", 15, context = context))
        whenever(diceRollDao.getRollsByContext(context)).thenReturn(flowOf(expectedRolls))

        // When
        repository.getRollsByContext(context).test {
            val rolls = awaitItem()
            
            // Then
            rolls shouldBe expectedRolls
            awaitComplete()
        }
        
        verify(diceRollDao).getRollsByContext(context)
    }

    @Test
    fun `getTotalRollCount should return count from dao`() = runTest {
        // Given
        val expectedCount = 42
        whenever(diceRollDao.getTotalRollCount()).thenReturn(expectedCount)

        // When
        val result = repository.getTotalRollCount()

        // Then
        result shouldBe expectedCount
        verify(diceRollDao).getTotalRollCount()
    }

    @Test
    fun `getAverageForDiceType should return average from dao`() = runTest {
        // Given
        val diceType = "d20"
        val expectedAverage = 10.5
        whenever(diceRollDao.getAverageForDiceType(diceType)).thenReturn(expectedAverage)

        // When
        val result = repository.getAverageForDiceType(diceType)

        // Then
        result shouldBe expectedAverage
        verify(diceRollDao).getAverageForDiceType(diceType)
    }

    @Test
    fun `getMaxForDiceType should return max from dao`() = runTest {
        // Given
        val diceType = "d20"
        val expectedMax = 20
        whenever(diceRollDao.getMaxForDiceType(diceType)).thenReturn(expectedMax)

        // When
        val result = repository.getMaxForDiceType(diceType)

        // Then
        result shouldBe expectedMax
        verify(diceRollDao).getMaxForDiceType(diceType)
    }

    @Test
    fun `getMinForDiceType should return min from dao`() = runTest {
        // Given
        val diceType = "d20"
        val expectedMin = 1
        whenever(diceRollDao.getMinForDiceType(diceType)).thenReturn(expectedMin)

        // When
        val result = repository.getMinForDiceType(diceType)

        // Then
        result shouldBe expectedMin
        verify(diceRollDao).getMinForDiceType(diceType)
    }

    @Test
    fun `getDiceTypeDistribution should return distribution from dao`() = runTest {
        // Given
        val expectedDistribution = listOf(
            DiceTypeCount("d20", 50),
            DiceTypeCount("d6", 30)
        )
        whenever(diceRollDao.getDiceTypeDistribution()).thenReturn(expectedDistribution)

        // When
        val result = repository.getDiceTypeDistribution()

        // Then
        result shouldBe expectedDistribution
        verify(diceRollDao).getDiceTypeDistribution()
    }

    @Test
    fun `getRollDistribution should return roll distribution from dao`() = runTest {
        // Given
        val diceType = "d20"
        val expectedDistribution = listOf(
            RollFrequency(1, 2),
            RollFrequency(20, 3)
        )
        whenever(diceRollDao.getRollDistribution(diceType)).thenReturn(expectedDistribution)

        // When
        val result = repository.getRollDistribution(diceType)

        // Then
        result shouldBe expectedDistribution
        verify(diceRollDao).getRollDistribution(diceType)
    }

    @Test
    fun `deleteRollsOlderThan should return deleted count`() = runTest {
        // Given
        val cutoffDate = LocalDateTime.now().minusDays(30)
        val expectedDeletedCount = 15
        whenever(diceRollDao.deleteRollsOlderThan(cutoffDate)).thenReturn(expectedDeletedCount)

        // When
        val result = repository.deleteRollsOlderThan(cutoffDate)

        // Then
        result shouldBe expectedDeletedCount
        verify(diceRollDao).deleteRollsOlderThan(cutoffDate)
    }

    @Test
    fun `clearAllRolls should call dao clearAllRolls`() = runTest {
        // When
        repository.clearAllRolls()

        // Then
        verify(diceRollDao).clearAllRolls()
    }

    @Test
    fun `deleteRoll should call dao deleteRoll with correct entity`() = runTest {
        // Given
        val rollEntity = createTestRollEntity(1, "d20", 15)

        // When
        repository.deleteRoll(rollEntity)

        // Then
        verify(diceRollDao).deleteRoll(rollEntity)
    }

    @Test
    fun `getRollById should return roll from dao`() = runTest {
        // Given
        val rollId = 123L
        val expectedRoll = createTestRollEntity(rollId, "d20", 15)
        whenever(diceRollDao.getRollById(rollId)).thenReturn(expectedRoll)

        // When
        val result = repository.getRollById(rollId)

        // Then
        result shouldBe expectedRoll
        verify(diceRollDao).getRollById(rollId)
    }

    @Test
    fun `getRollById should return null when roll not found`() = runTest {
        // Given
        val rollId = 999L
        whenever(diceRollDao.getRollById(rollId)).thenReturn(null)

        // When
        val result = repository.getRollById(rollId)

        // Then
        result shouldBe null
        verify(diceRollDao).getRollById(rollId)
    }

    private fun createTestRollEntity(
        id: Long,
        diceType: String,
        result: Int,
        modifier: Int = 0,
        sessionId: String? = null,
        context: String? = null,
        characterName: String? = null
    ): DiceRollEntity {
        return DiceRollEntity(
            id = id,
            diceType = diceType,
            result = result,
            modifier = modifier,
            totalResult = result + modifier,
            timestamp = LocalDateTime.now(),
            sessionId = sessionId,
            context = context,
            characterName = characterName
        )
    }
}
