package cz.mendelu.petstore.ui.screens.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.petstore.R
import cz.mendelu.petstore.communication.CommunicationResult
import cz.mendelu.petstore.communication.IPetsRemoteRepository
import cz.mendelu.petstore.dataStore.IDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListOfPetsViewModel @Inject constructor(
    private val repository: IPetsRemoteRepository,
    private val dataStore: IDataStoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListOfPetsScreenUIState())
    val uiState: StateFlow<ListOfPetsScreenUIState> = _uiState.asStateFlow()

    fun onRefresh() {
        loadPets()
    }

    fun checkValidToken() {
        viewModelScope.launch {
            dataStore.getLoginToken().collect { token ->
                if (token.isBlank()) {
                    Log.i("Login", "ListOfPetsViewModel: token found: $token")
                    _uiState.update { it.copy(
                        loggedIn = false
                    ) }
                }
            }
        }

    }

    fun loadPets(withLoading: Boolean = true){

        _uiState.update { it.copy(loading = (withLoading), error = null) }

        viewModelScope.launch {
            when(val result = repository.findByStatus("available")){
                is CommunicationResult.Success ->
                    _uiState.update { it.copy(loading = false, pets = result.data) }

                CommunicationResult.ConnectionError ->
                    _uiState.update { it.copy(loading = false, error = R.string.no_internet_connection) }
                is CommunicationResult.Error,
                is CommunicationResult.Exception ->
                    _uiState.update { it.copy(loading = false, error = R.string.failed_to_load_pets) }
            }
        }
    }
}
