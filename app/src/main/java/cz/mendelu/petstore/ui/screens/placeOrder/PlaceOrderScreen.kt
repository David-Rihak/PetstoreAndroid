package cz.mendelu.petstore.ui.screens.placeOrder

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.petstore.R
import cz.mendelu.petstore.navigation.INavigationRouter
import cz.mendelu.petstore.ui.elements.BaseScreen
import cz.mendelu.petstore.ui.elements.PlaceholderScreenContent
import cz.mendelu.petstore.ui.theme.basicMargin
import cz.mendelu.petstore.ui.theme.basicRadius
import cz.mendelu.petstore.ui.theme.buttonStyle
import cz.mendelu.petstore.ui.theme.cardColor
import cz.mendelu.petstore.ui.theme.fullCardElevation
import cz.mendelu.petstore.ui.theme.titleStyle

@Composable
fun PlaceOrderScreen(
    viewModel: PlaceOrderViewModel = hiltViewModel(),
    navRouter: INavigationRouter,
    id: Long?
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.orderPlaced && state.newOrderId != null) {
        Log.d("IMPORTANT", "changing screen to OrderDetail")
        navRouter.toOrderDetail(state.newOrderId)
    }

    BaseScreen(
        topBarText = stringResource(R.string.placeOrder_title),
        placeholderScreenContent = state.error?.let { PlaceholderScreenContent(title = stringResource(it)) },
        onBackClick = {
            navRouter.returnBack()
        }
    ) {
        PlaceOrderScreenContent(
            padVals = it,
            petId = id,
            numberValue = state.numberValue,
            numberError = state.numberError,
            onNumberValueChange = { viewModel.onNumberValueChange(it) },
            onPlace = { viewModel.placeOrder(id = id) }
        )
    }
}


@Composable
fun PlaceOrderScreenContent(
    padVals: PaddingValues,
    petId: Long?,
    numberValue: String,
    numberError: Int?,
    onNumberValueChange: (String)-> Unit,
    onPlace: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = padVals.calculateTopPadding(), bottom = padVals.calculateBottomPadding(), start = basicMargin(), end = basicMargin())
    ) {
        Card(
            shape = RoundedCornerShape(basicRadius()),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = fullCardElevation())
        ) {
            Column(
                modifier = Modifier.padding(basicMargin())
            ) {
                Text(
                    stringResource(R.string.placeOrderScreen_IdSupportingLabel), style = titleStyle
                )
                Text("$petId")
            }
        }
        Spacer(Modifier.height(basicMargin()))

        Card(
            shape = RoundedCornerShape(basicRadius()),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = fullCardElevation())
        ) {
            Column(
                modifier = Modifier.padding(basicMargin())
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(stringResource(R.string.placeOrder_howManyToOrder))
                    },
                    value = numberValue,
                    onValueChange = { onNumberValueChange(it)},
                    isError = numberError != null,
                    supportingText = {numberError?.let { Text(stringResource(numberError)) }}
                )
            }
        }


        Button(
            modifier = Modifier.fillMaxWidth(0.6f),
            shape = RoundedCornerShape(basicRadius()),
            onClick = onPlace,
            colors = buttonStyle
        ) {
            Text(stringResource(R.string.placeOrder_placeOrderLabel))
        }
    }

}
