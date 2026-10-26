package cz.mendelu.petstore.communication

data class CommunicationError(
    val code: Int,
    val message: String? = null
)
