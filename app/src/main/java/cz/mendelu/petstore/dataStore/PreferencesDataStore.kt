package cz.mendelu.petstore.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val LOGIN_TOKEN = stringPreferencesKey("login_token")
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
