package me.shouheng.i18n.i18n.java

import io.github.vinceglb.filekit.PlatformFile
import me.shouheng.i18n.i18n.AbsTextResource
import me.shouheng.i18n.manager.TextManager
import java.io.*
import java.nio.charset.Charset
import java.util.*

/** Java Properties 文件读取 */
object PropertiesParser {

    /** 解析 Properties 文件 */
    fun parse(
        file: PlatformFile,
        encoding: String? = null
    ): List<AbsTextResource> {
        val encoding = encoding ?: TextManager.JAVA_PROPERTIES_DEFAULT_ENCODING

        val props = Properties()
        props.load(InputStreamReader(FileInputStream(file.file), encoding))
        var resources = props.entries.map {
            PropertyResource(it.key.toString(), it.value.toString(), file)
        }

        // 对读取的结果进行排序：按照关键词名称在原文本中初次出现的顺序排序
        val charset = if (Charset.isSupported(encoding)) Charset.forName(encoding) else Charsets.UTF_8
        val content = file.file.readText(charset)
        resources = resources.sortedBy { content.indexOf(it.name) }

        return resources
    }

    /** 更新词条 */
    fun update(
        value: AbsTextResource,
        index: Int,
        file: PlatformFile,
        encoding: String? = null
    ): Boolean {
        val resources = parse(file, encoding)
        val texts = mutableListOf<AbsTextResource>()
        var replaced = false
        resources.forEach {
            val name = it.getTextName()
            if (name == value.getTextName()) {
                texts.add(value)
                replaced = true
            } else {
                texts.add(it)
            }
        }
        // 如果没找到，可能是新增词条，此时插入一个词条到指定的位置
        if (!replaced) {
            if (index >= texts.size) {
                texts.add(value)
            } else {
                texts.add(index, value)
            }
        }
        writeResources(file, texts, encoding)
        return true
    }

    /** 删除词条 */
    fun delete(name: String, file: PlatformFile, encoding: String? = null): Boolean {
        val resources = parse(file, encoding)
        val texts = resources.filter { it.getTextName() != name }
        writeResources(file, texts, encoding)
        return true
    }

    /** 写入到文件 */
    private fun writeResources(
        file: PlatformFile,
        resources: List<AbsTextResource>,
        encoding: String? = null
    ) {
        val encoding = encoding ?: TextManager.JAVA_PROPERTIES_DEFAULT_ENCODING
        val charset = if (Charset.isSupported(encoding)) Charset.forName(encoding) else Charsets.UTF_8
        val fos = OutputStreamWriter(FileOutputStream(file.file), charset)
        val bw = BufferedWriter(fos)
        val length = resources.size
        resources.forEachIndexed { index, resource ->
            var key = resource.getTextName()
            var value = resource.getDisplayValue()
            key = saveConvert(key, escapeSpace = true, escapeUnicode = false)
            value = saveConvert(value, escapeSpace = false, escapeUnicode = false)
            bw.write("$key=$value")
            if (index != length - 1) {
                bw.newLine()
            }
        }
        bw.flush()
    }

    /** 对写入的 key 和 value 进行转换：复制自 [Properties.saveConvert] */
    private fun saveConvert(
        theString: String,
        escapeSpace: Boolean,
        escapeUnicode: Boolean
    ): String {
        val len = theString.length
        var bufLen = len * 2
        if (bufLen < 0) {
            bufLen = Int.MAX_VALUE
        }
        val outBuffer = StringBuilder(bufLen)
        val hex = HexFormat.of().withUpperCase()
        for (x in 0..<len) {
            val aChar = theString[x]
            if ((aChar.code > 61) && (aChar.code < 127)) {
                if (aChar == '\\') {
                    outBuffer.append('\\')
                    outBuffer.append('\\')
                    continue
                }
                outBuffer.append(aChar)
                continue
            }
            when (aChar) {
                ' ' -> {
                    if (x == 0 || escapeSpace) outBuffer.append('\\')
                    outBuffer.append(' ')
                }
                '\t' -> {
                    outBuffer.append('\\')
                    outBuffer.append('t')
                }
                '\n' -> {
                    outBuffer.append('\\')
                    outBuffer.append('n')
                }
                '\r' -> {
                    outBuffer.append('\\')
                    outBuffer.append('r')
                }
                '\u000c' -> {
                    outBuffer.append('\\')
                    outBuffer.append('f')
                }
                '=', ':', '#', '!' -> {
                    outBuffer.append('\\')
                    outBuffer.append(aChar)
                }
                else -> if (((aChar.code < 0x0020) || (aChar.code > 0x007e)) and escapeUnicode) {
                    outBuffer.append("\\u")
                    outBuffer.append(hex.toHexDigits(aChar))
                } else {
                    outBuffer.append(aChar)
                }
            }
        }
        return outBuffer.toString()
    }
}