package ni.edu.uam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.model.Mascota
import ni.edu.uam.ui.theme.AdoptAppTheme
import ni.edu.uam.viewmodel.PetViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdoptAppTheme {
                PetScreen()
            }
        }
    }
}

@Composable
fun PetScreen(viewModel: PetViewModel = viewModel()) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(text = "Adopción de Mascotas", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Formulario para ingresar datos
            OutlinedTextField(
                value = viewModel.nombreMascota.value,
                onValueChange = { viewModel.onNombreChange(it) },
                label = { Text("Nombre de la Mascota") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.edadMascota.value,
                onValueChange = { viewModel.onEdadChange(it) },
                label = { Text("Edad") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.tipoMascota.value,
                onValueChange = { viewModel.onTipoChange(it) },
                label = { Text("Tipo (Perro, Gato, etc.)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.agregarMascota() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar Mascota")
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Lista de Mascotas", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            // Lista dinámica
            LazyColumn {
                items(viewModel.listaMascotas) { mascota ->
                    PetItem(mascota = mascota, onDelete = { viewModel.eliminarMascota(mascota) })
                }
            }
        }
    }
}

@Composable
fun PetItem(mascota: Mascota, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Nombre: ${mascota.nombre}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Edad: ${mascota.edad}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Tipo: ${mascota.tipo}", style = MaterialTheme.typography.bodyMedium)
            }
            Button(onClick = onDelete) {
                Text("Eliminar")
            }
        }
    }
}
