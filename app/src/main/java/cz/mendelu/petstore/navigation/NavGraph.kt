package cz.mendelu.petstore.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import cz.mendelu.petstore.ui.screens.detail.PetDetailScreen
import cz.mendelu.petstore.ui.screens.list.ListOfPetsScreen
import cz.mendelu.petstore.ui.screens.login.LoginScreen
import cz.mendelu.petstore.ui.screens.orderDetail.OrderDetailScreen
import cz.mendelu.petstore.ui.screens.placeOrder.PlaceOrderScreen
import cz.mendelu.petstore.ui.screens.profile.ProfileScreen


@Composable
fun NavGraph(
    startDestination: Destination,
    navController: NavHostController = rememberNavController(),
    navigation: INavigationRouter = remember(navController) {
        NavigationRouterImpl(navController)
    },
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        composable<Destination.ListOfPetsScreen> {
            ListOfPetsScreen(navRouter = navigation)
        }

        composable<Destination.PetDetailScreen> { backStackEntry ->
            val destination: Destination.PetDetailScreen = backStackEntry.toRoute()
            PetDetailScreen(navRouter = navigation,id = destination.id)
        }

        composable<Destination.LoginScreen>{
            LoginScreen(
                navRouter = navigation
            )
        }

        composable<Destination.ProfileScreen>{
            ProfileScreen(
                navRouter = navigation
            )
        }

        composable <Destination.PlaceOrderScreen> { bse ->
            val route: Destination.PlaceOrderScreen = bse.toRoute()
            PlaceOrderScreen(
                navRouter = navigation,
                id = route.id
            )
        }

        composable<Destination.OrderDetailScreen> { bse ->
            val route: Destination.OrderDetailScreen = bse.toRoute()
            OrderDetailScreen(
                navRouter = navigation,
                id = route.id
            )
        }
    }
}
