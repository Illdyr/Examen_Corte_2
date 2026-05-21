package ni.edu.uam.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ni.edu.uam.model.Mascota

class PetViewModel : ViewModel() {

    private val _listaMascotas = mutableStateListOf<Mascota>()
    val listaMascotas: List<Mascota> get() = _listaMascotas

    private val _nombreMascota = mutableStateOf("")
    val nombreMascota: State<String> = _nombreMascota

    private val _edadMascota = mutableStateOf("")
    val edadMascota: State<String> = _edadMascota

    private val _tipoMascota = mutableStateOf("")
    val tipoMascota: State<String> = _tipoMascota

    fun onNombreChange(nuevoNombre: String) {
        _nombreMascota.value = nuevoNombre
    }

    fun onEdadChange(nuevaEdad: String) {
        _edadMascota.value = nuevaEdad
    }

    fun onTipoChange(nuevoTipo: String) {
        _tipoMascota.value = nuevoTipo
    }

    fun agregarMascota() {
        val nombre = _nombreMascota.value.trim()
        val edad = _edadMascota.value.toIntOrNull()
        val tipo = _tipoMascota.value.trim()

        if (nombre.isNotBlank() && edad != null && tipo.isNotBlank()) {
            val nuevaMascota = Mascota(
                id = _listaMascotas.size + 1,
                nombre = nombre,
                edad = edad,
                tipo = tipo
            )

            _listaMascotas.add(nuevaMascota)

            _nombreMascota.value = ""
            _edadMascota.value = ""
            _tipoMascota.value = ""
        }
    }

    fun eliminarMascota(mascota: Mascota) {
        _listaMascotas.remove(mascota)
    }

    fun obtenerMascota(id: Int): Mascota? {
        return _listaMascotas.find { mascota ->
            mascota.id == id
        }
    }
}