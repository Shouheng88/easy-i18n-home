package me.shouheng.i18n.data.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ColorScheme
import me.shouheng.i18n.data.Colors

/** 定义深色主题的配色方案 */
val DarkColorScheme: ColorScheme = darkColorScheme(
    primary = Colors.primary, // 主色
    onPrimary = Color.White, // 主色上的文字颜色，白色对比强

    secondary = Colors.primary, // 次主色，用于强调按钮等
    onSecondary = Color.White, // 次主色上的文字颜色

    secondaryContainer = Colors.secondaryContainer, // 导航栏选中之后的背景颜色

    background = Colors.darkBackground, // 整体背景，看起来更现代
    onBackground = Colors.onDarkBackground, // 背景上的文字颜色

    surface = Colors.darkSurface, // 卡片、弹窗等组件的背景色
    surfaceContainer = Colors.darkSurfaceContainer, // dropdown 菜单等的颜色
    onSurface = Colors.onDarkSurface, // 表面上的文字颜色

    error = Colors.error, // 错误色，醒目的红色
    onError = Color.White, // 错误提示上的文字颜色

    outline = Colors.darkOutline // 边框、分割线颜色，淡灰色
)
