package me.shouheng.i18n.utils.extension

import com.google.gson.JsonObject
import kotlinx.serialization.json.Json

val json = Json {
    ignoreUnknownKeys = true // 忽略JSON中未知的字段
    isLenient = true // 宽松模式（允许JSON格式不严格）
    encodeDefaults = true // 序列化时包含默认值
}

/** 尝试获取 String */
fun JsonObject.tryString(key: String): String? {
    if (this.has(key)) {
        return this.get(key).asString
    }
    return null
}

/** 尝试获取 JsonObject */
fun JsonObject.tryJsonObject(key: String): JsonObject? {
    if (this.has(key)) {
        return this.get(key).asJsonObject
    }
    return null
}
