package cz.mendelu.petstore.dataStore

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl(
    private val context: Context
): IDataStoreRepository {
    override fun getLoginToken(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[LOGIN_TOKEN] ?: ""
        }
    }

    override suspend fun saveLoginToken(token: String) {
        context.dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[LOGIN_TOKEN] = token
            }
        }
    }

    override suspend fun clearLoginToken() {
        context.dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[LOGIN_TOKEN] = ""
            }
        }
    }
}