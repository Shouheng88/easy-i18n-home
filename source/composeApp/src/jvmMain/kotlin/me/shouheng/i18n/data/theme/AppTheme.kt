package me.shouheng.i18n.data.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.set_app_theme_dark
import easy_i18n.composeapp.generated.resources.set_app_theme_light
import easy_i18n.composeapp.generated.resources.set_app_theme_system
import org.jetbrains.compose.resources.StringResource

/** 应用主题 */
enum class AppTheme(
    val id: Int,
    val nameRes: StringResource,
    val icon: ImageVector,
    val getColorSchema: @Composable () -> ColorScheme
) {
    SYSTEM(
        0,
        Res.string.set_app_theme_system,
        Icons.Default.Contrast,
        { if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme }
    ),
    LIGHT(
        1,
        Res.string.set_app_theme_light,
        Icons.Default.LightMode,
        { LightColorScheme }
    ),
    DARK(
        2,
        Res.string.set_app_theme_dark,
        Icons.Default.DarkMode,
        { DarkColorScheme }
    );

    companion object {
        fun from(id: Int): AppTheme {
            return entries.firstOrNull { it.id == id } ?: SYSTEM
        }
    }
}