package me.shouheng.i18n.i18n.ios

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.stream.JsonWriter
import io.github.vinceglb.filekit.PlatformFile
import me.shouheng.i18n.data.model.SingleFileI18WordModel
import me.shouheng.i18n.data.model.WordUpdateItem
import me.shouheng.i18n.data.model.XCStrings
import me.shouheng.i18n.manager.TextManager
import me.shouheng.i18n.utils.extension.loge
import me.shouheng.i18n.utils.extension.logi
import org.xmlpull.v1.XmlPullParserException
import java.io.File
import java.io.StringWriter

/** iOS .xcstrings 文件解析 */
object XCStringsParser {

    /** 解析 .xcstrings 资源 */
    fun parse(file: PlatformFile): List<XCStringResource> {
        val fileContent = try {
            file.file.readText(Charsets.UTF_8)
        } catch (e: Exception) {
            loge { "读取文件失败: ${e.message}" }
            e.printStackTrace()
            return emptyList()
        }

        // 解析 JSON 结构
        val xcstrings = Gson().fromJson(fileContent, XCStrings::class.java)

        val resources = mutableListOf<XCStringResource>()
        xcstrings.strings?.entries?.forEach {
            val name = it.key
            val xCStringEntry = it.value
            xCStringEntry.localizations?.entries?.forEach { entry ->
                val language = entry.key
                resources.add(XCStringResource(file, name, language, xcstrings))
            }
        }

        return resources
    }

    /** 删除某个键 */
    fun delete(name: String, file: PlatformFile): Boolean {
        val fileContent = try {
            file.file.readText(Charsets.UTF_8)
        } catch (e: Exception) {
            loge { "读取文件失败: ${e.message}" }
            e.printStackTrace()
            return false
        }
        val isDotSpaceAround = fileContent.contains("\" : \"")

        val jsonObject = JsonParser.parseString(fileContent).asJsonObject
        if (jsonObject.has("strings")) {
            val strings = jsonObject.getAsJsonObject("strings")
            if (strings.has(name)) {
                strings.remove(name)
                // 重新写入到文件
                writeXCString(jsonObject, isDotSpaceAround, file)
                return true
            } else {
                loge { "未找到键: $name" }
            }
        }

        return false
    }

    /** 更新词条 */
    fun update(
        word: SingleFileI18WordModel,
        description: String,
        items: List<WordUpdateItem>
    ): Boolean {
        val origin = word.origin
        if (!origin.isSupport()) {
            logi { "不支持修改这种类型" }
            return false
        }
        val name = word.name
        val file = word.origin.file

        val fileContent = try {
            file.file.readText(Charsets.UTF_8)
        } catch (e: Exception) {
            loge { "读取文件失败: ${e.message}" }
            e.printStackTrace()
            return false
        }
        val isDotSpaceAround = fileContent.contains("\" : \"")

        val jsonObject = JsonParser.parseString(fileContent).asJsonObject
        val strings = jsonObject.getAsJsonObject("strings") ?: JsonObject()
        val entry = strings.getAsJsonObject(name) ?: JsonObject()
        entry.addProperty("comment", description)
        val localizations = entry.getAsJsonObject("localizations") ?: JsonObject()

        // 全部都是复数才是复数
        val isPlural = items.filter { it.value.isNotEmpty() }.all { TextManager.isPlural(it.value) }
        items.forEach {
            val language = it.meaning.language
            // 如果之前没有这个类型的值，并且数据没发生变化，那么就不改它了
            if (!localizations.has(language) && !it.changed) {
                return@forEach
            }

            val localization = localizations.getAsJsonObject(language) ?: JsonObject()

            if (isPlural) {
                // 复数资源
                val pluralRules = JsonObject()
                val items = TextManager.parsePlural(it.value)
                items?.entries?.forEach { entry -> pluralRules.addProperty(entry.key, entry.value) }

                val stringUnit = localization.getAsJsonObject("stringUnit") ?: JsonObject()
                stringUnit.add("pluralRules", pluralRules)
                localization.add("stringUnit", stringUnit)
            } else {
                // 普通字符串资源
                if (localization.has("value")) {
                    localization.addProperty("value", it.value)
                } else {
                    val stringUnit = localization.getAsJsonObject("stringUnit") ?: JsonObject()
                    stringUnit.addProperty("value", it.value)
                    localization.add("stringUnit", stringUnit)
                }
            }

            localizations.add(language, localization)
        }

        entry.add("localizations", localizations)
        strings.add(name, entry)
        jsonObject.add("strings", strings)

        // 重新写入到文件
        writeXCString(jsonObject, isDotSpaceAround, file)
        return true
    }

    /** 写入文件 */
    private fun writeXCString(jsonObject: JsonObject, isDotSpaceAround: Boolean, file: PlatformFile) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        // 重新写入
        val writer = StringWriter()
        val jsonWriter = gson.newJsonWriter(writer)
        // 利用反射修改输出逻辑，在冒号前后增加空格，注意 Gson 的版本，2.11.0 可用
        if (isDotSpaceAround) {
            runCatching {
                val field = JsonWriter::class.java.getDeclaredField("formattedColon")
                field.isAccessible = true
                field.set(jsonWriter, " : ")
            }.onFailure {
                it.printStackTrace()
            }
        }
        // 输出 json
        gson.toJson(jsonObject, jsonWriter)
        val json = writer.toString()
        // 回写 json
        file.file.writeText(json)
    }
}

fun main() {
    try {
        val file = File("/Users/shouhwang/Desktop/repo/ios/CotEditor/CotEditor/Localizables/CommandBar.xcstrings")
//        val result = XCStringsParser.parse(file)
        val json = file.readText()
        val jsonObject = JsonParser.parseString(json)
        val gson = GsonBuilder().setPrettyPrinting().create()
        println(gson.toJson(jsonObject))
    } catch (e: XmlPullParserException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}