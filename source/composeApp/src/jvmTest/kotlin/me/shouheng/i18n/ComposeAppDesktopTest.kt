package me.shouheng.i18n

import me.shouheng.i18n.data.model.I18nLanguage
import me.shouheng.i18n.data.model.I18nResourceType
import me.shouheng.i18n.manager.LanguageManager
import kotlin.test.Test
import kotlin.test.assertEquals

class
ComposeAppDesktopTest {

    @Test
    fun example() {
        assertEquals(3, 1 + 2)
    }

    @Test
    fun testParseLanguage() {
        assertEquals("zh", LanguageManager.parseLanguageFrom("zh", I18nResourceType.AndroidXML))
        assertEquals("zh", LanguageManager.parseLanguageFrom("zh-rCN", I18nResourceType.ComposeMultiplatformXML))
        assertEquals("zh", LanguageManager.parseLanguageFrom("zh-CN", I18nResourceType.IOSXCStrings))
        assertEquals("zh", LanguageManager.parseLanguageFrom("zh", I18nResourceType.IOSXCStrings))
        assertEquals("zh", LanguageManager.parseLanguageFrom("zh_CN", I18nResourceType.JavaProperties))
        assertEquals("zh", LanguageManager.parseLanguageFrom("zh", I18nResourceType.JavaProperties))
    }

    @Test
    fun testI18nLanguage() {
        // 测试 Android 重复性
        val set = mutableSetOf<String>()
        I18nLanguage.entries.forEach {
            it.android.forEach { name ->
                if (set.contains(name)) {
                    throw IllegalStateException("found duplicate: $name")
                } else {
                    set.add(name)
                }
            }
        }
        // 测试 ios 重复性
        set.clear()
        I18nLanguage.entries.forEach {
            it.ios.forEach { name ->
                if (set.contains(name)) {
                    throw IllegalStateException("found duplicate: ${name}")
                } else {
                    set.add(name)
                }
            }
        }
        // 测试 java properties 重复性
        set.clear()
        I18nLanguage.entries.forEach {
            it.properties.forEach { name ->
                if (set.contains(name)) {
                    throw IllegalStateException("found duplicate: $name")
                } else {
                    set.add(name)
                }
            }
        }
        assert(true)
    }
}