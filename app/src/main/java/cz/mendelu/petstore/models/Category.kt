package cz.mendelu.petstore.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Category(
    val id: Long? = null,
    val name: String? = null
)
