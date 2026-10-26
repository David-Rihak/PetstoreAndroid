package cz.mendelu.petstore.ui.screens.list

import cz.mendelu.petstore.models.Pet


data class ListOfPetsScreenUIState(
    val loading: Boolean = true,
    val refreshLoading: Boolean = false,
    val pets: List<Pet> = emptyList(),
    val error: Int? = null,
    val loggedIn: Boolean = true
)
