package me.shouheng.i18n.data

object ErrorCode {

    const val UPDATE_TYPE_MISS = "-10001"
    const val UPDATE_TYPE_DIFF = "-10002"
    const val UPDATE_RESOURCE_PARSE = "-10003"
    const val UPDATE_WRITE = "-10004"

    const val DELETE_IO = "-20001"
    const val DELETE_TYPE_DIFF = "-20002"

    const val TRANSLATE_ERROR_MAX_ERROR_COUNT = "-30001"

    const val TRANSLATOR_TYPE_NOT_FOUND = "-40001"
    const val TRANSLATOR_TARGET_LANGUAGE_NOT_FOUND = "-40002"
    const val TRANSLATOR_TARGET_LANGUAGE_DEFAULT = "-40003"
    const val TRANSLATOR_TARGET_LANGUAGE_NOT_SUPPORT = "-40004"

    /** 设备已注册 */
    const val ERROR_APP_DEVICE_ALREADY_REGISTERED = "20031"
    const val ERROR_USER_TOKEN_EXPIRED = "10013"

}