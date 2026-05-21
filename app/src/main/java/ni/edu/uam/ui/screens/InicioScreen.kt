package ni.edu.uam.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.ui.components.BarraBusqueda
import ni.edu.uam.ui.components.EstadoVacio
import ni.edu.uam.ui.components.FiltrosTipo
import ni.edu.uam.ui.components.MascotaCard
import ni.edu.uam.ui.components.PantallaPrincipal
import ni.edu.uam.ui.components.PetAdoptBottomBar
import ni.edu.uam.ui.components.TarjetaEstadistica
import ni.edu.uam.viewmodel.PetViewModel

/**
 * Pantalla principal de la app: header con estadísticas, búsqueda,
 * filtros y lista de mascotas con FAB para agregar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    viewModel: PetViewModel,
    onAgregarClick: () -> Unit,
    onMascotaClick: (String) -> Unit,
    onFavoritosClick: () -> Unit,
    onAcercaDeClick: () -> Unit
) {
    val mascotas by viewModel.mascotasFiltradas.collectAsState()
    val filtros by viewModel.filtros.collectAsState()
    val estadisticas by viewModel.estadisticas.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Muestra el mensaje del snackbar cuando cambia
    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.consumirMensaje()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🐾", fontSize = 24.sp)
                        Text(
                            text = "  PetAdopt",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            PetAdoptBottomBar(
                tabSeleccionada = PantallaPrincipal.INICIO,
                onInicioClick = { /* ya estamos en Inicio */ },
                onFavoritosClick = onFavoritosClick,
                onAcercaDeClick = onAcercaDeClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAgregarClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar mascota")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {

            // === Header con gradiente y estadísticas ===
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "¡Bienvenido! 👋",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "Tenemos amigos esperándote",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                    )
                    Spacer(Modifier.height(12.dp))

                    // Fila de estadísticas
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TarjetaEstadistica(
                            titulo = "Total",
                            valor = estadisticas.total.toString(),
                            emoji = "🐾",
                            colorFondo = Color(0xFFFF8A65),
                            modifier = Modifier.weight(1f)
                        )
                        TarjetaEstadistica(
                            titulo = "Disponibles",
                            valor = estadisticas.disponibles.toString(),
                            emoji = "💚",
                            colorFondo = Color(0xFF81C784),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TarjetaEstadistica(
                            titulo = "Adoptadas",
                            valor = estadisticas.adoptadas.toString(),
                            emoji = "🏠",
                            colorFondo = Color(0xFF64B5F6),
                            modifier = Modifier.weight(1f)
                        )
                        TarjetaEstadistica(
                            titulo = "Favoritas",
                            valor = estadisticas.favoritas.toString(),
                            emoji = "❤️",
                            colorFondo = Color(0xFFEF5350),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            BarraBusqueda(
                valor = filtros.texto,
                onValueChange = viewModel::onTextoBusquedaChange
            )
            Spacer(Modifier.height(8.dp))
            FiltrosTipo(
                tipoSeleccionado = filtros.tipoSeleccionado,
                onTipoSeleccionado = viewModel::onTipoFiltroChange
            )
            Spacer(Modifier.height(8.dp))

            // === Lista de mascotas ===
            if (mascotas.isEmpty()) {
                EstadoVacio(
                    titulo = "No hay mascotas",
                    descripcion = "Toca el botón + para registrar la primera",
                    emoji = "🐶"
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        bottom = 80.dp
                    )
                ) {
                    items(items = mascotas, key = { it.id }) { mascota ->
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
