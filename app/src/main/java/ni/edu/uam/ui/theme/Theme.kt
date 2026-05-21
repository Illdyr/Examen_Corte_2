package ni.edu.uam.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = OrangePrimaryDarkMode,
    onPrimary = TextDark,
    primaryContainer = PeachDarkMode,
    onPrimaryContainer = TextLightDarkMode,
    secondary = BrownSecondaryDarkMode,
    onSecondary = TextDark,
    tertiary = GreenAccentDarkMode,
    onTertiary = TextDark,
    background = DarkBackground,
    onBackground = TextLightDarkMode,
    surface = DarkSurface,
    onSurface = TextLightDarkMode,
    surfaceVariant = PeachDarkMode,
    onSurfaceVariant = TextSecondaryDarkMode,
    error = RedDeleteDarkMode,
    onError = TextDark
)

private val LightColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = SurfaceLight,
    primaryContainer = PeachLight,
    onPrimaryContainer = OrangePrimaryDark,
    secondary = BrownSecondary,
    onSecondary = SurfaceLight,
    tertiary = GreenAccent,
    onTertiary = SurfaceLight,
    background = CreamBackground,
    onBackground = TextDark,
    surface = SurfaceLight,
    onSurface = TextDark,
    surfaceVariant = PeachLight,
    onSurfaceVariant = TextLight,
    error = RedDelete,
    onError = SurfaceLight
)

/**
 * Tema principal de la app PetAdopt.
 * Deshabilitamos dynamicColor para mantener nuestra identidad visual cálida.
 */
@Composable
fun AdoptAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
