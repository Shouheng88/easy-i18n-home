package me.shouheng.i18n.manager

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.shouheng.i18n.data.Event
import me.shouheng.i18n.data.common.SnackMessage
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

object MessageManager {

    private val _message = MutableStateFlow<SnackMessage?>(null)
    val message: StateFlow<SnackMessage?> = _message.asStateFlow()

    private val _event = MutableStateFlow<Event?>(null)
    val event: StateFlow<Event?> = _event.asStateFlow()

    fun showMessage(snackMessage: SnackMessage) {
        _message.value = snackMessage
    }

    fun post(event: Event) {
        _event.value = event
    }
}

fun post(name: String, data: Any? = null) {
    MessageManager.post(Event(name, data))
}

fun showMessage(message: SnackMessage) {
    MessageManager.showMessage(message)
}

fun showSuccess(message: String) {
    MessageManager.showMessage(SnackMessage.success(message))
}

fun showInfo(message: String) {
    MessageManager.showMessage(SnackMessage.info(message))
}

fun showWarn(message: String) {
    MessageManager.showMessage(SnackMessage.warn(message))
}

fun showError(message: String) {
    MessageManager.showMessage(SnackMessage.error(message))
}

fun showSuccess(init: suspend () -> String) {
    GlobalScope.launch {
        MessageManager.showMessage(SnackMessage.success(init()))
    }
}

fun showInfo(init: suspend () -> String) {
    GlobalScope.launch {
        MessageManager.showMessage(SnackMessage.info(init()))
    }
}

fun showWarn(init: suspend () -> String) {
    GlobalScope.launch {
        MessageManager.showMessage(SnackMessage.warn(init()))
    }
}

fun showError(init: suspend () -> String) {
    GlobalScope.launch {
        MessageManager.showMessage(SnackMessage.error(init()))
    }
}

fun showSuccess(message: StringResource) {
    GlobalScope.launch {
        MessageManager.showMessage(SnackMessage.success(getString(message)))
    }
}

fun showInfo(message: StringResource) {
    GlobalScope.launch {
        MessageManager.showMessage(SnackMessage.info(getString(message)))
    }
}

fun showWarn(message: StringResource) {
    GlobalScope.launch {
        MessageManager.showMessage(SnackMessage.warn(getString(message)))
    }
}

fun showError(message: StringResource) {
    GlobalScope.launch {
        MessageManager.showMessage(SnackMessage.error(getString(message)))
    }
}