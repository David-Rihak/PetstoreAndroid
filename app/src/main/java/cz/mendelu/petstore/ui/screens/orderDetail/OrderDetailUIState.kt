package cz.mendelu.petstore.ui.screens.orderDetail

import cz.mendelu.petstore.models.Order

data class OrderDetailUIState (
    val error: Int? = null,
    val loading: Boolean = true,
    val order: Order? = null,
    val orderDeleted: Boolean = false
)