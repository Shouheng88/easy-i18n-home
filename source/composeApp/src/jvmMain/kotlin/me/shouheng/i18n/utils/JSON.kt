package me.shouheng.i18n.utils

import com.google.gson.*
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.*

/** Json 工具类 */
object JSON {

    private val GSON: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(Date::class.java, object : JsonDeserializer<Date> {
                override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date {
                    return Date(json?.asLong ?: 0L)
                }
            })
            .registerTypeAdapter(Date::class.java, object : JsonSerializer<Date> {
                override fun serialize(src: Date?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
                    return JsonPrimitive(src?.time ?: 0L)
                }
            })
            .create()
    }

    /** 获取适用于 Retrofit 的 [Converter.Factory] */
    @JvmStatic
    fun getConvertFactory(): Converter.Factory = GsonConverterFactory.create(GSON)

    /** 获取任意对象的 json 字符串 */
    @JvmStatic
    fun toJson(any: Any): String? =
        runCatching {
            GSON.toJson(any)
        }.getOrNull()

    /** 从 json 字符串中解析任意类型实例 */
    @JvmStatic
    fun <T> from(json: String, type: Class<T>): T? =
        runCatching {
            GSON.fromJson(json, type)
        }.getOrNull()

    /** 从 json 字符串中解析任意类型实例 */
    @JvmStatic
    fun <T> from(json: String, typeOfT: Type): T? =
        runCatching {
            GSON.fromJson<T>(json, typeOfT)
        }.getOrNull()

    /** 从 json 字符串中解析 List 实例 */
    @JvmStatic
    fun <T> listFrom(json: String, type: Type): List<T>? =
        runCatching {
            GSON.fromJson<List<T>>(json, type)
        }.getOrNull()
}
