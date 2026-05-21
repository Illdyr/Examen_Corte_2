package ni.edu.uam.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import ni.edu.uam.model.Mascota

/**
 * Extensión que crea un DataStore único para toda la app
 * (almacena las mascotas como JSON serializado).
 */
private val Context.dataStore by preferencesDataStore(name = "pet_adopt_prefs")

/**
 * Repositorio que persiste la lista de mascotas en DataStore.
 * Centraliza el acceso a la fuente de datos siguiendo el patrón Repository.
 */
class MascotaRepository(private val context: Context) {

    private val mascotasKey = stringPreferencesKey("mascotas_json")
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Flujo reactivo con la lista actual de mascotas almacenadas.
     */
    val mascotasFlow: Flow<List<Mascota>> = context.dataStore.data.map { prefs ->
        val raw = prefs[mascotasKey] ?: return@map emptyList()
        runCatching {
            json.decodeFromString<List<Mascota>>(raw)
        }.getOrDefault(emptyList())
    }

    /**
     * Guarda la lista completa de mascotas en DataStore.
     */
    suspend fun guardarMascotas(mascotas: List<Mascota>) {
        val raw = json.encodeToString(mascotas)
        context.dataStore.edit { prefs ->
            prefs[mascotasKey] = raw
        }
    }
}
