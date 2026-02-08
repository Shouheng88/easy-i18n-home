package me.shouheng.i18n.data

/** 常量 */
object Const {

    const val APP_NAME_ENGLISH = "Easy I18n"

    const val APP_ID = 77L
    const val APP_CODE = "i18n"

    const val REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"

    const val CODE_SEND_TIME_LIMIT = 60_000 // ms，间隔多久才能发送第二次验证码

    const val APP_CONFIG_ITEM_PRIVACY_VERSION = "common#privacy_service_version"
    const val APP_CONFIG_ITEM_VERSION_INFO = "common#version_info"
    const val APP_CONFIG_ITEM_VERSION_INFO_FORCE_UPGRADE = "forceUpgrade"
    const val APP_CONFIG_ITEM_VERSION_INFO_LATEST_VERSION = "latestVersion"
    const val APP_CONFIG_ITEM_UPGRADE_DESCRIPTION = "common#upgrade_description"
    const val APP_CONFIG_ITEM_APP_DOWNLOAD_LINK = "common#app_download_link"
    const val APP_CONFIG_ITEM_APP_HOMEPAGE_MESSAGE = "common#app_homepage_message"

    /* 非会员能创建的 项目 的最大数量 */
    const val MAX_PROJECT_COUNT_NONE_PREMIUM = 1
    /* 非会员能创建的 路径 的最大数量 */
    const val MAX_FOLDER_COUNT_NONE_PREMIUM = 1
}