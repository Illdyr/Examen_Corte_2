package ni.edu.uam.model

import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Modelo de datos que representa una mascota disponible para adopción.
 *
 * @property id Identificador único (UUID).
 * @property nombre Nombre de la mascota.
 * @property edad Edad en años.
 * @property tipo Tipo de mascota (perro, gato, conejo, ave, etc.).
 * @property raza Raza o descripción específica.
 * @property genero Género de la mascota.
 * @property descripcion Descripción larga, personalidad, etc.
 * @property esFavorito Indica si está marcada como favorita.
 * @property adoptada Indica si ya fue adoptada.
 * @property fechaRegistro Fecha en que se registró (timestamp).
 */
@Serializable
data class Mascota(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val edad: Int,
    val tipo: TipoMascota,
    val raza: String = "",
    val genero: Genero = Genero.DESCONOCIDO,
    val descripcion: String = "",
    val esFavorito: Boolean = false,
    val adoptada: Boolean = false,
    val fechaRegistro: Long = System.currentTimeMillis()
)

/**
 * Tipos de mascota soportados. Cada tipo trae su emoji y etiqueta legible.
 */
@Serializable
enum class TipoMascota(val etiqueta: String, val emoji: String) {
    PERRO("Perro", "\uD83D\uDC36"),
    GATO("Gato", "\uD83D\uDC31"),
    CONEJO("Conejo", "\uD83D\uDC30"),
    AVE("Ave", "\uD83D\uDC26"),
    PEZ("Pez", "\uD83D\uDC1F"),
    HAMSTER("Hámster", "\uD83D\uDC39"),
    TORTUGA("Tortuga", "\uD83D\uDC22"),
    OTRO("Otro", "\uD83D\uDC3E");

    companion object {
        fun fromEtiqueta(etiqueta: String): TipoMascota =
            entries.firstOrNull { it.etiqueta.equals(etiqueta, ignoreCase = true) } ?: OTRO
    }
}

/**
 * Género de la mascota.
 */
@Serializable
enum class Genero(val etiqueta: String, val emoji: String) {
    MACHO("Macho", "♂"),
    HEMBRA("Hembra", "♀"),
    DESCONOCIDO("Desconocido", "?")
}
