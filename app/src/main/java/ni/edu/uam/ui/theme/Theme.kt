package ni.edu.uam.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.toArgb
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

private val LightPetColorScheme = lightColorScheme(
    primary = PetPrimary,
    onPrimary = Color.White,

    secondary = PetSecondary,
    onSecondary = PetTextDark,

    tertiary = PetTertiary,
    onTertiary = PetTextDark,

    background = PetBackground,
    onBackground = PetTextDark,

    surface = PetSurface,
    onSurface = PetTextDark,

    surfaceVariant = PetSurfaceVariant,
    onSurfaceVariant = PetTextSoft,

    error = PetError,
    onError = Color.White,

    outline = PastelPurple
)

private val DarkPetColorScheme = darkColorScheme(
    primary = PastelPurple,
    onPrimary = Color.White,

    secondary = PastelPink,
    onSecondary = PetTextDark,

    tertiary = PastelBlue,
    onTertiary = PetTextDark,

    background = Color(0xFF2A2028),
    onBackground = Color(0xFFFFF7F2),

    surface = Color(0xFF3A2D36),
    onSurface = Color(0xFFFFF7F2),

    surfaceVariant = Color(0xFF4A3944),
    onSurfaceVariant = Color(0xFFE8D5DD),

    error = PetError,
    onError = Color.White
)

val PetShapes = Shapes(
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(22.dp),
    large = RoundedCornerShape(32.dp)
)

@Composable
fun AdoptAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkPetColorScheme
    } else {
        LightPetColorScheme
    }

    val context = LocalContext.current

    if (context is Activity) {
        context.window.statusBarColor = colorScheme.background.toArgb()
        context.window.navigationBarColor = colorScheme.background.toArgb()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.window.decorView.systemUiVisibility =
                android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PetTypography,
        shapes = PetShapes,
        content = content
    )
}