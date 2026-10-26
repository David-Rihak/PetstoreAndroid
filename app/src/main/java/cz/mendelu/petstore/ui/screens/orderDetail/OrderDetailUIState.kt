package cz.mendelu.petstore.ui.screens.orderDetail

import androidx.compose.ui.graphics.vector.ImageVector
import cz.mendelu.petstore.models.Order

data class OrderDetailUIState (
    val error: Int? = null,
    val errorIcon: ImageVector? = null,
    val loading: Boolean = true,
    val order: Order? = null,
    val orderDeleted: Boolean = false
)