package cz.mendelu.petstore.communication

import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface IBaseRemoteRepository {

    suspend fun <T: Any> processResponse(
        request: suspend () -> Response<T>
    ): CommunicationResult<T> = try {
        val response = request()
        val body = response.body()
        when {
            response.isSuccessful && body != null -> CommunicationResult.Success(body)

            else -> CommunicationResult.Error(
                CommunicationError(
                    response.code(),
                    response.errorBody()?.toString()
                )
            )
        }

    } catch (ex: UnknownHostException) {
        CommunicationResult.ConnectionError
    }  catch (ex: SocketTimeoutException) {
        CommunicationResult.ConnectionError
    } catch (ex: IOException) {
        CommunicationResult.ConnectionError
    } catch (ex: Exception){
        CommunicationResult.Exception(ex)
    }
}