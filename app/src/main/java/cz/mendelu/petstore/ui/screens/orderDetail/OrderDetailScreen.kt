package cz.mendelu.petstore.ui.screens.orderDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.petstore.R
import cz.mendelu.petstore.models.Order
import cz.mendelu.petstore.navigation.INavigationRouter
import cz.mendelu.petstore.ui.elements.BaseScreen
import cz.mendelu.petstore.ui.elements.PlaceholderScreenContent
import cz.mendelu.petstore.ui.theme.basicMargin
import cz.mendelu.petstore.ui.theme.basicRadius
import cz.mendelu.petstore.ui.theme.fullCardElevation
import cz.mendelu.petstore.ui.theme.quarterMargin
import java.util.Locale

@Composable
fun OrderDetailScreen(
    viewModel: OrderDetailViewModel = hiltViewModel(),
    navRouter: INavigationRouter,
    id: Long?
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.loadOrder(id)
    }

    if (state.orderDeleted) {
        navRouter.navigateToListOfPetsScreen()
    }

    BaseScreen(
        topBarText = stringResource(R.string.orderDetail_title, id.toString()),
        showLoading = state.loading,
        placeholderScreenContent = state.error?.let {
            PlaceholderScreenContent(
                title = stringResource(it),
                icon = state.errorIcon,
                onButtonClick = if (id != null) { { viewModel.loadOrder(id = id) } } else null,
                buttonText = stringResource(R.string.retry)

            )
        },
        onBackClick = navRouter::navigateToListOfPetsScreen
    ) {
        OrderDetailScreenContent(
            padVals = it,
            order = state.order,
            onDelete = {
                viewModel.deleteOrder(id)
            }
        )
    }
}

@Composable
fun OrderDetailScreenContent(
    padVals: PaddingValues,
    order: Order?,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padVals)
            .padding(basicMargin()),
        verticalArrangement = Arrangement.spacedBy(basicMargin())
    ) {
        order?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(basicRadius()),
                elevation = CardDefaults.cardElevation(defaultElevation = fullCardElevation())
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(basicMargin()),
                    verticalArrangement = Arrangement.spacedBy(basicMargin())
                ) {
                    Text(
                        text = stringResource(R.string.orderDetail_title),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(quarterMargin()))

                    OrderDetailRow(label = stringResource(R.string.label_quantity), value = it.quantity.toString())
                    OrderDetailRow(label = stringResource(R.string.label_shipDate), value = it.shipDate.format("dd.MM.YYYY"))
                    OrderDetailRow(label = stringResource(R.string.label_status), value = it.status)
                    OrderDetailRow(label = stringResource(R.string.label_completed), value = if (it.complete) stringResource(R.string.yes) else stringResource(R.string.no)
                    )
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(basicRadius()),
            onClick = onDelete
        ) {
            Text(stringResource(R.string.orderDetail_removeOrder))
        }
    }
}

@Composable
fun OrderDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}