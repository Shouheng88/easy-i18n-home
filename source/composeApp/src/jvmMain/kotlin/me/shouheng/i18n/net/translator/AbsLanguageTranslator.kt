package me.shouheng.i18n.net.translator

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import me.shouheng.i18n.utils.extension.json

abstract class AbsLanguageTranslator: ILanguageTranslator {
    protected val client = HttpClient {
        install(ContentNegotiation) {
            json(
                json
            ) // 使用 kotlinx.serialization JSON
        }
        install(Logging) {
            level = LogLevel.ALL   // 输出所有信息：headers + body
            logger = Logger.DEFAULT
        }
        install(HttpTimeout) {
            connectTimeoutMillis = TIMEOUT_IN_SECONDS * 1000
            requestTimeoutMillis = TIMEOUT_IN_SECONDS * 1000
            socketTimeoutMillis = TIMEOUT_IN_SECONDS * 1000
        }
    }

    companion object {
        const val TIMEOUT_IN_SECONDS = 15L
    }
}