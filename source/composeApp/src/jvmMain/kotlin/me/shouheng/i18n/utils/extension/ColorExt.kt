package me.shouheng.i18n.utils.extension

import androidx.compose.ui.graphics.Color

fun Color.alpha(
    alpha: Float
): Color {
    return Color(this.red, this.green, this.blue, alpha)
}