package com.example.airapp
import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión de contexto para crear DataStore
val Context.dataStore by preferencesDataStore(name = "settings")
class UserData(private val context: Context) {
    companion object {
        // Key para guardar el nombre (tipo String)
        val NAME_KEY = stringPreferencesKey("username")
    }

    // Guardar el nombre
    suspend fun saveName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
        }
    }

    // Leer el nombre como Flow
    val getName: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[NAME_KEY] ?: "Flaviongas02"
        }
}