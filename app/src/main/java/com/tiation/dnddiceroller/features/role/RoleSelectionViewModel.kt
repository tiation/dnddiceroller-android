package com.tiation.dnddiceroller.features.role

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing user role selection and persistence.
 * Handles saving and retrieving the user's chosen role (Player or Dungeon Master) using DataStore.
 * 
 * Built by Garrett Dillman (garrett@sxc.codes) & Tia (tiatheone@protonmail.com)
 */
@HiltViewModel
class RoleSelectionViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    
    companion object {
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
    }
    
    /**
     * Flow that emits the current user role, or null if no role has been selected
     */
    val userRole: Flow<UserRole?> = dataStore.data.map { preferences ->
        preferences[USER_ROLE_KEY]?.let { roleString ->
            try {
                UserRole.valueOf(roleString)
            } catch (e: IllegalArgumentException) {
                null // Return null if stored value is invalid
            }
        }
    }
    
    /**
     * Saves the selected user role to DataStore
     * 
     * @param role The role to save (Player or Dungeon Master)
     */
    fun saveRole(role: UserRole) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[USER_ROLE_KEY] = role.name
            }
        }
    }
    
    /**
     * Clears the saved user role from DataStore
     * Useful for testing or allowing users to reselect their role
     */
    fun clearRole() {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences.remove(USER_ROLE_KEY)
            }
        }
    }
}
