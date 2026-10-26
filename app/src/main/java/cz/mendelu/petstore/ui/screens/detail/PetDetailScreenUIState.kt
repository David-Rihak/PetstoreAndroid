package cz.mendelu.petstore.ui.screens.detail

import androidx.compose.ui.graphics.vector.ImageVector
import cz.mendelu.petstore.data.OrderStatus
import cz.mendelu.petstore.models.Tag


data class PetDetailScreenUIState(
    val loading: Boolean = true,
    val error: Int? = null,
    val id: Long? = null,
    val imageUrls: List<String> = listOf(),
    val name: String = "",
    val status: String = "",
    val category: String = "",
    val tags: List<Tag> = listOf(),
    val tag: String = "",
    val tagError: Int? = null,
    val nameError: Int? = null,
    val statusError: Int? = null,
    val errorIcon: ImageVector? = null,
    val categoryError: Int? = null,
    val loggedIn: Boolean = true,
    val petNonexistentPopupShown: Boolean = false,
    val addedOrUpdated: Boolean = false,

    //DropDownMenu
    val statusDDExpanded: Boolean = false

)
