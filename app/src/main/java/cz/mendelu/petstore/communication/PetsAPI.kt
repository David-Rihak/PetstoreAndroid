package cz.mendelu.petstore.communication

import cz.mendelu.petstore.models.Order
import cz.mendelu.petstore.models.Pet
import cz.mendelu.petstore.models.PetStoreResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PetsAPI {

    @GET("pet/findByStatus")
    suspend fun findByStatus(@Query("status") status: String): Response<List<Pet>>

    @GET("pet/{petId}")
    suspend fun findPetById(@Path("petId") id: Long): Response<Pet>

    @GET("user/login")
    suspend fun getLoginToken(@Query("username") username: String, @Query("password") password: String) : Response<PetStoreResponse>


    @Multipart @POST("pet/{petId}/uploadImage")
    suspend fun uploadImage(@Path("petId") id: Long, @Part file: MultipartBody.Part, @Part("additionalMetadata") additionalMetadata: RequestBody? = null): Response<PetStoreResponse>

    /*
    val fileRequestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
    val filePart = MultipartBody.Part.createFormData( name = "file", filename = "pet_image.jpg", body = fileRequestBody )
     */

    @POST("pet")
    suspend fun addPet(@Body pet: Pet) : Response<PetStoreResponse>

    @PUT("pet")
    suspend fun updatePet(@Body pet: Pet) : Response<PetStoreResponse>

    @DELETE("pet/{petId}")
    suspend fun deletePet(@Path("petId") id: Long): Response<PetStoreResponse>

    // Orders

    @POST("store/order")
    suspend fun addOrder(@Body order: Order): Response<Order>

    @GET("store/order/{orderId}")
    suspend fun getOrder(@Path("orderId") orderId: Long): Response<Order>

    @DELETE("store/order/{orderId}")
    suspend fun deleteOrder(@Path("orderId") orderId: Long): Response<PetStoreResponse>

}