package cz.mendelu.petstore.ui.screens.placeOrder

data class PlaceOrderUIState (
    val petIdToOrder: Long? = null,
    val numberValue: String = "",
    val numberError: Int? = null,
    val error: Int? = null,
    val newOrderId: Long? = null,
    val orderPlaced: Boolean = false
)