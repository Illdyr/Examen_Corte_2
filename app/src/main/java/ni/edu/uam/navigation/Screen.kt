package ni.edu.uam.navigation

/**
 * Definición centralizada de las rutas de navegación de la app.
 * Usar este sealed class evita strings duplicados y errores de tipeo.
 */
sealed class Screen(val route: String) {

    /** Pantalla de bienvenida con splash animado. */
    data object Splash : Screen("splash")

    /** Pantalla principal con lista de mascotas. */
    data object Inicio : Screen("inicio")

    /** Pantalla de favoritos. */
    data object Favoritos : Screen("favoritos")

    /** Pantalla de estadísticas / acerca de. */
    data object AcercaDe : Screen("acerca_de")

    /** Pantalla para registrar nueva mascota. */
    data object Registrar : Screen("registrar")

    /** Pantalla de detalle: recibe el id de la mascota. */
    data object Detalle : Screen("detalle/{mascotaId}") {
        fun crearRuta(mascotaId: String) = "detalle/$mascotaId"
        const val ARG_ID = "mascotaId"
    }

    /** Pantalla para editar mascota existente. */
    data object Editar : Screen("editar/{mascotaId}") {
        fun crearRuta(mascotaId: String) = "editar/$mascotaId"
        const val ARG_ID = "mascotaId"
    }
}
