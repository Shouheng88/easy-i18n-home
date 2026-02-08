package me.shouheng.i18n.utils

import me.shouheng.i18n.utils.extension.md5
import java.util.TimeZone
import java.util.UUID
import kotlin.random.Random

/** 设备唯一 ID，这种方式生成的设备唯一 ID 每次卸载再重新安装之后会更新 */
object DeviceIdFactory {

    private var deviceUniqueId: String = ""

    /** 获取设备唯一 ID */
    @JvmStatic
    fun get(): String {
        if (deviceUniqueId.isEmpty()) {
            prepare()
        }
        return deviceUniqueId
    }

    /** 准备设备唯一 ID */
    @JvmStatic
    fun prepare() {
        val key = "__device_unique_id__".md5("unique")
        val deviceUniqueId = settings.getString(key, "")
        if (deviceUniqueId.isEmpty()) {
            val uniqueId = generateDeviceUniqueId()
            settings.putString(key, uniqueId)
            this.deviceUniqueId = uniqueId
        } else {
            this.deviceUniqueId = deviceUniqueId
        }
    }

    /** 生成设备唯一 ID */
    private fun generateDeviceUniqueId(): String {
        val timestamp = System.currentTimeMillis()
        val random = UUID.randomUUID()
        val rn1 = Random.nextLong()
        val rn2 = Random.nextLong()
        val timeZoneId = TimeZone.getDefault().id
        val version = AppUtils.getAppVersionName()
        return UUID.nameUUIDFromBytes(
            "${timestamp}_${random}_${timeZoneId}_${rn1}_${rn2}_${version}".toByteArray()
        ).toString()
    }
}

fun main() {
    println(DeviceIdFactory.get())
}
