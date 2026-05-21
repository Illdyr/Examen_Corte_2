package ni.edu.uam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import ni.edu.uam.navigation.PetAdoptNavHost
import ni.edu.uam.ui.theme.AdoptAppTheme

/**
 * Punto de entrada de la aplicación PetAdopt.
 * Inicializa el tema y delega la navegación a [PetAdoptNavHost].
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdoptAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    PetAdoptNavHost(navController = navController)
                }
            }
        }
    }
}
