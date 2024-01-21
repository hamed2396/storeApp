package com.example.storeapp.data.stored

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.storeapp.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionManger @Inject constructor(@ApplicationContext private val context: Context) {
    private val appContext = context.applicationContext

    companion object {
        private val Context.dataStore by preferencesDataStore(Constants.SESSION_AUTH_DATA)
        private val userToken = stringPreferencesKey(Constants.USER_TOKEN)
    }

    suspend fun saveToken(token: String) = context.dataStore.edit {
        it[userToken] = token

    }

    fun getToken() = context.dataStore.data.map {
        it[userToken]
    }

    suspend fun deleteToken() = context.dataStore.edit {
        it.clear()
    }


}