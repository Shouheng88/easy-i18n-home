package me.shouheng.i18n.data.common

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.NavigationRailItemColors
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import me.shouheng.i18n.data.Colors
import me.shouheng.i18n.data.Router.ROUTE_PREMIUM
import org.jetbrains.compose.resources.StringResource

/**
 * 首页 TAB 数据结构
 */
data class HomeTabItem(
    val title: StringResource,
    val icon: ImageVector,
    val route: String,
    val content: @Composable () -> Unit
) {
    companion object {
        @Composable
        fun colors(route: String, colorScheme: ColorScheme): NavigationRailItemColors {
            return if (route == ROUTE_PREMIUM) {
                NavigationRailItemDefaults.colors(
                    selectedIconColor = Colors.premiumTextColor,
                    selectedTextColor = Colors.premiumTextColor,
                    unselectedIconColor = colorScheme.onSurface,
                    unselectedTextColor = colorScheme.onSurface
                )
            } else {
                NavigationRailItemDefaults.colors(
                    selectedIconColor = colorScheme.primary,
                    selectedTextColor = colorScheme.primary,
                    unselectedIconColor = colorScheme.onSurface,
                    unselectedTextColor = colorScheme.onSurface
                )
            }
        }
    }
}