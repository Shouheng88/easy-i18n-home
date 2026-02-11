package me.shouheng.i18n.i18n.yaml

import io.github.vinceglb.filekit.PlatformFile
import me.shouheng.i18n.i18n.AbsTextResource
import me.shouheng.i18n.utils.extension.loge
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.StringWriter
import java.util.regex.Pattern

object YamlParser {

    fun parse(file: PlatformFile): List<AbsTextResource> {
        val fileContent = try {
            file.file.readText(Charsets.UTF_8)
        } catch (e: Exception) {
            loge { "读取文件失败: ${e.message}" }
            e.printStackTrace()
            return emptyList()
        }

        val yamlMap = try {
            val yaml = Yaml()
            yaml.load<Any>(fileContent) ?: emptyMap<String, Any>()
        } catch (e: Exception) {
            loge { "解析 YAML 失败: ${e.message}" }
            return emptyList()
        }

        val resources = mutableListOf<AbsTextResource>()
        flattenValue(yamlMap, "", resources, file)
        return resources
    }

    private fun flattenValue(
        value: Any?,
        prefix: String,
        resources: MutableList<AbsTextResource>,
        file: PlatformFile
    ) {
        if (value is Map<*, *>) {
            @Suppress("UNCHECKED_CAST")
            val map = value as Map<String, Any>
            map.entries.forEach { entry ->
                val key = if (prefix.isEmpty()) entry.key else "$prefix.${entry.key}"
                flattenValue(entry.value, key, resources, file)
            }
        } else if (value is List<*>) {
            value.forEachIndexed { index, item ->
                // If prefix is empty (root list), use "[0]", else "key[0]"
                val key = "$prefix[$index]"
                flattenValue(item, key, resources, file)
            }
        } else {
            if (prefix.isNotEmpty()) {
                resources.add(YamlResource(prefix, value.toString(), file))
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

        val yaml = createYaml()
        val data = try {
            yaml.load<Any>(fileContent) ?: mutableMapOf<String, Any>()
        } catch (e: Exception) {
            mutableMapOf<String, Any>()
        }
        
        // Handle root type
        var root = data
        if (root !is MutableMap<*, *> && root !is MutableList<*>) {
             root = mutableMapOf<String, Any>()
        }

        val segments = parseKeySegments(key)
        if (segments.isEmpty()) return false

        try {
            updateRecursive(root, segments, 0, value)
            return writeYamlFile(root, file)
        } catch (e: Exception) {
            loge { "Update failed: ${e.message}" }
            e.printStackTrace()
            return false
        }
    }
    
    private fun updateRecursive(current: Any, segments: List<PathSegment>, index: Int, value: String) {
        val segment = segments[index]
        val isLast = index == segments.size - 1
        
        if (segment is PathSegment.MapKey) {
            val map = current as? MutableMap<String, Any> 
                ?: throw IllegalStateException("Expected Map at segment $segment but got ${current::class}")
            
            if (isLast) {
                map[segment.key] = value
            } else {
                val nextSegment = segments[index + 1]
                val child = map[segment.key]
                val nextContainer = prepareContainer(child, nextSegment)
                map[segment.key] = nextContainer
                updateRecursive(nextContainer, segments, index + 1, value)
            }
        } else if (segment is PathSegment.ListIndex) {
            val list = current as? MutableList<Any?> 
                ?: throw IllegalStateException("Expected List at segment $segment but got ${current::class}")
            
            ensureListSize(list, segment.index)
            
            if (isLast) {
                list[segment.index] = value
            } else {
                val nextSegment = segments[index + 1]
                val child = list[segment.index]
                val nextContainer = prepareContainer(child, nextSegment)
                list[segment.index] = nextContainer
                updateRecursive(nextContainer, segments, index + 1, value)
            }
        }
    }
    
    private fun prepareContainer(current: Any?, nextSegment: PathSegment): Any {
        return when (nextSegment) {
            is PathSegment.MapKey -> {
                if (current is MutableMap<*, *>) current else mutableMapOf<String, Any>()
            }
            is PathSegment.ListIndex -> {
                if (current is MutableList<*>) current else mutableListOf<Any?>()
            }
        }
    }

    private fun ensureListSize(list: MutableList<Any?>, index: Int) {
        while (list.size <= index) {
            list.add(null)
        }
    }

    fun delete(key: String, file: PlatformFile): Boolean {
        val fileContent = try {
            file.file.readText(Charsets.UTF_8)
        } catch (e: Exception) {
            return false
        }

        val yaml = createYaml()
        val data = try {
            yaml.load<Any>(fileContent) ?: return false
        } catch (e: Exception) {
            return false
        }
        
        val segments = parseKeySegments(key)
        if (segments.isEmpty()) return false
        
        try {
            if (deleteRecursive(data, segments, 0)) {
                writeYamlFile(data, file)
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun deleteRecursive(current: Any, segments: List<PathSegment>, index: Int): Boolean {
        val segment = segments[index]
        val isLast = index == segments.size - 1
        
        if (segment is PathSegment.MapKey) {
            val map = current as? MutableMap<String, Any> ?: return false
            if (isLast) {
                return map.remove(segment.key) != null
            } else {
                val child = map[segment.key] ?: return false
                val deleted = deleteRecursive(child, segments, index + 1)
                if (isEmptyContainer(child)) {
                    map.remove(segment.key)
                }
                return deleted
            }
        } else if (segment is PathSegment.ListIndex) {
            val list = current as? MutableList<Any?> ?: return false
            if (segment.index >= list.size) return false
            
            if (isLast) {
                list.removeAt(segment.index)
                return true
            } else {
                val child = list[segment.index] ?: return false
                val deleted = deleteRecursive(child, segments, index + 1)
                if (isEmptyContainer(child)) {
                    list.removeAt(segment.index)
                }
                return deleted
            }
        }
        return false
    }

    private fun isEmptyContainer(container: Any): Boolean {
        return (container is Map<*, *> && container.isEmpty()) ||
               (container is List<*> && container.isEmpty())
    }

    private fun createYaml(): Yaml {
        val options = DumperOptions()
        options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        options.isPrettyFlow = true
        options.setAllowUnicode(true)
        return Yaml(options)
    }

    private fun writeYamlFile(data: Any, file: PlatformFile): Boolean {
        return try {
            val yaml = createYaml()
            val writer = StringWriter()
            yaml.dump(data, writer)
            file.file.writeText(writer.toString())
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    private sealed class PathSegment {
        data class MapKey(val key: String) : PathSegment()
        data class ListIndex(val index: Int) : PathSegment()
    }
    
    private fun parseKeySegments(key: String): List<PathSegment> {
        val segments = mutableListOf<PathSegment>()
        // Regex to match property (excluding . [ ]) OR index ([digits])
        val p = Pattern.compile("([^.\\[\\]]+)|(\\[(\\d+)\\])")
        val m = p.matcher(key)
        while(m.find()) {
            if (m.group(1) != null) {
                segments.add(PathSegment.MapKey(m.group(1)))
            } else if (m.group(3) != null) {
                segments.add(PathSegment.ListIndex(m.group(3).toInt()))
            }
        }
        return segments
    }
}
