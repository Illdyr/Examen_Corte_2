package ni.edu.uam.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ni.edu.uam.ui.components.FormularioMascota
import ni.edu.uam.ui.components.FormularioMascotaState
import ni.edu.uam.viewmodel.PetViewModel

/**
 * Pantalla para editar una mascota existente.
 * Carga los datos actuales en el formulario y al guardar actualiza.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarMascotaScreen(
    mascotaId: String,
    viewModel: PetViewModel,
    onBack: () -> Unit,
    onGuardado: () -> Unit
) {
    val mascotas by viewModel.mascotas.collectAsState()
    val mascota = mascotas.firstOrNull { it.id == mascotaId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (mascota != null) "Editar a ${mascota.nombre}" else "Editar",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (mascota == null) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Mascota no encontrada")
            }
            return@Scaffold
        }
        FormularioMascota(
            estadoInicial = FormularioMascotaState.fromMascota(mascota),
            textoBoton = "Guardar cambios",
            onGuardar = { estado ->
                val actualizada = estado.aMascota(
                    idExistente = mascota.id,
                    esFavorito = mascota.esFavorito,
                    adoptada = mascota.adoptada
                )
                viewModel.actualizarMascota(actualizada)
                onGuardado()
            },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )
    }
}
