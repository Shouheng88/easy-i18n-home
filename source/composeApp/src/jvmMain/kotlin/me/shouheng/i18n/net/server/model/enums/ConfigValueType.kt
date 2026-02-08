package me.shouheng.i18n.net.server.model.enums

/** 配置值的类型  */
enum class ConfigValueType(val id: Int) {
    STRING(0),
    NUMBER(1),
    DOUBLE(2),
    BOOLEAN(3),
    JSON(4);

    companion object {
        @JvmStatic
        fun getTypeById(id: Int): ConfigValueType? = entries.firstOrNull { it.id == id }
    }
}