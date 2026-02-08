package me.shouheng.i18n.db.dao

import me.shouheng.i18n.Database
import me.shouheng.i18n.I18nProject
import me.shouheng.i18n.I18nProjectQueries
import me.shouheng.i18n.db.driver
import java.util.*

class I18nProjectDao {

    private val database = Database(driver)

    private val queries: I18nProjectQueries = database.i18nProjectQueries

    fun getAll(): List<I18nProject> {
        return queries.getAll().executeAsList()
    }

    fun getById(id: Long): I18nProject? {
        return queries.getById(id).executeAsOneOrNull()
    }

    fun insert(
        name: String,
        logo: String? = null,
        description: String? = null
    ): I18nProject? {
        val createdTime = Date().time
        queries.insert(name, logo, description, createdTime).value
        return queries.getByCreatedTime(createdTime).executeAsOneOrNull()
    }

    fun update(
        project: I18nProject,
        name: String? = null,
        logo: String? = null,
        description: String? = null
    ) {
        queries.update(
            name ?: project.name,
            logo ?: project.logo,
            description ?: project.description,
            project.id
        )
    }

    fun delete(id: Long) {
        queries.delete(id)
    }
}