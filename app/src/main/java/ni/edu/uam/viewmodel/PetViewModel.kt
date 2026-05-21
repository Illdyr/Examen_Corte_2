package ni.edu.uam.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ni.edu.uam.model.Mascota

class PetViewModel : ViewModel() {
    // Listas dinámicas
    private val _listaMascotas = mutableStateListOf<Mascota>()
    val listaMascotas: List<Mascota> get() = _listaMascotas

    // Datos ingresados por el usuario
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

    // Actualización de información / Adición
    fun agregarMascota() {
        if (_nombreMascota.value.isNotBlank() && _edadMascota.value.isNotBlank()) {
            val nuevaMascota = Mascota(
                id = _listaMascotas.size + 1,
                nombre = _nombreMascota.value,
                edad = _edadMascota.value.toIntOrNull() ?: 0,
                tipo = _tipoMascota.value
            )
            _listaMascotas.add(nuevaMascota)
            // Limpiar campos
            _nombreMascota.value = ""
            _edadMascota.value = ""
            _tipoMascota.value = ""
        }
    }

    fun eliminarMascota(mascota: Mascota) {
        _listaMascotas.remove(mascota)
    }
}
