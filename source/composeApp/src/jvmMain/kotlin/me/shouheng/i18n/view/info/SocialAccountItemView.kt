package me.shouheng.i18n.view.info

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import me.shouheng.i18n.manager.UrlManager
import me.shouheng.i18n.net.server.model.vo.SocialAccount

/** 社交账号条目 */
@Preview
@Composable
fun SocialAccountItemView(account: SocialAccount) {
    val urlHandler = UrlManager.handler
    Row(
        modifier = Modifier
            .clickable(onClick = {
                if (!account.link.isNullOrEmpty()) {
                    urlHandler.handle(account.link ?: "")
                }
            })
            .heightIn(min = 40.dp)
            .padding(end = 10.dp, start = 15.dp)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        if (account.logo != null) {
            KamelImage(
                resource = { asyncPainterResource(account.logo!!) },
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                account.platform ?: "",
                fontSize = 14.sp,
                modifier = Modifier,
                lineHeight = 18.sp
            )
            if (!account.description.isNullOrEmpty()) {
                Text(
                    account.description ?: "",
                    fontSize = 12.sp,
                    modifier = Modifier.alpha(0.8f),
                    lineHeight = 16.sp
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!account.name.isNullOrEmpty()) {
                Text(account.name ?: "", fontSize = 12.sp, modifier = Modifier.alpha(0.8f))
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier
                    .alpha(0.8f)
                    .size(18.dp)
            )
        }
    }
}