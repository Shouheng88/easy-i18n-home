package me.shouheng.i18n.view.settings.dialog.translator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.text_document
import me.shouheng.i18n.data.model.TranslatorType
import me.shouheng.i18n.net.translator.GoogleTranslator
import me.shouheng.i18n.vm.TranslatorSetterShareViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun GoogleTranslatorConfigureView(
    vm: TranslatorSetterShareViewModel
) {
    var googleAPIKey by remember { mutableStateOf(GoogleTranslator.getAPIKey()) }
    val uriHandler = LocalUriHandler.current
    LaunchedEffect(Unit) {
        vm.shouldSave.collect {
            if (it) {
                GoogleTranslator.setAPIKey(googleAPIKey.trim())
                vm.onSaved(true)
            }
        }
    }
    Column(
        modifier = Modifier.padding(horizontal = 15.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        OutlinedTextField(
            value = googleAPIKey,
            onValueChange = { googleAPIKey = it },
            label = { Text("API Key") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            stringResource(Res.string.text_document) + ": " + TranslatorType.GOOGLE.docUrl,
            fontSize = 12.sp,
            style = TextStyle(lineHeight = 14.sp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { uriHandler.openUri(TranslatorType.GOOGLE.docUrl) }
        )
    }
}
