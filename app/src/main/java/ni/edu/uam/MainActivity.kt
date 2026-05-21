package ni.edu.uam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
<<<<<<< Updated upstream
=======
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
>>>>>>> Stashed changes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
<<<<<<< Updated upstream
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ni.edu.uam.ui.theme.AdoptAppTheme
=======

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ni.edu.uam.model.Mascota
import ni.edu.uam.ui.theme.AdoptAppTheme
import ni.edu.uam.viewmodel.PetViewModel
>>>>>>> Stashed changes

class MainActivity : ComponentActivity() {

    private lateinit var petViewModel: PetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        petViewModel = ViewModelProvider(this)[PetViewModel::class.java]

        setContent {
            AdoptAppTheme {
<<<<<<< Updated upstream
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
=======
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(viewModel = petViewModel)
>>>>>>> Stashed changes
                }
            }
        }
    }
}

@Composable
<<<<<<< Updated upstream
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
=======
fun AppNavigation(viewModel: PetViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        composable("inicio") {
            HomeScreen(
                onNavigateToList = {
                    navController.navigate("lista")
                }
            )
        }

        composable("lista") {
            PetListScreen(
                mascotas = viewModel.listaMascotas,
                onPetClick = { id ->
                    navController.navigate("detalle/$id")
                },
                onAddPet = {
                    navController.navigate("agregar")
                }
            )
        }

        composable("agregar") {
            AddPetScreen(
                viewModel = viewModel,
                onSave = {
                    viewModel.agregarMascota()
                    navController.navigate("lista") {
                        popUpTo("lista") { inclusive = true }
                    }
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "detalle/{petId}",
            arguments = listOf(
                navArgument("petId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: 0
            val mascota = viewModel.obtenerMascota(petId)

            PetDetailScreen(
                mascota = mascota,
                onBack = {
                    navController.popBackStack()
                },
                onDelete = {
                    if (mascota != null) {
                        viewModel.eliminarMascota(mascota)
                    }

                    navController.navigate("lista") {
                        popUpTo("lista") { inclusive = true }
                    }
                }
            )
        }
    }
>>>>>>> Stashed changes
}

@Preview(showBackground = true)
@Composable
<<<<<<< Updated upstream
fun GreetingPreview() {
    AdoptAppTheme {
        Greeting("Android")
    }
=======
fun HomeScreen(onNavigateToList: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🐾 AdoptApp",
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Registra y visualiza mascotas disponibles para adopción.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNavigateToList,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver mascotas")
        }
    }
}

@Composable
fun PetListScreen(
    mascotas: List<Mascota>,
    onPetClick: (Int) -> Unit,
    onAddPet: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Mascotas",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "Lista de mascotas registradas",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAddPet,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar mascota")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (mascotas.isEmpty()) {
            EmptyListMessage()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(mascotas) { mascota ->
                    PetCard(
                        mascota = mascota,
                        onClick = {
                            onPetClick(mascota.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyListMessage() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🐾",
                style = MaterialTheme.typography.displayMedium
            )

            Text(
                text = "No hay mascotas registradas",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Presiona el botón para agregar una mascota.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PetCard(
    mascota: Mascota,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "${getPetEmoji(mascota.tipo)} ${mascota.nombre}",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Tipo: ${mascota.tipo}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Edad: ${mascota.edad} años",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun AddPetScreen(
    viewModel: PetViewModel,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val nombre by viewModel.nombreMascota
    val edad by viewModel.edadMascota
    val tipo by viewModel.tipoMascota

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Agregar mascota",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(18.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = viewModel::onNombreChange,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = edad,
            onValueChange = viewModel::onEdadChange,
            label = { Text("Edad") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = tipo,
            onValueChange = viewModel::onTipoChange,
            label = { Text("Tipo") },
            placeholder = { Text("Perro, Gato, Conejo...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text("Cancelar")
        }
    }
}

@Composable
fun PetDetailScreen(
    mascota: Mascota?,
    onBack: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = onBack) {
            Text("Atrás")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (mascota == null) {
            Text(
                text = "Mascota no encontrada",
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = getPetEmoji(mascota.tipo),
                        style = MaterialTheme.typography.displayLarge
                    )

                    Text(
                        text = mascota.nombre,
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Tipo: ${mascota.tipo}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "Edad: ${mascota.edad} años",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Eliminar mascota")
            }
        }
    }
}

fun getPetEmoji(tipo: String): String {
    return when (tipo.lowercase()) {
        "perro" -> "🐶"
        "gato" -> "🐱"
        "conejo" -> "🐰"
        "hamster" -> "🐹"
        "hámster" -> "🐹"
        "ave" -> "🐦"
        "pajaro" -> "🐦"
        "pájaro" -> "🐦"
        else -> "🐾"
    }
>>>>>>> Stashed changes
}