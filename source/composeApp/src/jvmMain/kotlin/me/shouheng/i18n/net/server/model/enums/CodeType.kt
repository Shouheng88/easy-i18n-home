package me.shouheng.i18n.net.server.model.enums

/**
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2021/1/11 22:21
 */
enum class CodeType(val id: Int) {
    // Verification code type for registering
    REGISTER(0),  // Verification code type for login
    LOGIN(1),  // Verification code type for change password
    CHANGE_PASSWORD(2);

    companion object {
        fun getTypeById(id: Int): CodeType? = entries.firstOrNull { id == it.id }
    }
}