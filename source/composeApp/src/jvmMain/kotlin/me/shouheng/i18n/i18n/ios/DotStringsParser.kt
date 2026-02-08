package me.shouheng.i18n.i18n.ios

import io.github.vinceglb.filekit.PlatformFile
import me.shouheng.i18n.utils.StringUtils
import me.shouheng.i18n.utils.extension.loge
import org.xmlpull.v1.XmlPullParserException
import java.nio.charset.Charset
import kotlin.text.iterator

/** iOS 的 .strings 文件词条加载 */
object DotStringsParser {

    /** 解析.strings文件并返回键值对映射 */
    fun parse(file: PlatformFile, charset: Charset = Charsets.UTF_8): List<IOSStringResource> {
        val fileContent = try {
            file.file.readText(charset)
        } catch (e: Exception) {
            loge { "读取文件失败: ${e.message}" }
            return emptyList()
        }

        val processedContent = preprocessContent(fileContent)
        val entries = splitIntoEntries(processedContent)

        val resources = mutableListOf<IOSStringResource>()
        entries.forEach { entry ->
            val resource = parseStringResource(entry.trim(), file)
            if (resource != null) {
                resources.add(resource)
            }
        }

        return resources
    }

    /** 删除词条 */
    fun delete(name: String, file: PlatformFile): Boolean {
        val resources = parse(file)
        val texts = resources.filter { it.getTextName() != name }
        writeResources(file, texts)
        return true
    }

    /** 更新指定的词条 */
    fun update(index: Int, name: String, value: String, file: PlatformFile): Boolean {
        val resources = parse(file)
        var replaced = false
        val texts = resources.map {
            if (it.name == name) {
                replaced = true
                IOSStringResource(file, name, value, it.nameQuoted)
            } else {
                it
            }
        }.toMutableList()
        // 没有找到指定的词条，说明是新增词条
        if (!replaced) {
            // 如果字符串名称中存在 大小写字母和下划线 之外的词，那么就用引号包裹字符串资源
            val quoted = !StringUtils.isAlphaUnderline(name) ||
                    texts.count { it.nameQuoted } * 3 > texts.size
            val resource = IOSStringResource(file, name, value, quoted)
            if (index >= texts.size) {
                texts.add(resource)
            } else {
                texts.add(index, resource)
            }
        }
        writeResources(file, texts)
        return true
    }

    /** 写入到文件中 */
    private fun writeResources(file: PlatformFile, resources: List<IOSStringResource>) {
        val texts = resources.map {
            var value = it.value
            // 处理引号
            if (value.indexOf('\"') >= 0) {
                val cs = charArrayOf().toMutableList()
                value.forEachIndexed { i, c ->
                    val last = i - 1
                    if (c == '\"' && (last < 0 || value[last] != '\\')) {
                        cs.add('\\')
                    }
                    cs.add(c)
                }
                value = String(cs.toCharArray())
            }
            if (it.nameQuoted) {
                "\"${it.name}\" = \"${value}\";"
            } else {
                "${it.name} = \"${value}\";"
            }
        }
        val text = texts.joinToString("\n")
        file.file.writeText(text)
    }

    /**
     * 预处理内容：移除注释和空行，处理多行字符串连接
     * 修复了转义引号导致的多行状态判断错误
     */
    private fun preprocessContent(content: String): String {
        val lines = content.lines()
        val stringBuilder = StringBuilder()
        var inMultiLineString = false
        var escapeMode = false  // 新增：跟踪转义状态
        var inMultiLineComment = false

        for (line in lines) {
            var processedLine = line.trim()

            if (processedLine.isEmpty() && !inMultiLineString) {
                continue
            }
            // 找多行注释
            if (processedLine.startsWith("/*")) {
                inMultiLineComment = true
            }
            // 出于多行注释
            if (inMultiLineComment) {
                // 找多行注释结尾
                if (processedLine.contains("*/")) {
                    val commentIndex = processedLine.indexOf("*/")
                    if (commentIndex >= 0) {
                        // 对剩余部分进行处理
                        inMultiLineComment = false
                        processedLine = processedLine.substring(commentIndex+2).trim()
                        if (processedLine.isEmpty()) {
                            continue
                        }
                    } else {
                        continue
                    }
                } else {
                    continue
                }
            }

            // 处理单行注释（仅当不在字符串中时）
            var commentIndex = -1
            if (!inMultiLineString) {
                commentIndex = findCommentIndex(processedLine)
            }

            if (commentIndex != -1) {
                processedLine = processedLine.substring(0, commentIndex).trim()
            }

            if (processedLine.isEmpty() && !inMultiLineString) continue

            // 重新实现：正确计算非转义引号的数量
            var unescapedQuoteCount = 0
            escapeMode = false

            for (char in processedLine) {
                when {
                    escapeMode -> {
                        escapeMode = false
                    }
                    char == '\\' -> {
                        escapeMode = true
                    }
                    char == '"' -> {
                        unescapedQuoteCount++
                    }
                }
            }

            // 根据非转义引号的数量判断是否切换多行状态
            if (unescapedQuoteCount % 2 != 0) {
                inMultiLineString = !inMultiLineString
            }

            stringBuilder.append(processedLine)
            if (inMultiLineString) {
                stringBuilder.append("\n")
            }
        }

        return stringBuilder.toString()
    }

    /** 查找注释的起始位置（忽略字符串中的//） */
    private fun findCommentIndex(line: String): Int {
        var inString = false
        var escapeMode = false

        for (i in line.indices) {
            val char = line[i]
            when {
                escapeMode -> {
                    escapeMode = false
                }
                char == '\\' -> {
                    escapeMode = true
                }
                char == '"' -> {
                    inString = !inString
                }
                char == '/' && i < line.length - 1 && line[i + 1] == '/' && !inString -> {
                    return i  // 找到注释起始位置
                }
            }
        }
        return -1
    }

    /** 将预处理后的内容分割为多个键值对条目 */
    private fun splitIntoEntries(content: String): List<String> {
        val entries = mutableListOf<String>()
        val currentEntry = StringBuilder()
        var inString = false
        var escapeMode = false

        for (char in content) {
            when {
                escapeMode -> {
                    escapeMode = false
                    currentEntry.append(char)
                }
                char == '\\' -> {
                    escapeMode = true
                    currentEntry.append(char)
                }
                char == '"' -> {
                    inString = !inString
                    currentEntry.append(char)
                }
                char == ';' && !inString -> {
                    entries.add(currentEntry.toString().trim())
                    currentEntry.clear()
                }
                else -> {
                    currentEntry.append(char)
                }
            }
        }

        return entries
    }

    /** 解析单个键值对条目 */
    private fun parseStringResource(entry: String, file: PlatformFile): IOSStringResource? {
        if (entry.isEmpty()) return null

        var inKey = true
        var inString = false
        var escapeMode = false
        val keyBuilder = StringBuilder()
        val valueBuilder = StringBuilder()
        var currentBuilder = keyBuilder

        for (char in entry) {
            when {
                escapeMode -> {
                    currentBuilder.append(char)
                    escapeMode = false
                }
                char == '\\' -> {
                    escapeMode = true
                    currentBuilder.append(char)
                }
                char == '"' -> {
                    inString = !inString
                    currentBuilder.append(char)
                }
                char == '=' && !inString && inKey -> {
                    inKey = false
                    currentBuilder = valueBuilder
                }
                else -> {
                    currentBuilder.append(char)
                }
            }
        }

        // 处理键：移除可能的引号
        var key = keyBuilder.toString().trim()
        var quoted = false
        if (key.startsWith("\"") && key.endsWith("\"")) {
            key = key.substring(1, key.length - 1)
            quoted = true
        }

        // 处理值：移除可能的引号并还原转义字符
        var value = valueBuilder.toString().trim()
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length - 1)
        }

        // 引号处理掉
        value = value.replace("\\\"", "\"")

        return if (key.isNotEmpty() && value.isNotEmpty()) {
            IOSStringResource(file, key, value, quoted)
        } else {
            null
        }
    }
}

fun main() {
    try {
        // 从文件或资源获取输入流
        val resources = DotStringsParser.parse(PlatformFile("/Users/shouhwang/Desktop/repo/ios/CotEditor/CotEditor/ja.lproj/UnicodeBlock.strings"))
        resources.forEachIndexed { index, resource ->
            println("${index+1}. ${resource.name}[${resource.nameQuoted}] >>> ${resource.value}")
        }
    } catch (e: XmlPullParserException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}