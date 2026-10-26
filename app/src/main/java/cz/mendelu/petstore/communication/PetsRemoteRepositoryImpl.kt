package cz.mendelu.petstore.communication

import cz.mendelu.petstore.models.Order
import cz.mendelu.petstore.models.Pet
import cz.mendelu.petstore.models.PetStoreResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class PetsRemoteRepositoryImpl @Inject constructor(private val petsAPI: PetsAPI) : IPetsRemoteRepository
{
    override suspend fun findByStatus(status: String): CommunicationResult<List<Pet>> =
        processResponse { petsAPI.findByStatus(status) }

    override suspend fun findPetById(id: Long): CommunicationResult<Pet> =
        processResponse { petsAPI.findPetById(id) }

    override suspend fun getLoginToken(
        username: String,
        password: String
    ) = processResponse { petsAPI.getLoginToken(username = username, password = password) }

    override suspend fun uploadImage(id: Long, image: ByteArray): CommunicationResult<PetStoreResponse> =
        processResponse { petsAPI.uploadImage(
            id = id,
            file = MultipartBody.Part.createFormData(
                name = "file",
                filename = "petImage.jpg",
                body = image.toRequestBody("image/jpeg".toMediaTypeOrNull()
            ))
        ) }

    override suspend fun addPet(pet: Pet): CommunicationResult<PetStoreResponse> =
        processResponse { petsAPI.addPet(pet = pet) }

    override suspend fun updatePet(pet: Pet): CommunicationResult<PetStoreResponse> =
        processResponse { petsAPI.updatePet(pet = pet) }

    override suspend fun deletePet(id: Long): CommunicationResult<PetStoreResponse> =
        processResponse { petsAPI.deletePet(id = id) }

    override suspend fun placeOrder(order: Order): CommunicationResult<Order> =
        processResponse { petsAPI.addOrder(order = order) }


    override suspend fun viewOrder(orderId: Long): CommunicationResult<Order> =
        processResponse { petsAPI.getOrder(orderId = orderId) }


    override suspend fun deleteOrder(orderId: Long): CommunicationResult<PetStoreResponse> =
        processResponse { petsAPI.deleteOrder(orderId = orderId) }
}