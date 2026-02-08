package me.shouheng.i18n.net.server.repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.shouheng.i18n.data.common.Resource
import kotlin.coroutines.CoroutineContext

/** Repo 基类 */
open class BaseRepo {

    /** 启动一个任务 */
    fun <T> execute(
        onResult: (Resource<T>) -> Unit = {},
        context: CoroutineContext = Dispatchers.Main,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return GlobalScope.launch(context, start) {
            runCatching {
                block()
            }.onFailure {
                onResult(Resource.failure<T>("-1", it.message).apply {
                    this.throwable = it
                })
            }
        }
    }

    /** 在 [Dispatchers.IO] 和 [runCatching] 中执行任务 */
    suspend fun <T> runCachingInIO(
        onResult: (Resource<T>) -> Unit = {},
        block: suspend CoroutineScope.() -> T
    ) = withContext(Dispatchers.IO) {
        runCatching {
            block()
        }
    }.onSuccess {
        onResult(Resource.success(it))
    }.onFailure {
        onResult(Resource.failure<T>("", it.message).apply {
            this.throwable = it
        })
    }
}