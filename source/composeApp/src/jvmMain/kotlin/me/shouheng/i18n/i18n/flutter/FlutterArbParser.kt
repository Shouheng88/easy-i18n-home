package me.shouheng.i18n.i18n.flutter

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.github.vinceglb.filekit.PlatformFile
import me.shouheng.i18n.i18n.AbsTextResource
import me.shouheng.i18n.utils.JSON
import me.shouheng.i18n.utils.extension.loge
import me.shouheng.i18n.utils.extension.tryJsonObject
import me.shouheng.i18n.utils.extension.tryString

/** Flutter 的 .arb 文件解析器 */
object FlutterArbParser {

    /** 解析 */
    fun parse(file: PlatformFile): List<AbsTextResource> {
        val fileContent = try {
            file.file.readText(Charsets.UTF_8)
        } catch (e: Exception) {
            loge { "读取文件失败: ${e.message}" }
            e.printStackTrace()
            return emptyList()
        }

        val json = JSON.from(fileContent, JsonObject::class.java)
        json ?: loge { "解析 flutter arb json 失败" }
        json ?: return emptyList()

        val resources = mutableListOf<AbsTextResource>()
        json.keySet().forEach {
            if (!it.startsWith("@")) {
                val value = json.tryString(it) ?: ""
                val keyProp = "@${it}"
                val desc = json.tryJsonObject(keyProp)?.tryString("description")
                resources.add(FlutterArbResource(it, value, desc, file))
            }
        }

        return resources
    }

    /** 删除词条 */
    fun delete(name: String, file: PlatformFile): Boolean {
        val fileContent = try {
            file.file.readText(Charsets.UTF_8)
        } catch (e: Exception) {
            loge { "读取文件失败: ${e.message}" }
            e.printStackTrace()
            return false
        }

        val jsonObject = JsonParser.parseString(fileContent).asJsonObject
        jsonObject.remove(name)
        jsonObject.remove("@$name")
        writeArbFile(jsonObject, file)

        return true
    }

    /** 更新词条 */
    fun update(
        value: AbsTextResource,
        index: Int,
        file: PlatformFile,
    ): Boolean {
        val fileContent = try {
            file.file.readText(Charsets.UTF_8)
        } catch (e: Exception) {
            loge { "读取文件失败: ${e.message}" }
            e.printStackTrace()
            return false
        }

        val jsonObject = JsonParser.parseString(fileContent).asJsonObject
        jsonObject.addProperty(value.getTextName(), value.getDisplayValue())

        writeArbFile(jsonObject, file)
        return true
    }

    /** 写入文件 */
    private fun writeArbFile(jsonObject: JsonObject, file: PlatformFile) {
        val gson = GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create()
        // 重新写入
        val json = gson.toJson(jsonObject)
        // 回写 json
        file.file.writeText(json)
    }
}

fun main() {
    val resources = FlutterArbParser.parse(PlatformFile("/Users/shouhwang/Desktop/repo/flutter/flutter_first/flutter_sample/build/app/intermediates/flutter/debug/flutter_assets/i18n/app_en.arb"))
    println(resources)
}