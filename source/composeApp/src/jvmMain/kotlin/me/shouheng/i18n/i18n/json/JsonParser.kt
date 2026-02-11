package me.shouheng.i18n.i18n.json

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.github.vinceglb.filekit.PlatformFile
import me.shouheng.i18n.i18n.AbsTextResource
import me.shouheng.i18n.utils.JSON
import me.shouheng.i18n.utils.extension.loge

object JsonParser {

    fun parse(file: PlatformFile): List<AbsTextResource> {
        val fileContent = try {
            file.file.readText(Charsets.UTF_8)
        } catch (e: Exception) {
            loge { "读取文件失败: ${e.message}" }
            e.printStackTrace()
            return emptyList()
        }

        val jsonElement = try {
            JsonParser.parseString(fileContent)
        } catch (e: Exception) {
            loge { "解析 JSON 失败: ${e.message}" }
            return emptyList()
        }

        if (!jsonElement.isJsonObject) {
            return emptyList()
        }

        val resources = mutableListOf<AbsTextResource>()
        flatten(jsonElement.asJsonObject, "", resources, file)
        return resources
    }

    private fun flatten(
        element: JsonObject,
        prefix: String,
        resources: MutableList<AbsTextResource>,
        file: PlatformFile
    ) {
        element.entrySet().forEach { entry ->
            val key = if (prefix.isEmpty()) entry.key else "$prefix.${entry.key}"
            val value = entry.value
            if (value.isJsonObject) {
                flatten(value.asJsonObject, key, resources, file)
            } else if (value.isJsonPrimitive && value.asJsonPrimitive.isString) {
                resources.add(JsonResource(key, value.asString, file))
            }
        }
    }

    fun update(
        key: String,
        value: String,
        file: PlatformFile
    ): Boolean {
        val fileContent = try {
            file.file.readText(Charsets.UTF_8)
        } catch (e: Exception) {
            loge { "读取文件失败: ${e.message}" }
            e.printStackTrace()
            return false
        }

        val jsonElement = try {
            JsonParser.parseString(fileContent)
        } catch (e: Exception) {
            JsonParser.parseString("{}")
        }

        val root = if (jsonElement.isJsonObject) jsonElement.asJsonObject else JsonObject()
        
        // Unflatten and update
        val keys = key.split(".")
        var current = root
        for (i in 0 until keys.size - 1) {
            val k = keys[i]
            if (!current.has(k) || !current.get(k).isJsonObject) {
                val newObj = JsonObject()
                current.add(k, newObj)
                current = newObj
            } else {
                current = current.getAsJsonObject(k)
            }
        }
        current.addProperty(keys.last(), value)

        writeJsonFile(root, file)
        return true
    }

    fun delete(key: String, file: PlatformFile): Boolean {
        val fileContent = try {
            file.file.readText(Charsets.UTF_8)
        } catch (e: Exception) {
            loge { "读取文件失败: ${e.message}" }
            e.printStackTrace()
            return false
        }

        val jsonElement = try {
            JsonParser.parseString(fileContent)
        } catch (e: Exception) {
            return false
        }

        if (!jsonElement.isJsonObject) return false
        val root = jsonElement.asJsonObject

        val keys = key.split(".")
        deleteRecursive(root, keys, 0)

        writeJsonFile(root, file)
        return true
    }

    private fun deleteRecursive(current: JsonObject, keys: List<String>, index: Int): Boolean {
        if (index == keys.size - 1) {
            return if (current.has(keys[index])) {
                current.remove(keys[index])
                true
            } else {
                false
            }
        }

        val k = keys[index]
        if (current.has(k) && current.get(k).isJsonObject) {
            val child = current.getAsJsonObject(k)
            val deleted = deleteRecursive(child, keys, index + 1)
            // If child object is empty after deletion, remove it too? 
            // Usually keeping empty object is safer or removing it depending on requirement.
            // Vue i18n might handle empty objects fine. Let's keep it for now unless empty.
            if (child.entrySet().isEmpty()) {
                current.remove(k)
            }
            return deleted
        }
        return false
    }

    private fun writeJsonFile(jsonObject: JsonObject, file: PlatformFile) {
        val gson = GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create()
        val json = gson.toJson(jsonObject)
        file.file.writeText(json)
    }
}
