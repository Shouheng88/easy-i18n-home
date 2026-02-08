package me.shouheng.i18n.data.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import me.shouheng.i18n.data.Colors

/** 定义浅色主题的配色方案 */
val LightColorScheme: ColorScheme = lightColorScheme(
    primary = Colors.primary, // 主色
    onPrimary = Color.White, // 主色上的文字颜色，白色对比强

    secondary = Colors.primary, // 次主色，用于强调按钮等
    onSecondary = Color.White, // 次主色上的文字颜色

    secondaryContainer = Colors.secondaryContainer, // 导航栏选中之后的背景颜色

    background = Colors.background, // 整体背景，看起来更现代
    onBackground = Colors.onBackground, // 背景上的文字颜色

    surface = Colors.surface, // 卡片、弹窗等组件的背景色
    surfaceContainer = Colors.surfaceContainer, // dropdown 菜单等的颜色
    onSurface = Colors.onSurface, // 表面上的文字颜色

    error = Colors.error, // 错误色，醒目的红色
    onError = Color.White, // 错误提示上的文字颜色

    outline = Colors.outline // 边框、分割线颜色，淡灰色
)
