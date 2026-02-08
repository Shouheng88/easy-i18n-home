package me.shouheng.i18n.utils.extension

import io.github.aakira.napier.Napier

fun logv(message: String, throwable: Throwable? = null, tag: String? = null) {
    Napier.v(message, throwable, tag)
}

fun logv(throwable: Throwable? = null, tag: String? = null, message: () -> String) {
    Napier.v(throwable, tag, message)
}

fun logi(message: String, throwable: Throwable? = null, tag: String? = null) {
    Napier.i(message, throwable, tag)
}

fun logi(throwable: Throwable? = null, tag: String? = null, message: () -> String) {
    Napier.i(throwable, tag, message)
}

fun logd(message: String, throwable: Throwable? = null, tag: String? = null) {
    Napier.d(message, throwable, tag)
}

fun logd(throwable: Throwable? = null, tag: String? = null, message: () -> String) {
    Napier.d(throwable, tag, message)
}

fun logw(message: String, throwable: Throwable? = null, tag: String? = null) {
    Napier.w(message, throwable, tag)
}

fun logw(throwable: Throwable? = null, tag: String? = null, message: () -> String) {
    Napier.w(throwable, tag, message)
}

fun loge(message: String, throwable: Throwable? = null, tag: String? = null) {
    Napier.e(message, throwable, tag)
}
fun loge(throwable: Throwable? = null, tag: String? = null, message: () -> String) {
    Napier.e(throwable, tag, message)
}

fun logwtf(message: String, throwable: Throwable? = null, tag: String? = null) {
    Napier.wtf(message, throwable, tag)
}

fun logwtf(throwable: Throwable? = null, tag: String? = null, message: () -> String) {
    Napier.wtf(throwable, tag, message)
}