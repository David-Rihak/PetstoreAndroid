package cz.mendelu.petstore.ui.screens.detail

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.petstore.R
import cz.mendelu.petstore.communication.CommunicationResult
import cz.mendelu.petstore.communication.IPetsRemoteRepository
import cz.mendelu.petstore.data.OrderStatus
import cz.mendelu.petstore.data.PetStatus
import cz.mendelu.petstore.dataStore.IDataStoreRepository
import cz.mendelu.petstore.models.Category
import cz.mendelu.petstore.models.Pet
import cz.mendelu.petstore.models.Tag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetDetailViewModel @Inject constructor(
    private val repository: IPetsRemoteRepository,
    private val dataStore: IDataStoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PetDetailScreenUIState())
    val uiState: StateFlow<PetDetailScreenUIState> = _uiState.asStateFlow()

    fun stopLoadingCreatingNew(){
        _uiState.update { it.copy(
            loading = false
        ) }
    }

    fun loadPetDetail(id: Long) {
        _uiState.update { it.copy(loading = true, error = null, id = id) }

        viewModelScope.launch {
            when(val result = repository.findPetById(id)){
                is CommunicationResult.Success ->
                    _uiState.update { it.copy(
                        loading = false,
                        name = result.data.name?:"",
                        imageUrls = result.data.photoUrls?: listOf(),
                        category = result.data.category?.name?:"",
                        status = result.data.status?:"",
                        tags = result.data.tags?:listOf()
                    ) }

                CommunicationResult.ConnectionError ->
                    _uiState.update { it.copy(loading = false, error = R.string.no_internet_connection) }
                is CommunicationResult.Error,
                is CommunicationResult.Exception ->
                    _uiState.update { it.copy(loading = false, error = R.string.failed_to_load_pet) }
            }
        }
    }

    fun onImageAdd(uri: Uri?) {
        uri?.let { newUri ->
            _uiState.update { state ->
                state.copy(
                    imageUrls = state.imageUrls + newUri.toString()
                )
            }
        }
    }

    fun onImageRemove(index: Int) {
        _uiState.update { state ->
            state.copy(
                imageUrls = state.imageUrls.filterIndexed { i, _ -> i != index }
            )
        }
    }

    fun checkValidToken() {
        viewModelScope.launch {
            dataStore.getLoginToken().collect { token ->
                if (token.isBlank()) {
                    _uiState.update { it.copy(
                        loggedIn = false
                    ) }
                }

            }
        }

    }

    fun removePet() {
        val id = _uiState.value.id

        viewModelScope.launch {
            if (id != null) {
                when (val response = repository.deletePet(id = id)) {
                    CommunicationResult.ConnectionError -> {
                        Log.e("PetAddEdit", "network error while removing pet")
                    }
                    is CommunicationResult.Error -> {
                        Log.e("PetAddEdit", "server error while removing pet: ${response.error.code}: ${response.error.message}")
                    }
                    is CommunicationResult.Exception ->  {
                        Log.e("PetAddEdit", "exeption while removing pet: ${response.exception.message}")
                    }
                    is CommunicationResult.Success -> {
                        Log.i("PetAddEdit", "removed pet: ${response.data}")
                        _uiState.update { it.copy(
                            addedOrUpdated = true
                        ) }
                    }
                }
            }
        }

    }

    fun addNewTag() {
        val value = uiState.value.tag
        if (!value.isBlank()) {
            _uiState.update { it.copy(
                tags = it.tags + Tag(id = (it.tags.sortedByDescending { tag -> tag.id }.firstOrNull()?.id?:0)+1, name = it.tag),
                tag = ""
            ) }
        } else{
            _uiState.update { it.copy(
                tagError = R.string.detail_tagCannotBeEmpty
            ) }
        }
    }

    fun removeTag(id: Long) {
        _uiState.update { it.copy(
            tags = it.tags.filter { tag -> tag.id != id }
        ) }
    }

    fun onTagValueChange(value: String) {
        _uiState.update { it.copy(
            tag = value,
            tagError = null
        ) }
    }

    fun onNameValueChange(newValue: String) {
        _uiState.update { it.copy(
            name = newValue,
            nameError = null
        ) }
    }

    fun onStatusChange(newStatus: String) {
        _uiState.update { it.copy(
            status = newStatus,
            statusError = null
        ) }
    }

    fun onCategoryChange(newValue: String) {
        _uiState.update { it.copy(
            category = newValue,
            categoryError = null
        ) }
    }

    fun setStatusDDMExpanded(to: Boolean) {
        _uiState.update { it.copy(
            statusDDExpanded = to
        ) }
    }

    fun onSave() {
        Log.i("PetAddEdit", "started onsave")
        var canSave = true
        if (uiState.value.name.isBlank()) {
            _uiState.update { it.copy(
                nameError = R.string.detail_nameError
            ) }
            canSave = false
            Log.i("PetAddEdit", "name is blank")
        }
        
        if (uiState.value.category.isBlank()) {
            _uiState.update { it.copy(
                categoryError = R.string.detail_categoryCannotBeEmpty
            ) }
            canSave= false
            Log.i("PetAddEdit", "Category cannot be empty")
        }


        if (uiState.value.status.isBlank()) {
            _uiState.update { it.copy(
                statusError = R.string.detail_statusError
            ) }
            canSave = false
            Log.i("PetAddEdit", "status is blank")
        }

        if (!canSave) return

        viewModelScope.launch {
            Log.i("PetAddEdit", "checking if id is null")
            val id = uiState.value.id
            if (id != null) {
                Log.i("PetAddEdit", "id is not null")
                when ( val response: CommunicationResult<Pet> = repository.findPetById(id = id) ) {
                    CommunicationResult.ConnectionError -> {
                        _uiState.update { it.copy(
                            error = R.string.detail_connectionIssue
                        ) }
                    }
                    is CommunicationResult.Error -> {
                        Log.d("PetAddEdit","comRes Error while finding by pet id: code ${response.error.code}: ${response.error.message}")
                    }
                    is CommunicationResult.Exception -> {
                        Log.d("PetAddEdit","comRes Exception while finding by pet id: ${response.exception.message}")
                    }
                    is CommunicationResult.Success -> {
                        Log.i("PetAddEdit", "updating pet")
                        val response = repository.updatePet(
                            pet = Pet(
                                id = id,
                                name = uiState.value.name,
                                status = uiState.value.status,
                                photoUrls = uiState.value.imageUrls,
                                category = Category(id = 0, name=uiState.value.category),
                                tags = uiState.value.tags
                            )
                        )
                        _uiState.update { it.copy(
                            addedOrUpdated = true
                        ) }
                        Log.i("PetAddEdit", "updating pet response: ${response}")

                    }
                }
            } else {
                Log.i("PetAddEdit", "creating a new pet")
                when (val response = repository.addPet(
                    pet = Pet(
                        id = System.currentTimeMillis(),
                        name = uiState.value.name,
                        status = uiState.value.status,
                        photoUrls = uiState.value.imageUrls,
                        category = Category(id=0,name=uiState.value.category),
                        tags = uiState.value.tags
                    )
                )) {
                    CommunicationResult.ConnectionError -> {
                        _uiState.update { it.copy(
                            error = R.string.detail_connectionIssue
                        ) }
                    }
                    is CommunicationResult.Error -> {
                        Log.d("PetAddEdit","comRes Error while finding by pet id: code ${response.error.code}: ${response.error.message}")
                    }
                    is CommunicationResult.Exception -> {
                        Log.d("PetAddEdit","comRes Exception while finding by pet id: ${response.exception.message}")
                    }
                    is CommunicationResult.Success -> {
                        Log.d("PetAddEdit","comRes Success. Response code: ${response.data.code} message: ${response.data.message}")
                        _uiState.update { it.copy(
                            addedOrUpdated = true
                        ) }
                    }
                }
            }

        }
    }

}
