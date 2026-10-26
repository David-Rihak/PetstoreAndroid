package cz.mendelu.petstore.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Order (
    val id: Long,
    val petId: Long,
    val quantity: Int,
    val shipDate: String,
    val status: String,
    val complete: Boolean
)