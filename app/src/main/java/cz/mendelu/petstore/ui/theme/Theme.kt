package cz.mendelu.petstore.ui.theme

import android.app.Activity
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
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = primary,
    onPrimary = dark,
    primaryContainer = primary,
    onPrimaryContainer = dark,


    secondary = secondary,
    secondaryContainer = Color(0xFFDDD0C4),
    onSecondaryContainer = Color(0xFF2E241B),

    tertiary = ternary,

    background = background,

    onSurface = baseText,
    onBackground = baseText,
    onSurfaceVariant = Color(0xFF4F4640),
    outline = Color(0xFF80756C),
    outlineVariant = Color(0xFFD3C8BF),
    surfaceVariant = Color(0xFFE6DED6),

    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF8F3EE),
    surfaceContainer = Color(0xFFF3EDE7),
    surfaceContainerHigh = Color(0xFFEFE8E1),
    surfaceContainerHighest = cardColor

    /* Other default colors to override
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun PetStoreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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