package cz.mendelu.petstore.ui.screens.detail

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.petstore.R
import cz.mendelu.petstore.data.PetStatus
import cz.mendelu.petstore.models.Tag
import cz.mendelu.petstore.navigation.INavigationRouter
import cz.mendelu.petstore.ui.elements.BaseScreen
import cz.mendelu.petstore.ui.elements.PlaceholderScreenContent
import cz.mendelu.petstore.ui.elements.ProfileIcon
import cz.mendelu.petstore.ui.theme.basicMargin
import cz.mendelu.petstore.ui.theme.basicRadius
import cz.mendelu.petstore.ui.theme.fullCardElevation
import cz.mendelu.petstore.ui.theme.halfMargin
import cz.mendelu.petstore.ui.theme.halfRadius
import cz.mendelu.petstore.ui.theme.quarterMargin

const val TestTagPetDetailName = "TestTagPetDetailName"

@Composable
fun PetDetailScreen(
    navRouter: INavigationRouter,
    id: Long?,
    viewModel: PetDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        focusManager.clearFocus()
        uri?.let { viewModel.onImageAdd(it) }
    }

    LaunchedEffect(id) {
        viewModel.loadPetDetail(id)
    }
    if(!state.loggedIn) {
        navRouter.toLogin()
    }

    if (state.addedOrUpdated) {
        navRouter.navigateToListOfPetsScreen()
    }


    BaseScreen(
        topBarText = stringResource(R.string.detail),
        onBackClick = navRouter::returnBack,
        showLoading = state.loading,
        placeholderScreenContent = if (state.error != null) PlaceholderScreenContent(
            title = stringResource(state.error!!),
            icon = state.errorIcon,
            onButtonClick = { viewModel.loadPetDetail(id = id) },
            buttonText = stringResource(R.string.retry)
        ) else {null},
        actions = {
            if (id != null) {
                IconButton(
                    onClick = {
                        navRouter.toPlaceOrder(id)
                    }
                ) {
                    Icon(Icons.Default.ShoppingBasket, contentDescription = stringResource(R.string.detail_orderPet))
                }
            }

            ProfileIcon(onClick = {
                navRouter.toProfile()
            })
        }
    ) { padVals ->
        PetDetailScreenContent(
            padVals = padVals,
            imageUrls = state.imageUrls,
            onRemoveImageClick = { index ->
                viewModel.onImageRemove(index)
            },
            onAddImageClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            name = state.name,
            status = state.status,
            category = state.category,
            onCategoryValueChange = { newValue ->
                viewModel.onCategoryChange(newValue)
            },
            tags = state.tags,
            tag = state.tag,
            onTagRemove = { id ->
                viewModel.removeTag(id)
            },
            onTagSave = {
                focusManager.clearFocus()
                viewModel.addNewTag()
            },
            onTagValueChange = { value ->

                viewModel.onTagValueChange(value)
            },
            tagError = state.tagError,
            nameError = state.nameError,
            statusError = state.statusError,
            categoryError = state.categoryError,
            onNameValueChange = { newValue ->
                viewModel.onNameValueChange(newValue = newValue)
            },
            onStatusValueChange = { newValue ->
                viewModel.onStatusChange(newStatus = newValue)
            },
            statusDDMExpanded = state.statusDDExpanded,
            setStatusDDMExpanded = { setTo ->
                viewModel.setStatusDDMExpanded(setTo)
            },
            onSave ={
                viewModel.onSave()
            },
            saveButtonText = if (id != null) stringResource(R.string.detail_updatePet) else stringResource(R.string.detail_savePet),
            onRemovePetClick = if (id != null) {{viewModel.removePet()}} else null

        )

    }
}

@Composable
fun PetDetailScreenContent(
    padVals: PaddingValues,
    imageUrls: List<String>,
    onRemoveImageClick: (Int) -> Unit,
    onAddImageClick: () -> Unit,
    name: String,
    status: String,
    category: String,
    onCategoryValueChange: (String) -> Unit,
    tags: List<Tag>,
    tag: String,
    onTagRemove: (Long) -> Unit,
    onTagSave: () -> Unit,
    onTagValueChange: (String) -> Unit,
    tagError: Int?,
    nameError: Int?,
    statusError: Int?,
    categoryError: Int?,
    onNameValueChange: (String) -> Unit,
    onStatusValueChange: (String) -> Unit,
    statusDDMExpanded: Boolean,
    setStatusDDMExpanded: (Boolean) -> Unit,
    onSave: () -> Unit,
    saveButtonText: String,
    onRemovePetClick: (() -> Unit)?

) {

    Column (
        modifier = Modifier
            .verticalScroll(state = rememberScrollState(0))
            .fillMaxSize()
            .padding(padVals)
            .padding(horizontal = basicMargin(), vertical = basicMargin()),
        verticalArrangement = Arrangement.spacedBy(basicMargin())
    ) {
        Card(
            shape = RoundedCornerShape(halfMargin()),
            elevation = CardDefaults.cardElevation(defaultElevation = fullCardElevation())
        ) {
            Column(
                modifier = Modifier.padding(basicMargin()),
                verticalArrangement = Arrangement.spacedBy(halfMargin())
            ) {
                Text(stringResource(R.string.title_about), style = MaterialTheme.typography.titleMedium )
                Spacer(modifier = Modifier.height(basicMargin()))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    onValueChange = { onNameValueChange(it) },
                    label = { Text(text = stringResource(R.string.name_label)) },
                    isError = nameError != null,
                    supportingText = {
                        if (nameError != null) Text(stringResource(R.string.detail_nameError))
                    },
                    singleLine = true,
                )

                Spacer(modifier = Modifier.height(basicMargin()))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = category,
                    onValueChange = {onCategoryValueChange(it)},
                    label={Text(stringResource(R.string.detail_categoryLabel))},
                    isError = categoryError!=null,
                    supportingText = {if (categoryError != null) Text(stringResource(categoryError))}
                )
            }

        }

        Card(
            shape = RoundedCornerShape(halfMargin()),
            elevation = CardDefaults.cardElevation(defaultElevation = fullCardElevation())
        ) {
            Column(
                Modifier
                    .padding(basicMargin())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(halfMargin())
            ) {
                Text(stringResource(R.string.label_status), style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(basicMargin()))
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(halfRadius()))
                            .clickable(onClick = { setStatusDDMExpanded(!statusDDMExpanded) })
                            .border(
                                width = 1.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(halfRadius())
                            )
                            .padding(basicMargin())
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (status.isBlank()) stringResource(R.string.detail_selectStatusLabel) else status, style = TextStyle(color = if (statusError != null) Color.Red else Color.Black))
                        Icon(Icons.Default.ArrowDropDown, contentDescription = stringResource(R.string.detail_categoryDDLabel))
                    }
                    DropdownMenu(
                        modifier = Modifier
                            .padding(halfMargin())
                            .fillMaxWidth(0.5f),
                        expanded = statusDDMExpanded,
                        onDismissRequest = {
                            setStatusDDMExpanded(false)
                        }
                    ) {
                        Text(if (status.isBlank()) stringResource(R.string.none) else status)
                        PetStatus.entries.forEach { petStatus ->
                            DropdownMenuItem(
                                modifier = Modifier.fillMaxWidth(),
                                text = { Text(petStatus.name) },
                                onClick = {
                                    onStatusValueChange(petStatus.name)
                                    setStatusDDMExpanded(false)
                                }
                            )
                        }
                    }
                }
            }
        }



        Spacer(modifier = Modifier.height(halfMargin()))

        Card(
            shape = RoundedCornerShape(halfMargin()),
            elevation = CardDefaults.cardElevation(defaultElevation = fullCardElevation())
        ) {
            Column(
                modifier = Modifier
                    .padding(basicMargin())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(halfMargin())
            ) {
                Text(stringResource(R.string.detail_tagsLabel), style = MaterialTheme.typography.titleMedium)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        modifier = Modifier.weight(6f),
                        label = { Text(stringResource(R.string.tag_label)) },
                        value = tag,
                        onValueChange = {
                            onTagValueChange(it)
                        },
                        isError = tagError != null,
                        supportingText = {
                            tagError?.let { Text(stringResource(it)) }
                        }
                    )
                    Spacer(Modifier.width(basicMargin()))
                    Button(
                        onClick = onTagSave,
                        modifier = Modifier
                            .padding(bottom = quarterMargin())
                            .weight(3f),
                        shape = RoundedCornerShape(basicRadius())
                    ) {
                        Text(stringResource(R.string.detail_addTag))
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(halfMargin()),
                    horizontalAlignment = Alignment.Start,
                ) {
                    tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100))
                                .background(color = MaterialTheme.colorScheme.secondary)
                                .clickable(onClick = { if (tag.id != null) onTagRemove(tag.id) })
                                .padding(horizontal = basicMargin(), vertical = halfMargin()),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(halfMargin()),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(tag.name?:stringResource(R.string.none), color = Color.White)
                                Icon(Icons.Default.Close, contentDescription = stringResource(R.string.detail_removeTag), tint = Color.White)
                            }
                        }
                    }
                }
            }

        }

        ImageCarousel(
            imageUrls = imageUrls,
            onRemoveImageClick = { index ->
                onRemoveImageClick(index)
            },
            onAddImageClick = {
                onAddImageClick()
            }
        )

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            if (onRemovePetClick != null) {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error, contentColor = MaterialTheme.colorScheme.onError),
                    onClick = onRemovePetClick,
                    shape = RoundedCornerShape(basicRadius())
                ) {
                    Text(stringResource(R.string.detail_removePetLabel))
                }
            }
            Button(
                onClick = onSave,
                shape = RoundedCornerShape(basicRadius())
            ) {
                Text(saveButtonText)
            }

        }
    }
}
