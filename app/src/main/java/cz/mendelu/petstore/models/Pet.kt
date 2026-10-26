package cz.mendelu.petstore.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Pet(
    val id: Long? = null,
    val name: String? = null,
    val status: String? = null,
    val photoUrls: List<String>? = null,
    val category: Category? = null,
    val tags: List<Tag>? = null
)

