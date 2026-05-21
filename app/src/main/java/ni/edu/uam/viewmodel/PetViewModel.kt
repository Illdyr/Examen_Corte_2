package ni.edu.uam.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ni.edu.uam.data.MascotaRepository
import ni.edu.uam.model.Genero
import ni.edu.uam.model.Mascota
import ni.edu.uam.model.TipoMascota

/**
 * Estado de UI para la pantalla de inicio (filtros y búsqueda).
 */
data class FiltrosUiState(
    val texto: String = "",
    val tipoSeleccionado: TipoMascota? = null,
    val mostrarSoloFavoritos: Boolean = false,
    val mostrarSoloDisponibles: Boolean = false
)

/**
 * ViewModel principal de la app.
 * Centraliza el estado de las mascotas, búsqueda, filtros y acciones CRUD.
 */
class PetViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MascotaRepository(application.applicationContext)

    // === Estado de filtros ===
    private val _filtros = MutableStateFlow(FiltrosUiState())
    val filtros: StateFlow<FiltrosUiState> = _filtros.asStateFlow()

    // === Lista completa cargada desde DataStore ===
    val mascotas: StateFlow<List<Mascota>> = repository.mascotasFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // === Lista filtrada combinando mascotas + filtros ===
    val mascotasFiltradas: StateFlow<List<Mascota>> = combine(mascotas, _filtros) { lista, f ->
        lista.filter { m ->
            val coincideTexto = f.texto.isBlank() ||
                m.nombre.contains(f.texto, ignoreCase = true) ||
                m.raza.contains(f.texto, ignoreCase = true) ||
                m.tipo.etiqueta.contains(f.texto, ignoreCase = true)
            val coincideTipo = f.tipoSeleccionado == null || m.tipo == f.tipoSeleccionado
            val coincideFavorito = !f.mostrarSoloFavoritos || m.esFavorito
            val coincideDisponible = !f.mostrarSoloDisponibles || !m.adoptada
            coincideTexto && coincideTipo && coincideFavorito && coincideDisponible
        }.sortedByDescending { it.fechaRegistro }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // === Mensaje transitorio para Snackbar ===
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init {
        // Si la primera lectura del DataStore arroja una lista vacía,
        // sembramos datos de ejemplo (sólo la primera vez que se abre la app).
        viewModelScope.launch {
            val primerEmit = repository.mascotasFlow.first()
            if (primerEmit.isEmpty()) {
                repository.guardarMascotas(mascotasDeEjemplo())
            }
        }
    }

    // === Acciones de filtro ===
    fun onTextoBusquedaChange(nuevo: String) {
        _filtros.value = _filtros.value.copy(texto = nuevo)
    }

    fun onTipoFiltroChange(tipo: TipoMascota?) {
        _filtros.value = _filtros.value.copy(tipoSeleccionado = tipo)
    }

    fun toggleSoloFavoritos() {
        _filtros.value = _filtros.value.copy(mostrarSoloFavoritos = !_filtros.value.mostrarSoloFavoritos)
    }

    fun toggleSoloDisponibles() {
        _filtros.value = _filtros.value.copy(mostrarSoloDisponibles = !_filtros.value.mostrarSoloDisponibles)
    }

    fun limpiarFiltros() {
        _filtros.value = FiltrosUiState()
    }

    // === CRUD ===
    /** Crea una nueva mascota y la persiste. */
    fun agregarMascota(mascota: Mascota) {
        viewModelScope.launch {
            val nueva = listOf(mascota) + mascotas.value
            repository.guardarMascotas(nueva)
            _mensaje.value = "🎉 ${mascota.nombre} fue registrada/o exitosamente"
        }
    }

    /** Actualiza una mascota existente. */
    fun actualizarMascota(mascota: Mascota) {
        viewModelScope.launch {
            val actualizadas = mascotas.value.map {
                if (it.id == mascota.id) mascota else it
            }
            repository.guardarMascotas(actualizadas)
            _mensaje.value = "✏️ ${mascota.nombre} fue actualizada/o"
        }
    }

    /** Elimina una mascota por id. */
    fun eliminarMascota(id: String) {
        viewModelScope.launch {
            val nombre = mascotas.value.firstOrNull { it.id == id }?.nombre ?: ""
            val nueva = mascotas.value.filterNot { it.id == id }
            repository.guardarMascotas(nueva)
            _mensaje.value = "🗑️ $nombre fue eliminada/o"
        }
    }

    /** Marca o desmarca como favorita. */
    fun toggleFavorito(id: String) {
        viewModelScope.launch {
            val actualizadas = mascotas.value.map {
                if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it
            }
            repository.guardarMascotas(actualizadas)
        }
    }

    /** Marca como adoptada. */
    fun toggleAdoptada(id: String) {
        viewModelScope.launch {
            val actualizadas = mascotas.value.map {
                if (it.id == id) it.copy(adoptada = !it.adoptada) else it
            }
            repository.guardarMascotas(actualizadas)
            val esAhoraAdoptada = actualizadas.first { it.id == id }.adoptada
            _mensaje.value = if (esAhoraAdoptada) "🏠 ¡Mascota adoptada con éxito!" else "↩️ Marcada como disponible"
        }
    }

    /** Devuelve una mascota por id. */
    fun obtenerMascota(id: String): Mascota? = mascotas.value.firstOrNull { it.id == id }

    /** Limpia el mensaje del snackbar tras mostrarse. */
    fun consumirMensaje() {
        _mensaje.value = null
    }

    // === Estadísticas calculadas reactivamente desde la lista de mascotas ===
    val estadisticas: StateFlow<Estadisticas> = mascotas
        .map { lista ->
            Estadisticas(
                total = lista.size,
                disponibles = lista.count { !it.adoptada },
                adoptadas = lista.count { it.adoptada },
                favoritas = lista.count { it.esFavorito }
            )
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Estadisticas())

    companion object {
        /** Factory para inyectar el Application context al ViewModel. */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                PetViewModel(application)
            }
        }

        /** Datos de ejemplo para sembrar la app la primera vez. */
        private fun mascotasDeEjemplo(): List<Mascota> = listOf(
            Mascota(
                nombre = "Luna",
                edad = 2,
                tipo = TipoMascota.PERRO,
                raza = "Labrador",
                genero = Genero.HEMBRA,
                descripcion = "Luna es muy juguetona, le encanta correr y los niños. Está esterilizada y al día con sus vacunas."
            ),
            Mascota(
                nombre = "Michi",
                edad = 1,
                tipo = TipoMascota.GATO,
                raza = "Siamés",
                genero = Genero.MACHO,
                descripcion = "Tierno y muy sociable. Ideal para hogares tranquilos. Usa correctamente la arenera."
            ),
            Mascota(
                nombre = "Toby",
                edad = 4,
                tipo = TipoMascota.PERRO,
                raza = "Mestizo",
                genero = Genero.MACHO,
                descripcion = "Tranquilo, perfecto compañero para personas mayores. Le encanta dormir cerca de su humano.",
                esFavorito = true
            ),
            Mascota(
                nombre = "Pelusa",
                edad = 1,
                tipo = TipoMascota.CONEJO,
                raza = "Holandés enano",
                genero = Genero.HEMBRA,
                descripcion = "Muy curiosa y cariñosa. Le gusta explorar y comer hojas verdes frescas."
            ),
            Mascota(
                nombre = "Kiwi",
                edad = 3,
                tipo = TipoMascota.AVE,
                raza = "Periquito",
                genero = Genero.MACHO,
                descripcion = "Canta hermoso todas las mañanas. Habla algunas palabras y es muy sociable."
            )
        )
    }
}

/** Estadísticas mostradas en la pantalla principal. */
data class Estadisticas(
    val total: Int = 0,
    val disponibles: Int = 0,
    val adoptadas: Int = 0,
    val favoritas: Int = 0
)
