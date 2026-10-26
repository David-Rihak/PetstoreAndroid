package cz.mendelu.petstore.communication

import cz.mendelu.petstore.models.Order
import cz.mendelu.petstore.models.Pet
import cz.mendelu.petstore.models.PetStoreResponse

interface IPetsRemoteRepository : IBaseRemoteRepository {

    suspend fun findByStatus(status: String): CommunicationResult<List<Pet>>

    suspend fun findPetById(id: Long): CommunicationResult<Pet>
    suspend fun getLoginToken(username: String, password: String) : CommunicationResult<PetStoreResponse>

    suspend fun uploadImage(id: Long, image: ByteArray): CommunicationResult<PetStoreResponse>

    suspend fun addPet(pet: Pet): CommunicationResult<PetStoreResponse>

    suspend fun updatePet(pet: Pet): CommunicationResult<PetStoreResponse>

    suspend fun deletePet(id: Long): CommunicationResult<PetStoreResponse>

    suspend fun placeOrder(order: Order): CommunicationResult<Order>

    suspend fun viewOrder(orderId: Long): CommunicationResult<Order>

    suspend fun deleteOrder(orderId: Long): CommunicationResult<PetStoreResponse>
}