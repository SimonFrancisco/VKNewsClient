package francisco.simon.vknewsclient.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Black900,
    secondary = Black900,
    tertiary = Color.Black,

    onPrimary = Color.White,
    onSecondary = Black500,
    onTertiary = Color.White,
    background = Black900,
    onBackground = Color.White,

    //Navigation Bar
    surface = Black900,
    onSurface = Color.White,
    secondaryContainer = Black900,
    onSecondaryContainer = Color.White,
    onSurfaceVariant = Black500,

    // FAB
    primaryContainer = Black500,
    onPrimaryContainer = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    secondary = Color.White,
    tertiary = Color.White,
    onPrimary = Black900,
    onSecondary = Black500,
    onTertiary = Color.Black,
    background = Color.White,
    onBackground = Color(0xFF000000),

    //Navigation Bar
    surface = Color.White,
    onSurface = Black900,
    secondaryContainer = Color.White,
    onSecondaryContainer = Black900,
    onSurfaceVariant = Black500,

    // FAB
    primaryContainer = Color.White,
    onPrimaryContainer = Black900
)

@Composable
fun VKNewsClientTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}