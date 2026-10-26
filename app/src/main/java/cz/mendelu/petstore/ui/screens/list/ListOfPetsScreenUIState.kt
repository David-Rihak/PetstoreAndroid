package cz.mendelu.petstore.ui.screens.list

import androidx.compose.ui.graphics.vector.ImageVector
import cz.mendelu.petstore.models.Pet


data class ListOfPetsScreenUIState(
    val loading: Boolean = true,
    val refreshLoading: Boolean = false,
    val pets: List<Pet> = emptyList(),
    val error: Int? = null,
    val errorIcon: ImageVector? = null,
    val loggedIn: Boolean = true
)
