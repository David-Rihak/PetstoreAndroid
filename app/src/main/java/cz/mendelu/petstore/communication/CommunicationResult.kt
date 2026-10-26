package cz.mendelu.petstore.communication

sealed interface CommunicationResult<out T: Any> {
    data class Success<out T: Any>(val data: T) : CommunicationResult<T>
    data class Error(val error: CommunicationError) : CommunicationResult<Nothing>
    data object ConnectionError : CommunicationResult<Nothing>
    data class Exception(val exception: Throwable) : CommunicationResult<Nothing>

}