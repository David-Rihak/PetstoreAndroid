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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.petstore.R
import cz.mendelu.petstore.navigation.INavigationRouter
import cz.mendelu.petstore.ui.elements.BaseScreen
import cz.mendelu.petstore.ui.theme.basicMargin
import cz.mendelu.petstore.ui.theme.basicRadius
import cz.mendelu.petstore.ui.theme.fullCardElevation
import cz.mendelu.petstore.ui.theme.halfMargin

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
        modifier = Modifier.fillMaxSize().padding(padVals).padding(basicMargin()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card (
            shape = RoundedCornerShape(basicRadius()),
            elevation = CardDefaults.cardElevation(defaultElevation = fullCardElevation())
        ) {
            Column(
                modifier = Modifier.padding(basicMargin()),
                verticalArrangement = Arrangement.spacedBy(halfMargin())
            ) {
                Text("Token", style = MaterialTheme.typography.titleMedium)
                Text(token)
            }
        }

        Spacer(Modifier.height(15.dp))
        Button(
            onClick = {
                onLogOut()
            },
            shape = RoundedCornerShape(basicRadius())
        ) {
            Text(stringResource(R.string.profile_logout))
        }

    }
}