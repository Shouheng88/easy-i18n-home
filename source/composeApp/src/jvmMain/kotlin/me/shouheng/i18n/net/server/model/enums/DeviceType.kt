package me.shouheng.i18n.net.server.model.enums

/** 设备类型 */
enum class DeviceType(val id: Int) {
    /** 未知 */
    UNKNOWN(        0),
    /** Android */
    ANDROID(        1),
    /** iOS */
    iOS(            2),
    /** 前端 */
    FE(             3),
    /** WeChat little program */
    WX(             4),
    /** Android Tablet */
    ANDROID_TABLET( 5),
    /** Android Watch */
    ANDROID_WATCH(  6),
    /** iPAD */
    iPAD(           7),
    /** MacOS */
    Mac(            8),
    /** PC, Windows */
    PC(             9),
    /** Linux */
    Linux(         10),
    ;

    companion object {
        @JvmStatic
        fun getTypeById(id: Int): DeviceType = DeviceType.entries.firstOrNull { it.id == id } ?: UNKNOWN
    }
}
