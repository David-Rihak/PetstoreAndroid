package cz.mendelu.petstore.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.petstore.dataStore.IDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel@Inject constructor(
    private val dataStore: IDataStoreRepository
): ViewModel() {
    val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    fun loadToken() {
        viewModelScope.launch {
            dataStore.getLoginToken().collect { token ->
                _uiState.update { it.copy(
                    currentToken = token,
                    loggedIn = !token.isBlank()
                ) }
            }
        }
    }

    fun checkValidToken() {
        viewModelScope.launch {
            dataStore.getLoginToken().collect { token ->
                if (token.isBlank()) {
                    _uiState.update { it.copy(
                        loggedIn = false
                    ) }
                }

            }
        }

    }

    fun logOut() {
        viewModelScope.launch {
            dataStore.clearLoginToken()
            _uiState.update { it.copy(
                loggedIn = false
            ) }
        }
    }

}