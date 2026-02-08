package me.shouheng.i18n.view.info.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.doc_title
import easy_i18n.composeapp.generated.resources.text_cancel
import me.shouheng.i18n.data.model.AppDocument
import me.shouheng.i18n.widget.NormalDialogFooter
import me.shouheng.i18n.widget.NormalDialogTitle
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/** 使用文档 */
@Composable
fun DocumentDialog(onClose: () -> Unit) {
    Dialog(onDismissRequest = onClose) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(.8f)
                    .fillMaxWidth(0.9f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 标题
                NormalDialogTitle(stringResource(Res.string.doc_title))
                Column (
                    modifier = Modifier.verticalScroll(rememberScrollState())
                        .weight(1f)
                        .padding(horizontal = 15.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ){
                    AppDocument.items.forEach {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                stringResource(it.titleRes),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                stringResource(it.detailRes).trimIndent(),
                                fontSize = 13.sp,
                            )
                        }
                    }
                }
                // 底部按钮
                NormalDialogFooter(
                    right = stringResource(Res.string.text_cancel),
                    onRight = onClose
                )
            }
        }
    }
}

@Preview
@Composable
private fun DocumentDialogPreview() {
    DocumentDialog {  }
}