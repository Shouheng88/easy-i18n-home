package me.shouheng.i18n.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import me.shouheng.i18n.Database
import me.shouheng.i18n.data.Const
import me.shouheng.i18n.db.dao.I18nPathDao
import me.shouheng.i18n.db.dao.I18nProjectDao
import me.shouheng.i18n.db.dao.I18nWordDao
import java.io.File
import java.util.*

val driver: SqlDriver by lazy {
    // 用户主目录（如 macOS: /Users/用户名，Windows: C:\Users\用户名）
    val userHome = System.getProperty("user.home")
    // 数据库存储目录（如 userHome/.myapp/databases/）
    val dbDir = File(userHome, ".${Const.APP_NAME_ENGLISH}/database")
    // 确保目录存在（不存在则创建）
    dbDir.mkdirs()
    // 数据库文件绝对路径
    val dbFile = File(dbDir, "data.db")
    return@lazy JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}", Properties(), Database.Schema)
}

/** 数据库文件 */
val databaseFile = File(System.getProperty("user.home"), ".${Const.APP_NAME_ENGLISH}/database/data.db")

object DB {

    val i18nProjectDao = I18nProjectDao()

    val i18nPathDao = I18nPathDao()

    val i18nWordDao = I18nWordDao()
}

fun main() {
    println(System.getProperty("user.home"))
}