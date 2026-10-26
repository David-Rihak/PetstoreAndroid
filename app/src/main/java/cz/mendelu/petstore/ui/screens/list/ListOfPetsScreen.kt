package cz.mendelu.petstore.ui.screens.list

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.petstore.R
import cz.mendelu.petstore.models.Pet
import cz.mendelu.petstore.navigation.INavigationRouter
import cz.mendelu.petstore.ui.elements.BaseScreen
import cz.mendelu.petstore.ui.elements.PlaceholderScreenContent
import cz.mendelu.petstore.ui.elements.ProfileIcon


@Composable
fun ListOfPetsScreen(
    navRouter: INavigationRouter,
    viewModel: ListOfPetsViewModel = hiltViewModel(),
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.checkValidToken()
        viewModel.loadPets()
    }

    if(!state.loggedIn) {
        Log.i("Login", "ListOfPetsScreen: logging out")
        navRouter.toLogin()
    }

    BaseScreen(
        topBarText = stringResource(R.string.list_of_pets),
        showLoading = state.loading,
        placeholderScreenContent = if (state.error != null) PlaceholderScreenContent(
            title = stringResource(state.error!!),
            icon = state.errorIcon,
            onButtonClick = viewModel::onRefresh,
            buttonText = stringResource(R.string.retry)
        ) else {null},
        actions = {
            ProfileIcon(onClick = { navRouter.toProfile() })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navRouter.navigateToPetDetailScreen(null)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.list_AddPet))
            }
        }
    ) { paddingValues ->
        ListOfPetsScreenContent(
            paddingValues = paddingValues,
            onPetClick = navRouter::navigateToPetDetailScreen,
            refreshLoading = state.refreshLoading,
            onRefresh = viewModel::onRefresh,
            pets = state.pets
        )
    }
}

@Composable
fun ListOfPetsScreenContent(
    paddingValues: PaddingValues,
    pets: List<Pet>,
    refreshLoading: Boolean,
    onRefresh: () -> Unit,
    onPetClick: (Long) -> Unit,
) {

    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        modifier = Modifier
            .padding(paddingValues),
        isRefreshing = refreshLoading,
        onRefresh = onRefresh,
        state = pullToRefreshState,
        indicator = {
            PullToRefreshDefaults.Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullToRefreshState,
                isRefreshing = refreshLoading
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = pullToRefreshState.distanceFraction*120.dp.toPx()
                },
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(pets) {
                ListRow(
                    pet = it,
                    onClick = {onPetClick(it.id!!)}
                )

            }
        }
    }

}


