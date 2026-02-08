package me.shouheng.i18n.utils

/** 应用信息 */
object AppUtils {

    /** 获取版本号 */
    fun getAppVersionName(): String = System.getProperty("build_version") ?: "1.0.0"

    /** 构建的信息 */
    fun getBuild(): String = System.getProperty("build") ?: "1"

    /** 获取环境信息 */
    fun getEnv(): String? = System.getProperty("env")
}

/** 是否可能为调试版本（重要的逻辑不要依赖这个方法） */
inline fun isPossibleDebug(): Boolean {
    val env = AppUtils.getEnv()
    // 生产环境 env 一定存在，因此可以快速判断出 false，不会影响性能
    if (env == "release") {
        return false
    }
    if (env == "debug") {
        return true
    }
    return false
}
