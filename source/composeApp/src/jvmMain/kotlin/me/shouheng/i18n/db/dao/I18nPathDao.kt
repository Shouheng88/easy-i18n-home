package me.shouheng.i18n.db.dao

import me.shouheng.i18n.Database
import me.shouheng.i18n.I18nPath
import me.shouheng.i18n.I18nPathQueries
import me.shouheng.i18n.data.model.I18nPlatform
import me.shouheng.i18n.data.model.I18nResourceType
import me.shouheng.i18n.db.driver
import java.util.*

class I18nPathDao {

    private val database = Database(driver)

    private val queries: I18nPathQueries = database.i18nPathQueries

    fun getAll(): List<I18nPath> {
        return queries.getAll().executeAsList()
    }

    fun insert(
        projectId: Long,
        platform: I18nPlatform,
        path: String,
        type: I18nResourceType
    ): Long {
        return queries.insert(
            projectId,
            platform.id.toLong(),
            path,
            type.id.toLong(),
            null,
            Date().time
        ).value
    }

    fun getOf(projectId: Long, platform: I18nPlatform): List<I18nPath> {
        return queries.getOf(projectId, platform.id.toLong()).executeAsList()
    }

    fun update(id: Long, language: String) {
        queries.updateSourceLanguage(language, id)
    }

    fun updateEncoding(id: Long, encoding: String) {
        queries.updateEncoding(encoding, id)
    }

    fun getById(id: Long): I18nPath? {
        return queries.getById(id).executeAsOneOrNull()
    }

    fun delete(id: Long) {
        queries.delete(id)
    }
}