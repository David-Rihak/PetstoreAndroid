package cz.mendelu.petstore.dataStore

import kotlinx.coroutines.flow.Flow

interface IDataStoreRepository {
    fun getLoginToken(): Flow<String>
    suspend fun saveLoginToken(token: String)
    suspend fun clearLoginToken()
}