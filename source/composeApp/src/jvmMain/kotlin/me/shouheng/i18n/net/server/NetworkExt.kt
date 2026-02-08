package me.shouheng.i18n.net.server

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.shouheng.i18n.data.common.Resource
import me.shouheng.i18n.net.server.model.base.BaseBusinessResponse
import me.shouheng.i18n.net.server.model.base.BusinessResponse

/** 执行 [BusinessResponse] 请求操作 */
suspend fun <T> callBusiness(request: suspend () -> BusinessResponse<T>): Resource<T> =
    withContext(Dispatchers.IO) {
        try {
            val response = request()
            return@withContext response.asResource()
        } catch (t: Throwable) {
            return@withContext Resource.failure<T>("${NetworkApi.CODE_IO_ERROR}", t.message).apply {
                this.throwable = t
            }
        }
    }

/** 执行 [BaseBusinessResponse] 请求操作 */
suspend fun <T, B> callBaseBusiness(request: suspend () -> BaseBusinessResponse<T, B>): Resource<T> =
    withContext(Dispatchers.IO) {
        try {
            val response = request()
            return@withContext response.asResource()
        } catch (t: Throwable) {
            return@withContext Resource.failure<T>("${NetworkApi.CODE_IO_ERROR}", t.message).apply {
                this.throwable = t
            }
        }
    }

/** 执行任意类型的请求操作 */
suspend fun <T> call(request: suspend () -> T): Resource<T> =
    withContext(Dispatchers.IO) {
        try {
            val data = request()
            val response = Resource.success(data)
            return@withContext response
        } catch (t: Throwable) {
            return@withContext Resource.failure<T>("${NetworkApi.CODE_IO_ERROR}", t.message).apply {
                this.throwable = t
            }
        }
    }
