package me.shouheng.i18n.db.dao

import me.shouheng.i18n.Database
import me.shouheng.i18n.I18nWord
import me.shouheng.i18n.I18nWordQueries
import me.shouheng.i18n.data.model.I18nWordModel
import me.shouheng.i18n.db.driver
import java.util.*

class I18nWordDao {

    private val database = Database(driver)

    private val queries: I18nWordQueries = database.i18nWordQueries

    fun getAll(): List<I18nWord> {
        return queries.getAll().executeAsList()
    }

    fun insert(name: String, path: String, description: String? = null): Long {
        return queries.insert(name, path, description, Date().time, Date().time).value
    }

    fun getOf(path: String): List<I18nWord> {
        return queries.getOf(path).executeAsList()
    }

    /** 更新/插入 单词的描述 */
    fun update(word: I18nWordModel, description: String) {
        val dto = word.dto
        if (dto == null) {
            insert(word.name, word.path.path, description)
        } else {
            queries.updateDescription(description, Date().time, dto.id)
        }
    }

    /** 根据路径和名称获取词条 */
    fun getOf(path: String, name: String): I18nWord? {
        return queries.getBy(path, name).executeAsOneOrNull()
    }

    /** 记录更新时间：用于方便用户查找最近修改的词条 */
    fun markUpdated(word: I18nWordModel, description: String? = null) {
        val dto = getOf(word.path.path, word.name)
        if (dto == null) {
            insert(word.name, word.path.path, description)
        } else {
            queries.markUpdatedTime(Date().time, dto.id)
        }
    }

    fun delete(id: Long) {
        queries.delete(id)
    }
}