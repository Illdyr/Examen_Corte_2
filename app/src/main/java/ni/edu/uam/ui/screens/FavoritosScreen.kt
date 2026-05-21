package ni.edu.uam.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ni.edu.uam.ui.components.EstadoVacio
import ni.edu.uam.ui.components.MascotaCard
import ni.edu.uam.ui.components.PantallaPrincipal
import ni.edu.uam.ui.components.PetAdoptBottomBar
import ni.edu.uam.viewmodel.PetViewModel

/**
 * Pantalla que muestra únicamente las mascotas marcadas como favoritas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(
    viewModel: PetViewModel,
    onBack: () -> Unit,
    onMascotaClick: (String) -> Unit,
    onInicioClick: () -> Unit,
    onAcercaDeClick: () -> Unit
) {
    val mascotas by viewModel.mascotas.collectAsState()
    val favoritas = mascotas.filter { it.esFavorito }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "❤️ Mis favoritas",
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
        bottomBar = {
            PetAdoptBottomBar(
                tabSeleccionada = PantallaPrincipal.FAVORITOS,
                onInicioClick = onInicioClick,
                onFavoritosClick = { /* ya estamos */ },
                onAcercaDeClick = onAcercaDeClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {
            Spacer(Modifier.height(8.dp))
            if (favoritas.isEmpty()) {
                EstadoVacio(
                    titulo = "Aún no tienes favoritas",
                    descripcion = "Toca el corazón en cualquier mascota para guardarla aquí",
                    emoji = "💔"
                )
            } else {
                Text(
                    text = "${favoritas.size} mascota${if (favoritas.size != 1) "s" else ""} en tu corazón",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(items = favoritas, key = { it.id }) { mascota ->
                        MascotaCard(
                            mascota = mascota,
                            onClick = { onMascotaClick(mascota.id) },
                            onFavoritoClick = { viewModel.toggleFavorito(mascota.id) }
                        )
                    }
                }
            }
        }
    }
}
