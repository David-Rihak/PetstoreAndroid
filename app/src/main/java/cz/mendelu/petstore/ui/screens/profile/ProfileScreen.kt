package cz.mendelu.petstore.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.petstore.R
import cz.mendelu.petstore.navigation.INavigationRouter
import cz.mendelu.petstore.ui.elements.BaseScreen
import cz.mendelu.petstore.ui.theme.basicMargin
import cz.mendelu.petstore.ui.theme.basicRadius
import cz.mendelu.petstore.ui.theme.buttonStyle
import cz.mendelu.petstore.ui.theme.cardColor
import cz.mendelu.petstore.ui.theme.fullCardElevation
import cz.mendelu.petstore.ui.theme.titleStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navRouter: INavigationRouter,
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadToken()
        viewModel.checkValidToken()
    }

    if (!state.loggedIn) {
        navRouter.toLogin()
    }

    BaseScreen(
        topBarText = stringResource(R.string.profile_title),
        onBackClick = {
            navRouter.returnBack()
        }
    ) { padVals ->
        ProfileScreenContent(
            padVals = padVals,
            token = state.currentToken,
            onLogOut = {
                viewModel.logOut()
                navRouter.toLogin()
            }
        )
    }
}

@Composable
fun ProfileScreenContent(
    padVals: PaddingValues,
    token: String,
    onLogOut: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padVals).padding(basicMargin())
    ) {
        Card (
            shape = RoundedCornerShape(basicRadius()),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = fullCardElevation())
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                Text("Token", style = titleStyle)
                Text(token)
            }
        }

        Spacer(Modifier.height(15.dp))
        Button(
            onClick = {
                onLogOut()
            },
            colors = buttonStyle,
            shape = RoundedCornerShape(basicRadius())
        ) {
            Text(stringResource(R.string.profile_logout))
        }

    }
}