package me.shouheng.i18n.data.common

inline fun <T> Resource<T>.onSuccess(
    block: (res: Resource<T>) -> Unit
): Resource<T> = apply {
    if (isSuccess) {
        block.invoke(this)
    }
}

inline fun <T> Resource<T>.onFailure(
    block: (res: Resource<T>) -> Unit
): Resource<T> = apply {
    if (isFailure) {
        block.invoke(this)
    }
}

inline fun <T> Resource<T>.onLoading(
    block: (res: Resource<T>) -> Unit
): Resource<T> = apply {
    if (isLoading) {
        block.invoke(this)
    }
}

inline fun <T> Resource<T>.onProgress(
    block: (res: Resource<T>) -> Unit
): Resource<T> = apply {
    if (isProgress) {
        block.invoke(this)
    }
}