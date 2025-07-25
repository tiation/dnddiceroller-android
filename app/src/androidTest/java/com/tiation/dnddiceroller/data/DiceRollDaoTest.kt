package com.tiation.dnddiceroller.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.testing.asSnapshot
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.chasewhiterabbit.dicengine.data.local.AppDatabase
import com.chasewhiterabbit.dicengine.data.local.DiceRollDao
import com.chasewhiterabbit.dicengine.data.local.DiceRollEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

/**
 * Integration tests for DiceRollDao
 * 
 * Tests the actual database operations with Room
 * 
 * Contact: Garrett Dillman (garrett.dillman@gmail.com, garrett@sxc.codes)
 * Contact: Tia (tiatheone@protonmail.com)
 */
@RunWith(AndroidJUnit4::class)
class DiceRollDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var diceRollDao: DiceRollDao

    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        
        diceRollDao = database.diceRollDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertRoll_shouldReturnValidId() = runTest {
        // Given
        val rollEntity = createTestRollEntity(diceType = "d20", result = 15)

        // When
        val insertedId = diceRollDao.insertRoll(rollEntity)

        // Then
        assertTrue("Inserted ID should be greater than 0", insertedId > 0)
    }

    @Test
    fun insertRoll_shouldPersistAllFields() = runTest {
        // Given
        val rollEntity = createTestRollEntity(
            diceType = "d20",
            result = 15,
            modifier = 2,
            sessionId = "session1",
            context = "attack",
            characterName = "Aragorn"
        )

        // When
        val insertedId = diceRollDao.insertRoll(rollEntity)
        val retrievedRoll = diceRollDao.getRollById(insertedId)

        // Then
        assertNotNull("Retrieved roll should not be null", retrievedRoll)
        assertEquals("d20", retrievedRoll!!.diceType)
        assertEquals(15, retrievedRoll.result)
        assertEquals(2, retrievedRoll.modifier)
        assertEquals(17, retrievedRoll.totalResult)
        assertEquals("session1", retrievedRoll.sessionId)
        assertEquals("attack", retrievedRoll.context)
        assertEquals("Aragorn", retrievedRoll.characterName)
    }

    @Test
    fun getRecentRolls_shouldReturnLimitedResults() = runTest {
        // Given - Insert multiple rolls
        repeat(15) { index ->
            val roll = createTestRollEntity(
                diceType = "d6",
                result = index + 1
            )
            diceRollDao.insertRoll(roll)
        }

        // When
        val recentRolls = diceRollDao.getRecentRolls(10)

        // Then
        assertEquals("Should return exactly 10 rolls", 10, recentRolls.size)
        // Should be ordered by timestamp DESC (most recent first)
        assertTrue("First roll should have higher result", 
            recentRolls[0].result > recentRolls[9].result)
    }

    @Test
    fun getRollsByDiceType_shouldFilterCorrectly() = runTest {
        // Given
        val d20Roll = createTestRollEntity(diceType = "d20", result = 15)
        val d6Roll = createTestRollEntity(diceType = "d6", result = 4)
        val anotherD20Roll = createTestRollEntity(diceType = "d20", result = 8)

        diceRollDao.insertRoll(d20Roll)
        diceRollDao.insertRoll(d6Roll)
        diceRollDao.insertRoll(anotherD20Roll)

        // When
        diceRollDao.getRollsByDiceType("d20").test {
            val d20Rolls = awaitItem()

            // Then
            assertEquals("Should return 2 d20 rolls", 2, d20Rolls.size)
            assertTrue("All rolls should be d20", 
                d20Rolls.all { it.diceType == "d20" })
            awaitComplete()
        }
    }

    @Test
    fun getRollsBySession_shouldFilterCorrectly() = runTest {
        // Given
        val session1Roll = createTestRollEntity(
            diceType = "d20", 
            result = 15, 
            sessionId = "session1"
        )
        val session2Roll = createTestRollEntity(
            diceType = "d6", 
            result = 4, 
            sessionId = "session2"
        )
        val anotherSession1Roll = createTestRollEntity(
            diceType = "d4", 
            result = 3, 
            sessionId = "session1"
        )

        diceRollDao.insertRoll(session1Roll)
        diceRollDao.insertRoll(session2Roll)
        diceRollDao.insertRoll(anotherSession1Roll)

        // When
        diceRollDao.getRollsBySession("session1").test {
            val sessionRolls = awaitItem()

            // Then
            assertEquals("Should return 2 session1 rolls", 2, sessionRolls.size)
            assertTrue("All rolls should be from session1", 
                sessionRolls.all { it.sessionId == "session1" })
            awaitComplete()
        }
    }

    @Test
    fun getTotalRollCount_shouldReturnCorrectCount() = runTest {
        // Given - Insert 5 rolls
        repeat(5) {
            val roll = createTestRollEntity(diceType = "d6", result = it + 1)
            diceRollDao.insertRoll(roll)
        }

        // When
        val totalCount = diceRollDao.getTotalRollCount()

        // Then
        assertEquals("Should return total count of 5", 5, totalCount)
    }

    @Test
    fun getAverageForDiceType_shouldCalculateCorrectly() = runTest {
        // Given - Insert d6 rolls with results 1, 2, 3, 4, 5, 6
        repeat(6) { index ->
            val roll = createTestRollEntity(diceType = "d6", result = index + 1)
            diceRollDao.insertRoll(roll)
        }

        // When
        val average = diceRollDao.getAverageForDiceType("d6")

        // Then
        assertNotNull("Average should not be null", average)
        assertEquals("Average should be 3.5", 3.5, average!!, 0.01)
    }

    @Test
    fun getMinMaxForDiceType_shouldReturnCorrectValues() = runTest {
        // Given - Insert d20 rolls with various results
        val results = listOf(1, 8, 15, 20, 12)
        results.forEach { result ->
            val roll = createTestRollEntity(diceType = "d20", result = result)
            diceRollDao.insertRoll(roll)
        }

        // When
        val min = diceRollDao.getMinForDiceType("d20")
        val max = diceRollDao.getMaxForDiceType("d20")

        // Then
        assertEquals("Min should be 1", 1, min)
        assertEquals("Max should be 20", 20, max)
    }

    @Test
    fun getDiceTypeDistribution_shouldReturnCorrectCounts() = runTest {
        // Given
        repeat(5) { diceRollDao.insertRoll(createTestRollEntity(diceType = "d20", result = 10)) }
        repeat(3) { diceRollDao.insertRoll(createTestRollEntity(diceType = "d6", result = 3)) }
        repeat(2) { diceRollDao.insertRoll(createTestRollEntity(diceType = "d4", result = 2)) }

        // When
        val distribution = diceRollDao.getDiceTypeDistribution()

        // Then
        assertEquals("Should have 3 dice types", 3, distribution.size)
        
        val d20Count = distribution.find { it.diceType == "d20" }?.count
        val d6Count = distribution.find { it.diceType == "d6" }?.count
        val d4Count = distribution.find { it.diceType == "d4" }?.count
        
        assertEquals("d20 count should be 5", 5, d20Count)
        assertEquals("d6 count should be 3", 3, d6Count)
        assertEquals("d4 count should be 2", 2, d4Count)
    }

    @Test
    fun getRollDistribution_shouldReturnCorrectFrequencies() = runTest {
        // Given - Insert multiple d6 rolls with specific results
        val results = listOf(1, 1, 2, 2, 2, 3, 6, 6)
        results.forEach { result ->
            val roll = createTestRollEntity(diceType = "d6", result = result)
            diceRollDao.insertRoll(roll)
        }

        // When
        val distribution = diceRollDao.getRollDistribution("d6")

        // Then
        assertEquals("Should have 4 different results", 4, distribution.size)
        
        val result1Freq = distribution.find { it.result == 1 }?.frequency
        val result2Freq = distribution.find { it.result == 2 }?.frequency
        val result3Freq = distribution.find { it.result == 3 }?.frequency
        val result6Freq = distribution.find { it.result == 6 }?.frequency
        
        assertEquals("Result 1 should appear 2 times", 2, result1Freq)
        assertEquals("Result 2 should appear 3 times", 3, result2Freq)
        assertEquals("Result 3 should appear 1 time", 1, result3Freq)
        assertEquals("Result 6 should appear 2 times", 2, result6Freq)
    }

    @Test
    fun deleteRollsOlderThan_shouldDeleteCorrectRolls() = runTest {
        // Given - Insert rolls with different timestamps
        val oldTimestamp = LocalDateTime.now().minusDays(10)
        val recentTimestamp = LocalDateTime.now().minusHours(1)
        
        val oldRoll = createTestRollEntity(
            diceType = "d20",
            result = 15,
            timestamp = oldTimestamp
        )
        val recentRoll = createTestRollEntity(
            diceType = "d6",
            result = 4,
            timestamp = recentTimestamp
        )
        
        diceRollDao.insertRoll(oldRoll)
        diceRollDao.insertRoll(recentRoll)

        // When
        val cutoffDate = LocalDateTime.now().minusDays(5)
        val deletedCount = diceRollDao.deleteRollsOlderThan(cutoffDate)

        // Then
        assertEquals("Should delete 1 old roll", 1, deletedCount)
        
        val remainingCount = diceRollDao.getTotalRollCount()
        assertEquals("Should have 1 remaining roll", 1, remainingCount)
    }

    @Test
    fun clearAllRolls_shouldDeleteAllRolls() = runTest {
        // Given - Insert several rolls
        repeat(5) {
            val roll = createTestRollEntity(diceType = "d6", result = it + 1)
            diceRollDao.insertRoll(roll)
        }

        // When
        diceRollDao.clearAllRolls()

        // Then
        val remainingCount = diceRollDao.getTotalRollCount()
        assertEquals("Should have 0 remaining rolls", 0, remainingCount)
    }

    @Test
    fun getAllRollsPaged_shouldReturnPagingSource() = runTest {
        // Given - Insert test data
        repeat(25) { index ->
            val roll = createTestRollEntity(diceType = "d6", result = (index % 6) + 1)
            diceRollDao.insertRoll(roll)
        }

        // When
        val pagingSource = diceRollDao.getAllRollsPaged()
        val snapshot = pagingSource.asSnapshot()

        // Then
        assertEquals("Should return all 25 rolls", 25, snapshot.size)
        // Verify they're ordered by timestamp DESC
        for (i in 0 until snapshot.size - 1) {
            assertTrue("Rolls should be ordered by timestamp DESC",
                snapshot[i].timestamp.isAfter(snapshot[i + 1].timestamp) ||
                snapshot[i].timestamp.isEqual(snapshot[i + 1].timestamp))
        }
    }

    private fun createTestRollEntity(
        diceType: String,
        result: Int,
        modifier: Int = 0,
        timestamp: LocalDateTime = LocalDateTime.now(),
        sessionId: String? = null,
        context: String? = null,
        characterName: String? = null
    ): DiceRollEntity {
        return DiceRollEntity(
            diceType = diceType,
            result = result,
            modifier = modifier,
            totalResult = result + modifier,
            timestamp = timestamp,
            sessionId = sessionId,
            context = context,
            characterName = characterName
        )
    }
}
