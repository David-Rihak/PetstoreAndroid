package cz.mendelu.petstore.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {

    override fun navigateToListOfPetsScreen() {
        navController.navigate(Destination.ListOfPetsScreen)
    }

    override fun navigateToPetDetailScreen(id: Long?) {
        navController.navigate(Destination.PetDetailScreen(id))
    }

    override fun returnBack() {
        navController.popBackStack()
    }

    override fun toProfile() {
        navController.navigate(Destination.ProfileScreen)
    }
    override fun toLogin() {
        navController.navigate(Destination.LoginScreen)
    }

    override fun toPlaceOrder(id: Long?) {
        navController.navigate(Destination.PlaceOrderScreen(id = id))
    }

    override fun toOrderDetail(id: Long?) {
        navController.navigate(Destination.OrderDetailScreen(id = id))
    }


}
