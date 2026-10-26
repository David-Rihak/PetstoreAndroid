package cz.mendelu.petstore.ui.screens.login

data class LoginState (
    val usernameValue: String = "",
    val passwordValue: String = "",
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val loginError: Int? = null,
    val loggedIn: Boolean = false
)