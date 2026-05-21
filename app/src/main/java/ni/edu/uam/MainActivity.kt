package ni.edu.uam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ni.edu.uam.model.Mascota
import ni.edu.uam.ui.theme.AdoptAppTheme

// Datos de ejemplo para la aplicación
val listaDeMascotas = listOf(
    Mascota(1, "Firulais", 3, "Perro"),
    Mascota(2, "Michi", 2, "Gato"),
    Mascota(3, "Rex", 5, "Perro"),
    Mascota(4, "Luna", 1, "Gato"),
    Mascota(5, "Simba", 4, "León")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdoptAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "inicio") {
        composable("inicio") {
            HomeScreen(onNavigateToList = { navController.navigate("lista") })
        }
        composable("lista") {
            PetListScreen(onPetClick = { id -> navController.navigate("detalle/$id") })
        }
        composable(
            route = "detalle/{petId}",
            arguments = listOf(navArgument("petId") { type = NavType.IntType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: 0
            PetDetailScreen(
                petId = petId,
                onAdopt = { navController.navigate("confirmar/$petId") },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "confirmar/{petId}",
            arguments = listOf(navArgument("petId") { type = NavType.IntType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: 0
            ConfirmScreen(
                petId = petId,
                onConfirm = {
                    navController.navigate("lista") {
                        popUpTo("lista") { inclusive = true }
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun HomeScreen(onNavigateToList: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bienvenido a AdoptApp", fontSize = 28.sp)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onNavigateToList) {
            Text("Ver Mascotas Disponibles")
        }
    }
}

@Composable
fun PetListScreen(onPetClick: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = "Mascotas para Adoptar", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(listaDeMascotas) { mascota ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onPetClick(mascota.id) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = mascota.nombre, style = MaterialTheme.typography.titleLarge)
                        Text(text = "Tipo: ${mascota.tipo}")
                    }
                }
            }
        }
    }
}

@Composable
fun PetDetailScreen(petId: Int, onAdopt: () -> Unit, onBack: () -> Unit) {
    val mascota = listaDeMascotas.find { it.id == petId }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(modifier = Modifier.height(48.dp))
        Button(onClick = onBack) { Text("Atrás") }
        Spacer(modifier = Modifier.height(24.dp))
        if (mascota != null) {
            Text(text = "Detalles de ${mascota.nombre}", style = MaterialTheme.typography.headlineLarge)
            Text(text = "Edad: ${mascota.edad} años")
            Text(text = "Tipo: ${mascota.tipo}")
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Esta es una mascota muy amigable que busca un hogar lleno de amor. Es muy juguetona y le gusta salir a pasear.")
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onAdopt, modifier = Modifier.fillMaxWidth()) {
                Text("Adoptar")
            }
        } else {
            Text("Mascota no encontrada")
        }
    }
}

@Composable
fun ConfirmScreen(petId: Int, onConfirm: () -> Unit, onCancel: () -> Unit) {
    val mascota = listaDeMascotas.find { it.id == petId }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (mascota != null) {
            Text(text = "¿Deseas adoptar a ${mascota.nombre}?", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(32.dp))
            Row {
                Button(onClick = onCancel, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Text("Cancelar")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = onConfirm) {
                    Text("Confirmar")
                }
            }
        }
    }
}
