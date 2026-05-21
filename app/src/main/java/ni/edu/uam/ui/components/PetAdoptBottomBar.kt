package ni.edu.uam.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/**
 * Identifica cuál pantalla principal está activa para resaltar la pestaña.
 */
enum class PantallaPrincipal { INICIO, FAVORITOS, ACERCA_DE }

/**
 * Barra de navegación inferior reutilizable.
 * Centraliza el estilo y el comportamiento de las tres pestañas principales.
 */
@Composable
fun PetAdoptBottomBar(
    tabSeleccionada: PantallaPrincipal,
    onInicioClick: () -> Unit,
    onFavoritosClick: () -> Unit,
    onAcercaDeClick: () -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        NavigationBarItem(
            selected = tabSeleccionada == PantallaPrincipal.INICIO,
            onClick = onInicioClick,
            icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
            label = { Text("Inicio", style = MaterialTheme.typography.labelMedium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        NavigationBarItem(
            selected = tabSeleccionada == PantallaPrincipal.FAVORITOS,
            onClick = onFavoritosClick,
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favoritos") },
            label = { Text("Favoritos", style = MaterialTheme.typography.labelMedium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        NavigationBarItem(
            selected = tabSeleccionada == PantallaPrincipal.ACERCA_DE,
            onClick = onAcercaDeClick,
            icon = { Icon(Icons.Filled.Info, contentDescription = "Acerca de") },
            label = { Text("Acerca de", style = MaterialTheme.typography.labelMedium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}
