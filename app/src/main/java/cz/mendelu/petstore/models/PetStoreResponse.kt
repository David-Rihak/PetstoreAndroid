package cz.mendelu.petstore.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PetStoreResponse (
    val code: Int? = null,
    val type: String? = null,
    val message: String? = null
)