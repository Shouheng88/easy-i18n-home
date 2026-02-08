package me.shouheng.i18n.widget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.shouheng.i18n.data.UIConst

/** 标题栏 */
@Preview
@Composable
fun PageToolbar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .height(UIConst.TOOLBAR_HEIGHT.dp)
            .padding(horizontal = UIConst.TOOLBAR_H_PADDING.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

/** 页面的标题 */
@Preview
@Composable
fun PageTitle(title: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        Icon(icon, title, modifier = Modifier.size(34.dp))
        Text(title, fontSize = 20.sp)
    }
}
