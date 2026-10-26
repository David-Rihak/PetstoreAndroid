package cz.mendelu.petstore.ui.screens.placeOrder

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.petstore.R
import cz.mendelu.petstore.communication.CommunicationResult
import cz.mendelu.petstore.communication.IPetsRemoteRepository
import cz.mendelu.petstore.data.OrderStatus
import cz.mendelu.petstore.data.PetStatus
import cz.mendelu.petstore.models.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@HiltViewModel
class PlaceOrderViewModel@Inject constructor(
    private val petsRepo: IPetsRemoteRepository
): ViewModel() {

    val _uiState = MutableStateFlow(PlaceOrderUIState())

    val uiState = _uiState.asStateFlow()

    fun onNumberValueChange(newVal: String) {
        _uiState.update { it.copy(
            numberValue = newVal
        ) }
    }

    fun placeOrder(id: Long?) {
        val value = uiState.value.numberValue.toIntOrNull()
        if (value == null) {
            _uiState.update { it.copy(
                numberError = R.string.placeOrder_numberError
            ) }
            return
        }

        if (value < 1) {
            _uiState.update { it.copy(
                numberError = R.string.placeOrder_numberError
            ) }
            return
        }

        viewModelScope.launch {
            if (id == null) {
                _uiState.update {
                    it.copy(
                        error = R.string.placeOrder_couldNotFIndAnAnimalToOrder
                    )
                }
                Log.e("placeOrderScreen", "Id was found null while placing order" )
                return@launch
            } else {
                val newOrderId = System.currentTimeMillis()
                val order: Order = Order(
                    id = newOrderId,
                    petId = id,
                    quantity = value,
                    shipDate = LocalDateTime.now().toString(),
                    status = OrderStatus.placed.toString(),
                    complete = false
                )
                
                when (val response = petsRepo.placeOrder(order = order)) {
                    CommunicationResult.ConnectionError -> {
                        _uiState.update { it.copy(
                            error = R.string.placeOrder_noInternetConnection
                        ) }
                    }
                    is CommunicationResult.Error -> {
                        _uiState.update { it.copy(
                            error = R.string.placeOrder_couldNotPlaceOrder
                        ) }
                        Log.e("placeOrderScreen", "server error: ${response.error.code} - ${response.error.message}" )
                    }
                    is CommunicationResult.Exception -> {
                        _uiState.update { it.copy(
                            error = R.string.placeOrder_couldNotPlaceOrder
                        ) }
                        Log.e("placeOrderScreen", "server error: ${response.exception.message}" )
                    }
                    is CommunicationResult.Success -> {
                        _uiState.update { it.copy(
                            newOrderId = newOrderId,
                            orderPlaced = true
                        ) }
                        Log.i("placeOrderScreen", "successfully placed order: ${response.data}")
                    }
                }
            }
            
        }
    }

}