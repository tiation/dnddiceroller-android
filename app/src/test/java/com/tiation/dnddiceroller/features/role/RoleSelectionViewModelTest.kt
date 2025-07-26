package com.tiation.dnddiceroller.features.role

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * Test class for RoleSelectionViewModel
 * 
 * Built by Garrett Dillman (garrett@sxc.codes) & Tia (tiatheone@protonmail.com)
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RoleSelectionViewModelTest {

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var viewModel: RoleSelectionViewModel

    @Before
    fun setup() {
        dataStore = PreferenceDataStoreFactory.create {
            tmpFolder.newFile("test_datastore.preferences_pb")
        }
        viewModel = RoleSelectionViewModel(dataStore)
    }

    @Test
    fun `initially no role is selected`() = runTest {
        val role = viewModel.userRole.first()
        assertNull(role)
    }

    @Test
    fun `saving player role works correctly`() = runTest {
        viewModel.saveRole(UserRole.PLAYER)
        val role = viewModel.userRole.first()
        assertEquals(UserRole.PLAYER, role)
    }

    @Test
    fun `saving dungeon master role works correctly`() = runTest {
        viewModel.saveRole(UserRole.DUNGEON_MASTER)
        val role = viewModel.userRole.first()
        assertEquals(UserRole.DUNGEON_MASTER, role)
    }

    @Test
    fun `clearing role works correctly`() = runTest {
        // First save a role
        viewModel.saveRole(UserRole.PLAYER)
        assertEquals(UserRole.PLAYER, viewModel.userRole.first())
        
        // Then clear it
        viewModel.clearRole()
        val role = viewModel.userRole.first()
        assertNull(role)
    }

    @Test
    fun `role persists between viewmodel instances`() = runTest {
        // Save role with first viewmodel instance
        viewModel.saveRole(UserRole.DUNGEON_MASTER)
        
        // Create new viewmodel instance with same datastore
        val newViewModel = RoleSelectionViewModel(dataStore)
        val role = newViewModel.userRole.first()
        assertEquals(UserRole.DUNGEON_MASTER, role)
    }
}
