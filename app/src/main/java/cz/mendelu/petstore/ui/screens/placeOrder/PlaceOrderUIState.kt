package cz.mendelu.petstore.ui.screens.placeOrder

import androidx.compose.ui.graphics.vector.ImageVector

data class PlaceOrderUIState (
    val petIdToOrder: Long? = null,
    val numberValue: String = "",
    val numberError: Int? = null,
    val error: Int? = null,
    val errorIcon: ImageVector? = null,
    val newOrderId: Long? = null,
    val orderPlaced: Boolean = false
)