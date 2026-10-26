package cz.mendelu.petstore.navigation

interface INavigationRouter {
    fun navigateToListOfPetsScreen()
    fun navigateToPetDetailScreen(id: Long?)
    fun returnBack()

    fun toProfile()
    fun toLogin()
    fun toPlaceOrder(id: Long?)
    fun toOrderDetail(id: Long?)
}
