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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.model.Mascota
import ni.edu.uam.viewmodel.PetViewModel

/**
 * Pantalla de detalle que muestra toda la información de una mascota
 * y permite editarla, eliminarla, marcarla como favorita o como adoptada.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    mascotaId: String,
    viewModel: PetViewModel,
    onBack: () -> Unit,
    onEditar: () -> Unit,
    onEliminado: () -> Unit
) {
    // Observamos la lista para refrescar cuando se haga toggle de favorito/adoptada.
    val mascotas by viewModel.mascotas.collectAsState()
    // Capturamos en variable local para que Kotlin haga smart-cast a no-nulo.
    val mascota: Mascota? = mascotas.firstOrNull { it.id == mascotaId }

    var mostrarConfirmacionBorrar by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Detalle", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    val m = mascota
                    if (m != null) {
                        IconButton(onClick = { viewModel.toggleFavorito(m.id) }) {
                            Icon(
                                imageVector = if (m.esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        IconButton(onClick = onEditar) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = { mostrarConfirmacionBorrar = true }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        // === Caso: mascota no encontrada ===
        if (mascota == null) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Mascota no encontrada", style = MaterialTheme.typography.titleMedium)
            }
            return@Scaffold
        }

        // A partir de aquí, mascota está garantizada no-nula.
        // La pasamos al contenido como parámetro explícito para que el
        // smart-cast funcione con seguridad.
        ContenidoDetalle(
            mascota = mascota,
            innerPadding = innerPadding,
            onEditar = onEditar,
            onToggleAdoptada = { viewModel.toggleAdoptada(mascota.id) }
        )

        // Diálogo de confirmación de eliminación (dentro del scope no-nulo)
        if (mostrarConfirmacionBorrar) {
            AlertDialog(
                onDismissRequest = { mostrarConfirmacionBorrar = false },
                title = { Text("¿Eliminar mascota?") },
                text = {
                    Text("¿Estás seguro de que deseas eliminar a ${mascota.nombre}? Esta acción no se puede deshacer.")
                },
                confirmButton = {
                    TextButton(onClick = {
                        mostrarConfirmacionBorrar = false
                        viewModel.eliminarMascota(mascota.id)
                        onEliminado()
                    }) {
                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarConfirmacionBorrar = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

/**
 * Contenido principal del detalle (cuerpo de la pantalla).
 * Se extrae a un composable separado para mantener el smart-cast de mascota.
 */
@Composable
private fun ContenidoDetalle(
    mascota: Mascota,
    innerPadding: androidx.compose.foundation.layout.PaddingValues,
    onEditar: () -> Unit,
    onToggleAdoptada: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // === Header con avatar grande ===
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = mascota.tipo.emoji, fontSize = 86.sp)
            }
        }

        // === Tarjeta con datos ===
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = mascota.nombre,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = mascota.genero.emoji,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                if (mascota.adoptada) {
                    Spacer(Modifier.height(6.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(50),
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.size(4.dp))
                            Text(
                                "Ya tiene un hogar",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                FilaDato("Tipo", "${mascota.tipo.emoji} ${mascota.tipo.etiqueta}")
                FilaDato("Raza", mascota.raza.ifBlank { "No especificada" })
                FilaDato("Edad", if (mascota.edad == 1) "1 año" else "${mascota.edad} años")
                FilaDato("Género", mascota.genero.etiqueta)
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Sobre mí",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = mascota.descripcion.ifBlank {
                        "Sin descripción todavía. ¡Edita esta mascota para contar su historia!"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Justify
                )
            }
        }

        // === Botón principal: adoptar / liberar ===
        Button(
            onClick = onToggleAdoptada,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = if (mascota.adoptada)
                ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            else
                ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
            shape = RoundedCornerShape(50)
        ) {
            Icon(
                Icons.Filled.Home,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = if (mascota.adoptada) "Marcar como disponible" else "¡Adoptar ahora!",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(
            onClick = onEditar,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(50)
        ) {
            Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.size(8.dp))
            Text("Editar información")
        }
        Spacer(Modifier.height(24.dp))
    }
}

/** Fila de información etiqueta + valor reutilizable dentro del detalle. */
@Composable
private fun FilaDato(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
