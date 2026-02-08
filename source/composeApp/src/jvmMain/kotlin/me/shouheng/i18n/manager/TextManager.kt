package me.shouheng.i18n.manager

/** 文本处理 */
object TextManager {

    const val JAVA_PROPERTIES_DEFAULT_ENCODING = "UTF-8"

    /** 解析数组 */
    fun parseArray(text: String): List<String>? {
        val lines = text.lines()
        val items = mutableListOf<String>()
        for (line in lines) {
            val trimmed = line.trimStart()
            if (!trimmed.startsWith("- ")) {
                // 不合法
                return null
            }
            val text = trimmed.removePrefix("- ").trimStart()
            items.add(text)
        }
        return items
    }

    /** 检测是否是复数资源 */
    fun isPlural(text: String): Boolean {
        val lines = text.lines()
        for (line in lines) {
            val trimmed = line.trimStart()
            if (!trimmed.startsWith("- ")) {
                return false
            }
            val text = trimmed.removePrefix("- ").trimStart()
            val index = text.indexOf(':')
            if (index < 0) {
                return false
            }
        }
        return true
    }

    /** 检测是否是数组资源 */
    fun isArray(text: String): Boolean {
        val lines = text.lines()
        for (line in lines) {
            val trimmed = line.trimStart()
            if (!trimmed.startsWith("- ")) {
                return false
            }
        }
        return true
    }

    /** 解析复数资源 */
    fun parsePlural(text: String): Map<String, String>? {
        val lines = text.lines()
        val items = mutableMapOf<String, String>()
        for (line in lines) {
            val trimmed = line.trimStart()
            if (!trimmed.startsWith("- ")) {
                // 不合法
                return null
            }
            val text = trimmed.removePrefix("- ").trimStart()
            val index = text.indexOf(':')
            if (index < 0) {
                // 不合法
                return null
            }
            val key = text.substring(0, index).trim()
            val value = text.substring(index+1).trimStart()
            items[key] = value
        }
        return items
    }

    /** 字符串首字母大写 */
    fun upperFirstCase(text: String): String {
        if (text.isEmpty()) return text
        // 先判断 plural
        val isPlural = isPlural(text)
        if (isPlural) return upperPluralFirstCase(text)
        // 再判断 array
        val isArray = isArray(text)
        if (isArray) return upperArrayFirstCase(text)
        // 最后判断普通字符串
        return text[0].uppercase() + text.substring(1)
    }

    /** 数组首字母大写 */
    private fun upperArrayFirstCase(text: String): String {
        val array = parseArray(text) ?: return text
        val newArray = mutableListOf<String>()
        array.forEach {
            if (it.isNotEmpty()) {
                newArray.add(it[0].uppercase() + it.substring(1))
            } else {
                newArray.add(it)
            }
        }
        return newArray.joinToString("\n") { "- $it" }
    }

    /** 复数首字母大写 */
    private fun upperPluralFirstCase(text: String): String {
        val map = parsePlural(text) ?: return text
        val newMap = mutableMapOf<String, String>()
        map.entries.forEach {
            val key = it.key
            val value = it.value
            if (value.isNotEmpty()) {
                newMap[key] = value[0].uppercase() + value.substring(1)
            } else {
                newMap[key] = value
            }
        }
        return newMap.entries.joinToString("\n") { "- ${it.key}: ${it.value}" }
    }

}