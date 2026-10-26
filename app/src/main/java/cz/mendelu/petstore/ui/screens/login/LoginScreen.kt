package cz.mendelu.petstore.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.petstore.R
import cz.mendelu.petstore.navigation.INavigationRouter
import cz.mendelu.petstore.ui.elements.BaseScreen
import cz.mendelu.petstore.ui.theme.basicMargin
import cz.mendelu.petstore.ui.theme.basicRadius
import cz.mendelu.petstore.ui.theme.fullCardElevation

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navRouter: INavigationRouter
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.checkValidToken()
    }

    if (state.loggedIn) {
        navRouter.navigateToListOfPetsScreen()
    }

    BaseScreen(
    ) {
        LoginScreenContent(
            onUsernameValueChange = { value ->
                viewModel.onUsernameChanged(value)
            },
            onPasswordValueChange = { value ->
                viewModel.onPasswordChanged(value)
            },
            usernameValue = state.usernameValue,
            passwordValue = state.passwordValue,

            usernameError = state.usernameError,
            passwordError = state.passwordError,
            loginError = state.loginError,
            loginConfirm = {
                viewModel.onLoginConfirm()
            }
        )
    }
}

@Composable
fun LoginScreenContent(
    onUsernameValueChange: (String) -> Unit,
    onPasswordValueChange: (String) -> Unit,
    usernameValue: String,
    passwordValue: String,
    usernameError: Int?,
    passwordError: Int?,
    loginError: Int?,
    loginConfirm: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.8f)
        ){
            Text(stringResource(R.string.login_title), style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp))
            if (loginError != null) {
                Text(stringResource(loginError), style = TextStyle(color = Color.Red))
            }
            Spacer(Modifier.height(50.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(basicRadius()),
                    elevation = CardDefaults.cardElevation(defaultElevation = fullCardElevation())
                ) {
                    Column(
                        modifier = Modifier.padding(basicMargin())
                    ) {
                        OutlinedTextField(
                            label = { Text(stringResource(R.string.login_username)) },
                            value = usernameValue,
                            isError = usernameError != null,
                            supportingText = {if (usernameError != null) Text(stringResource(usernameError))},
                            onValueChange = { onUsernameValueChange(it) }
                        )
                        Spacer(Modifier.height(basicMargin()))
                        OutlinedTextField(
                            label = { Text(stringResource(R.string.login_password)) },
                            value = passwordValue,
                            isError = passwordError != null,
                            supportingText = { if(passwordError != null) Text(stringResource(R.string.login_passwordError)) },
                            onValueChange = { onPasswordValueChange(it) }
                        )
                    }
                }

            }
            Spacer(Modifier.height(basicMargin()))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        loginConfirm()
                    },
                    shape = RoundedCornerShape(basicRadius())
                ) {
                    Text(stringResource(R.string.login_button))
                }
            }
        }
    }

}

