package ni.edu.uam.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ni.edu.uam.ui.screens.AcercaDeScreen
import ni.edu.uam.ui.screens.DetalleScreen
import ni.edu.uam.ui.screens.EditarMascotaScreen
import ni.edu.uam.ui.screens.FavoritosScreen
import ni.edu.uam.ui.screens.InicioScreen
import ni.edu.uam.ui.screens.RegistrarMascotaScreen
import ni.edu.uam.ui.screens.SplashScreen
import ni.edu.uam.viewmodel.PetViewModel

/**
 * Grafo de navegación principal de la app.
 * Define cada pantalla y sus transiciones animadas.
 */
@Composable
fun PetAdoptNavHost(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    // Usamos el mismo ViewModel para todas las pantallas para que compartan estado.
    val petViewModel: PetViewModel = viewModel(factory = PetViewModel.Factory)

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { it / 4 }, animationSpec = tween(300)) +
                fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it / 4 }, animationSpec = tween(300)) +
                fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -it / 4 }, animationSpec = tween(300)) +
                fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it / 4 }, animationSpec = tween(300)) +
                fadeOut(animationSpec = tween(300))
        }
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Inicio.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Inicio.route) {
            InicioScreen(
                viewModel = petViewModel,
                onAgregarClick = { navController.navigate(Screen.Registrar.route) },
                onMascotaClick = { id -> navController.navigate(Screen.Detalle.crearRuta(id)) },
                onFavoritosClick = { navController.navigate(Screen.Favoritos.route) },
                onAcercaDeClick = { navController.navigate(Screen.AcercaDe.route) }
            )
        }

        composable(Screen.Favoritos.route) {
            FavoritosScreen(
                viewModel = petViewModel,
                onBack = { navController.popBackStack() },
                onMascotaClick = { id -> navController.navigate(Screen.Detalle.crearRuta(id)) },
                onInicioClick = {
                    navController.navigate(Screen.Inicio.route) {
                        popUpTo(Screen.Inicio.route) { inclusive = true }
                    }
                },
                onAcercaDeClick = {
                    navController.navigate(Screen.AcercaDe.route) {
                        popUpTo(Screen.Inicio.route)
                    }
                }
            )
        }

        composable(Screen.AcercaDe.route) {
            AcercaDeScreen(
                viewModel = petViewModel,
                onBack = { navController.popBackStack() },
                onInicioClick = {
                    navController.navigate(Screen.Inicio.route) {
                        popUpTo(Screen.Inicio.route) { inclusive = true }
                    }
                },
                onFavoritosClick = {
                    navController.navigate(Screen.Favoritos.route) {
                        popUpTo(Screen.Inicio.route)
                    }
                }
            )
        }

        composable(Screen.Registrar.route) {
            RegistrarMascotaScreen(
                viewModel = petViewModel,
                onBack = { navController.popBackStack() },
                onGuardado = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Detalle.route,
            arguments = listOf(navArgument(Screen.Detalle.ARG_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString(Screen.Detalle.ARG_ID).orEmpty()
            DetalleScreen(
                mascotaId = id,
                viewModel = petViewModel,
                onBack = { navController.popBackStack() },
                onEditar = { navController.navigate(Screen.Editar.crearRuta(id)) },
                onEliminado = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Editar.route,
            arguments = listOf(navArgument(Screen.Editar.ARG_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString(Screen.Editar.ARG_ID).orEmpty()
            EditarMascotaScreen(
                mascotaId = id,
                viewModel = petViewModel,
                onBack = { navController.popBackStack() },
                onGuardado = { navController.popBackStack() }
            )
        }
    }
}
