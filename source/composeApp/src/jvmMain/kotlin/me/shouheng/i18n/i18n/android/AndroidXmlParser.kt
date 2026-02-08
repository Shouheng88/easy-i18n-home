package me.shouheng.i18n.i18n.android

import io.github.vinceglb.filekit.PlatformFile
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.FileInputStream

/**
 * 解析包含复杂标签的 Android strings.xml，支持: string、plurals、CDATA、xliff:g 等命名空间标签
 *
 * - Android strings.xml 文档：https://developer.android.com/guide/topics/resources/string-resource?hl=zh-cn
 */
object AndroidXmlParser {

    /** 开始解析 */
    fun parse(file: PlatformFile): AndroidResource {
        val inputStream = FileInputStream(file.file)
        val textResources = mutableListOf<AbsAndroidTextResource>()
        val namespaces = mutableListOf<String>()

        val factory = XmlPullParserFactory.newInstance()
        // 启用命名空间支持
        factory.isNamespaceAware = true
        val parser = factory.newPullParser()
        parser.setInput(inputStream, "UTF-8")

        var curPluralName: String? = null
        val curPluralItems = mutableMapOf<String, String>()

        var curArrayName: String? = null
        val curArrayItems = mutableListOf<String>()

        val attributes = mutableMapOf<String, String>()

        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (val tagName = parser.name) {
                        "resources" -> {
                            // 获取当前标签的深度
                            val depth = parser.getDepth()
                            // 获取上一深度命名空间数量（排除当前标签的声明）
                            val nsStart = parser.getNamespaceCount(depth - 1)
                            // 获取当前深度总命名空间数量
                            val nsEnd = parser.getNamespaceCount(depth)
                            // 迭代当前标签上新声明的命名空间
                            for (pos in nsStart..<nsEnd) {
                                val prefix = parser.getNamespacePrefix(pos) // 前缀，如 "xliff"；默认命名空间为 null
                                val uri = parser.getNamespaceUri(pos) // URI，如 "urn:oasis:names:tc:xliff:document:1.2"
                                namespaces.add("$prefix=\"$uri\"")
                            }
                        }
                        "string" -> {
                            // 处理 <string> 标签
                            attributes.clear()
                            val name = parser.getAttributeValue(null, "name")
                            for (i in 0 until parser.attributeCount) {
                                val attrName = parser.getAttributeName(i)
                                val attrValue = parser.getAttributeValue(i)
                                attributes[attrName] = attrValue
                            }
                            if (name != null) {
                                val (value, isCData) = getTagContent(parser)
                                textResources.add(StringResource(file, name, value, isCData, attributes.toMap()))
                            }
                        }
                        "plurals" -> {
                            // 处理 <plurals> 标签
                            attributes.clear()
                            curPluralName = parser.getAttributeValue(null, "name")
                            for (i in 0 until parser.attributeCount) {
                                val attrName = parser.getAttributeName(i)
                                val attrValue = parser.getAttributeValue(i)
                                attributes[attrName] = attrValue
                            }
                            curPluralItems.clear()
                        }
                        "string-array" -> {
                            // 处理 <plurals> 标签
                            attributes.clear()
                            curArrayName = parser.getAttributeValue(null, "name")
                            for (i in 0 until parser.attributeCount) {
                                val attrName = parser.getAttributeName(i)
                                val attrValue = parser.getAttributeValue(i)
                                attributes[attrName] = attrValue
                            }
                            curArrayItems.clear()
                        }
                        "item" -> {
                            if (curPluralName != null) {
                                // 处理 plurals 内部的 <item> 标签
                                val quantity = parser.getAttributeValue(null, "quantity")
                                if (quantity != null) {
                                    val (value, _) = getTagContent(parser)
                                    curPluralItems[quantity] = value
                                }
                            } else if (curArrayName != null) {
                                // 处理 string-array 内部的 <item> 标签
                                val (value, _) = getTagContent(parser)
                                curArrayItems.add(value)
                            }
                        }
                        // 忽略命名空间标签如 xliff:g，直接处理其内容
                        else -> {
                            // 检查是否是命名空间标签（包含冒号）
                            if (tagName.contains(":")) {
                                val (value, _) = getTagContent(parser)
                                // 如果需要保留标签本身，可以在这里处理
                            }
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    val tagName = parser.name
                    if (tagName == "plurals" && curPluralName != null) {
                        // 完成 plurals 解析
                        textResources.add(PluralResource(file, curPluralName, curPluralItems.toMap(), attributes.toMap()))
                        curPluralName = null
                        curPluralItems.clear()
                    } else if (tagName == "string-array" && curArrayName != null) {
                        // 完成 string-array 解析
                        textResources.add(StringArrayResource(file, curArrayName, curArrayItems.toList(), attributes.toMap()))
                        curArrayName = null
                        curArrayItems.clear()
                    }
                }
            }
            eventType = parser.next()
        }

        return AndroidResource(namespaces, textResources)
    }

    /** 更新词条 */
    fun update(value: AbsAndroidTextResource, index: Int, file: PlatformFile): Boolean {
        val resource = parse(file)
        val texts = mutableListOf<AbsAndroidTextResource>()
        var replaced = false
        resource.texts.forEach {
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
        val newResource = AndroidResource(resource.namespaces, texts)
        writeResources(file, newResource)
        return true
    }

    /** 删除词条 */
    fun delete(name: String, file: PlatformFile): Boolean {
        val resource = parse(file)
        val texts = resource.texts.filter { it.getTextName() != name }
        val newResource = AndroidResource(resource.namespaces, texts)
        writeResources(file, newResource)
        return true
    }

    /** 将多语言资源写入到文件中 */
    private fun writeResources(file: PlatformFile, resource: AndroidResource) {
        val resources = resource.texts
        val namespaces = if (resource.namespaces.isEmpty()) "" else
            " ${resource.namespaces.joinToString(" ")}"
        val html = """
<?xml version="1.0" encoding="utf-8"?>
<resources$namespaces>
${resources.joinToString("\n") { it.getXmlNode() }}
</resources>
""".trimIndent()
        file.file.writeText(html)
    }

    /** 获取标签内容，处理文本、CDATA 和嵌套标签 */
    private fun getTagContent(parser: XmlPullParser): Pair<String, Boolean> {
        val stringBuilder = StringBuilder()
        var eventType = parser.nextToken()
        var isCData = false

        while (eventType != XmlPullParser.END_TAG) {
            when (eventType) {
                XmlPullParser.TEXT -> {
                    stringBuilder.append(parser.text)
                }
                XmlPullParser.CDSECT -> {
                    // 处理 CDATA 区块
                    stringBuilder.append("<![CDATA[${parser.text}]]>")
                    isCData = true
                }
                XmlPullParser.START_TAG -> {
                    // 处理嵌套标签（如<xliff:g>）
                    val tagName = parser.name
                    stringBuilder.append("<$tagName")

                    // 添加所有属性
                    for (i in 0 until parser.attributeCount) {
                        val attrName = parser.getAttributeName(i)
                        val attrValue = parser.getAttributeValue(i)
                        stringBuilder.append(" $attrName=\"$attrValue\"")
                    }

                    stringBuilder.append(">")

                    // 递归处理嵌套内容
                    val (nestedContent, _) = getTagContent(parser)
                    stringBuilder.append(nestedContent)

                    stringBuilder.append("</$tagName>")
                }
                XmlPullParser.ENTITY_REF -> {
                    stringBuilder.append("&${parser.name};")
                }
            }
            eventType = parser.nextToken()
        }

        return Pair(stringBuilder.toString(), isCData)
    }
}

// 使用示例
fun main() {
    try {
        // 从文件或资源获取输入流
        val file = PlatformFile("/Users/shouhwang/Desktop/repo/android/LeafNote/app/src/main/res/values/strings.xml")
        val resources = AndroidXmlParser.parse(file).texts
        resources.forEach {
            println(">>>> $it")
        }
        AndroidXmlParser.delete("user_action", file)
    } catch (e: XmlPullParserException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
