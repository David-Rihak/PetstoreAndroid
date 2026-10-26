package cz.mendelu.petstore.ui.screens.orderDetail

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.WifiOff
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.petstore.R
import cz.mendelu.petstore.communication.CommunicationResult
import cz.mendelu.petstore.communication.IPetsRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel@Inject constructor(
    private val petsRepo: IPetsRemoteRepository
): ViewModel() {

    val _uiState = MutableStateFlow(OrderDetailUIState())

    val uiState = _uiState.asStateFlow()

    fun loadOrder(id: Long?) {

        if (id == null) {
            _uiState.update { it.copy(
                error = R.string.orderDetail_couldNotFIndOrder,
                errorIcon = Icons.Default.WarningAmber
            ) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(
                loading = true,
                error = null,
                errorIcon = null
            ) }
            when(val response = petsRepo.viewOrder(id)) {
                is CommunicationResult.ConnectionError -> {
                    _uiState.update { it.copy(
                        error = R.string.orderDetail_noInternetConnection,
                        errorIcon = Icons.Default.WifiOff
                    ) }
                }
                is CommunicationResult.Error -> {
                    _uiState.update { it.copy(
                        error = R.string.orderDetail_couldNotFIndOrder,
                        errorIcon = Icons.Default.Repeat
                    ) }
                    Log.e("orderDetailScreen", "server error: code:${response.error.code}, message: ${response.error.message}" )
                }
                is CommunicationResult.Exception -> {
                    _uiState.update { it.copy(
                        error = R.string.orderDetail_couldNotFIndOrder,
                        errorIcon = Icons.Default.Repeat
                    ) }
                    Log.e("orderDetailScreen", "server error: ${response.exception.message}" )
                }
                is CommunicationResult.Success -> {
                    _uiState.update { it.copy(
                        order = response.data,
                        loading = false
                    ) }
                    Log.i("orderDetailScreen", "laded order: ${response.data}")
                }
            }
        }

    }

    fun deleteOrder(id: Long?) {
        if (id == null) {
            _uiState.update { it.copy(
                error = R.string.orderDetail_couldNotDeleteOrder,
                errorIcon = Icons.Default.Repeat
            ) }
            return
        }
        
        viewModelScope.launch {
         when (val response = petsRepo.deleteOrder(id)) {
             is CommunicationResult.ConnectionError -> {
                 _uiState.update { it.copy(
                     error = R.string.orderDetail_noInternetConnection,
                     errorIcon = Icons.Default.WifiOff
                 ) }
             }
             is CommunicationResult.Error -> {
                 _uiState.update { it.copy(
                     error = R.string.orderDetail_couldNotDeleteOrder,
                     errorIcon = Icons.Default.Repeat
                 ) }
                 Log.e("orderDetailScreen", "server error: code:${response.error.code}, message: ${response.error.message}" )
             }
             is CommunicationResult.Exception -> {
                 _uiState.update { it.copy(
                     error = R.string.orderDetail_couldNotDeleteOrder,
                     errorIcon = Icons.Default.Repeat
                 ) }
                 Log.e("orderDetailScreen", "server error: ${response.exception.message}" )
             }
             is CommunicationResult.Success -> {
                 _uiState.update { it.copy(
                    orderDeleted = true
                 ) }
                 Log.i("orderDetailScreen", "laded order: ${response.data}")
             }
         }
        }
    }
}