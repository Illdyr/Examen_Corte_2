package ni.edu.uam.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ni.edu.uam.model.Mascota

class MascotaViewModel : ViewModel() {

    private val _mascotas = MutableStateFlow<List<Mascota>>(emptyList())
    val mascotas: StateFlow<List<Mascota>> = _mascotas.asStateFlow()

    private var currentId: Long = 1

    init {
        agregarMascota("Firulais", "Perro", "Mestizo", 2, "Disponible")
        agregarMascota("Mishi", "Gato", "Siamés", 1, "Disponible")
    }

    fun agregarMascota(nombre: String, especie: String, raza: String, edad: Int, estado: String) {
        val nuevaMascota = Mascota(
            id = currentId++, nombre = nombre, especie = especie, raza = raza, edad = edad, estado = estado
        )
        _mascotas.update { it + nuevaMascota }
    }

    fun obtenerMascota(id: Long): Mascota? {
        return _mascotas.value.find { it.id == id }
    }

    fun editarMascota(id: Long, nombre: String, especie: String, raza: String, edad: Int, estado: String) {
        _mascotas.update { lista ->
            lista.map { mascota ->
                if (mascota.id == id) {
                    mascota.copy(nombre = nombre, especie = especie, raza = raza, edad = edad, estado = estado)
                } else {
                    mascota
                }
            }
        }
    }

    fun eliminarMascota(id: Long) {
        _mascotas.update { lista -> lista.filter { it.id != id } }
    }
}