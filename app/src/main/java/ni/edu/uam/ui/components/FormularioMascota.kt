package ni.edu.uam.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.model.Genero
import ni.edu.uam.model.Mascota
import ni.edu.uam.model.TipoMascota

/**
 * Estado del formulario de mascota (registro o edición).
 */
data class FormularioMascotaState(
    val nombre: String = "",
    val edad: String = "",
    val tipo: TipoMascota = TipoMascota.PERRO,
    val raza: String = "",
    val genero: Genero = Genero.DESCONOCIDO,
    val descripcion: String = "",
    val errorNombre: String? = null,
    val errorEdad: String? = null
) {
    /** Valida campos requeridos. Devuelve un nuevo estado con errores. */
    fun validar(): FormularioMascotaState {
        val edadInt = edad.toIntOrNull()
        return copy(
            errorNombre = if (nombre.isBlank()) "Ingresa un nombre" else null,
            errorEdad = when {
                edad.isBlank() -> "Ingresa una edad"
                edadInt == null -> "Solo números"
                edadInt < 0 -> "Edad inválida"
                edadInt > 50 -> "Edad poco realista"
                else -> null
            }
        )
    }

    fun esValido(): Boolean {
        val v = validar()
        return v.errorNombre == null && v.errorEdad == null
    }

    /** Construye una mascota a partir del formulario. */
    fun aMascota(idExistente: String? = null, esFavorito: Boolean = false, adoptada: Boolean = false): Mascota {
        val edadInt = edad.toIntOrNull() ?: 0
        return if (idExistente != null) {
            Mascota(
                id = idExistente,
                nombre = nombre.trim(),
                edad = edadInt,
                tipo = tipo,
                raza = raza.trim(),
                genero = genero,
                descripcion = descripcion.trim(),
                esFavorito = esFavorito,
                adoptada = adoptada
            )
        } else {
            Mascota(
                nombre = nombre.trim(),
                edad = edadInt,
                tipo = tipo,
                raza = raza.trim(),
                genero = genero,
                descripcion = descripcion.trim()
            )
        }
    }

    companion object {
        fun fromMascota(m: Mascota) = FormularioMascotaState(
            nombre = m.nombre,
            edad = m.edad.toString(),
            tipo = m.tipo,
            raza = m.raza,
            genero = m.genero,
            descripcion = m.descripcion
        )
    }
}

/**
 * Formulario reutilizable de mascota: usado tanto al registrar como al editar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioMascota(
    estadoInicial: FormularioMascotaState,
    textoBoton: String,
    onGuardar: (FormularioMascotaState) -> Unit,
    modifier: Modifier = Modifier
) {
    var estado by remember { mutableStateOf(estadoInicial) }
    var expandidoTipo by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Vista previa del avatar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = estado.tipo.emoji, fontSize = 64.sp)
            }
        }

        // Nombre
        OutlinedTextField(
            value = estado.nombre,
            onValueChange = { estado = estado.copy(nombre = it, errorNombre = null) },
            label = { Text("Nombre *") },
            placeholder = { Text("Ej. Luna") },
            isError = estado.errorNombre != null,
            supportingText = {
                estado.errorNombre?.let { Text(it) }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        // Tipo (dropdown)
        ExposedDropdownMenuBox(
            expanded = expandidoTipo,
            onExpandedChange = { expandidoTipo = !expandidoTipo }
        ) {
            OutlinedTextField(
                value = "${estado.tipo.emoji} ${estado.tipo.etiqueta}",
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de mascota *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoTipo) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(
                expanded = expandidoTipo,
                onDismissRequest = { expandidoTipo = false }
            ) {
                TipoMascota.entries.forEach { tipo ->
                    DropdownMenuItem(
                        text = { Text("${tipo.emoji}  ${tipo.etiqueta}") },
                        onClick = {
                            estado = estado.copy(tipo = tipo)
                            expandidoTipo = false
                        }
                    )
                }
            }
        }

        // Edad y Raza en fila
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = estado.edad,
                onValueChange = { nuevo ->
                    // Sólo permitimos dígitos
                    if (nuevo.length <= 2 && nuevo.all { it.isDigit() }) {
                        estado = estado.copy(edad = nuevo, errorEdad = null)
                    }
                },
                label = { Text("Edad *") },
                placeholder = { Text("0") },
                isError = estado.errorEdad != null,
                supportingText = { estado.errorEdad?.let { Text(it) } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = estado.raza,
                onValueChange = { estado = estado.copy(raza = it) },
                label = { Text("Raza") },
                placeholder = { Text("Ej. Labrador") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(2f)
            )
        }

        // Género (chips)
        Column {
            Text(
                text = "Género",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Genero.entries.forEach { gen ->
                    val seleccionado = estado.genero == gen
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (seleccionado)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (seleccionado)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = { estado = estado.copy(genero = gen) }
                    ) {
                        Text(
                            text = "${gen.emoji} ${gen.etiqueta}",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }

        // Descripción
        OutlinedTextField(
            value = estado.descripcion,
            onValueChange = { estado = estado.copy(descripcion = it) },
            label = { Text("Descripción") },
            placeholder = { Text("Cuéntanos sobre su personalidad, cuidados, etc.") },
            minLines = 3,
            maxLines = 5,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(4.dp))

        // Botón guardar
        Button(
            onClick = {
                val validado = estado.validar()
                estado = validado
                if (validado.esValido()) {
                    onGuardar(validado)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        ) {
            Icon(Icons.Filled.Save, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.size(8.dp))
            Text(text = textoBoton, style = MaterialTheme.typography.titleMedium)
        }
        Spacer(Modifier.height(16.dp))
    }
}
