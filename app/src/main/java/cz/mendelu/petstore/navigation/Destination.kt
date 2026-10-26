package cz.mendelu.petstore.navigation

import kotlinx.serialization.Serializable
import java.io.Serial


sealed interface Destination {

    @Serializable
    data object LoginScreen : Destination

    @Serializable
    data object ListOfPetsScreen : Destination

    @Serializable
    data class PetDetailScreen(val id: Long?) : Destination
    @Serializable
    data object ProfileScreen: Destination

    @Serializable
    data class PlaceOrderScreen(val id: Long?): Destination

    @Serializable
    data class OrderDetailScreen(val id: Long?): Destination
}
