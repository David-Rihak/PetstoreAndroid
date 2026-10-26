package cz.mendelu.petstore.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.petstore.R
import cz.mendelu.petstore.communication.CommunicationResult
import cz.mendelu.petstore.communication.IBaseRemoteRepository
import cz.mendelu.petstore.communication.IPetsRemoteRepository
import cz.mendelu.petstore.dataStore.IDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val petsRepository: IPetsRemoteRepository,
    private val dataStore: IDataStoreRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(LoginState())

    val uiState = _uiState.asStateFlow()


    fun onUsernameChanged(newVal: String) {
        _uiState.update { it.copy(
            usernameValue = newVal
        ) }
    }

    fun onPasswordChanged(newVal: String) {
        _uiState.update { it.copy(
            passwordValue = newVal
        ) }
    }

    fun checkValidToken() {
        viewModelScope.launch {
            dataStore.getLoginToken().collect { token ->
                if (!token.isBlank()) {
                    _uiState.update { it.copy(
                        loggedIn = true
                    ) }
                }

            }
        }

    }

    fun onLoginConfirm() {
        if (uiState.value.usernameValue.isEmpty()) {
            _uiState.update { it.copy(
                usernameError = R.string.login_usernameError,
                passwordError = null
            ) }
            return
        } else if (uiState.value.passwordValue.isEmpty()) {
            _uiState.update { it.copy(
                usernameError = null,
                passwordError = R.string.login_passwordError
            ) }
            return
        } else {
            _uiState.update { it.copy(
                usernameError = null,
                passwordError = null
            ) }
        }
        viewModelScope.launch {
            when (val response = petsRepository.getLoginToken(username = uiState.value.usernameValue, password = uiState.value.passwordValue)) {
                CommunicationResult.ConnectionError -> {
                    _uiState.update { it.copy(
                        loginError = R.string.login_noNetworkError
                    ) }
                    return@launch
                }
                is CommunicationResult.Error -> {
                    _uiState.update { it.copy(
                        loginError = R.string.login_generalError
                    ) }
                    return@launch
                }
                is CommunicationResult.Exception -> {
                    _uiState.update { it.copy(
                        loginError = R.string.login_generalError
                    ) }
                    return@launch
                }
                is CommunicationResult.Success -> {
                    val pattern = "[0123456789]+$".toRegex()
                    val response = response.data.message

                    if (response == null) {
                        Log.d("info", "onLoginConfirm:  response was successful yet message was found to be null")
                        _uiState.update { it.copy(
                            loginError = R.string.login_generalError
                        ) }
                        return@launch
                    }

                    var parsedData: String? = null

                    for (matchResult in pattern.findAll(response)) {
                        parsedData = matchResult.value
                    }

                    if (parsedData != null) {
                        Log.d("info", "onLoginConfirm:  found token: $parsedData")
                        dataStore.saveLoginToken(token = parsedData)
                        _uiState.update { it.copy(
                            loggedIn = true
                        ) }
                    } else {
                        Log.d("info", "onLoginConfirm:  token was not able to be regexed")
                    }
                }
            }
        }

    }

}