package me.shouheng.i18n.utils

import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.language
import me.shouheng.i18n.net.server.model.enums.DeviceType
import org.jetbrains.compose.resources.getString
import java.net.NetworkInterface
import java.net.SocketException

/** 获取设备信息 */
object DeviceUtils {

    /**
     * 网络类型枚举
     */
    enum class NetworkType {
        WIFI,      // 无线局域网
        ETHERNET,  // 以太网（有线）
        MOBILE,    // 移动数据（如手机热点共享）
        UNKNOWN    // 未知类型
    }

    /** 获取设备类型 */
    fun getDeviceType(): DeviceType {
        val osName = getOSName().lowercase()
        return when {
            osName.contains("win") -> DeviceType.PC // 匹配 Windows（如 "windows 10", "windows 11"）
            osName.contains("mac") -> DeviceType.Mac // 匹配 macOS（如 "mac os x", "macos"）
            osName.contains("linux") -> DeviceType.Linux // 匹配 Linux（如 "linux", "ubuntu"）
            else -> DeviceType.UNKNOWN  // 未知系统
        }
    }

    /** 系统名称 */
    fun getOSName(): String = System.getProperty("os.name", "unknown")

    /** 操作系统架构 */
    fun getOSArch(): String = System.getProperty("os.arch")

    /** 系统版本 */
    fun getOSVersion(): String = System.getProperty("os.version", "unknown")

    /** 获取Java版本 */
    fun getJavaVersion(): String = System.getProperty("java.version", "unknown")

    /** 获取全部 mac 地址 */
    fun getAllValidMacAddresses(): Map<String, String> {
        val macMap = mutableMapOf<String, String>()
        try {
            // 遍历所有网络接口
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()

                // 过滤回环接口和未启用的接口
                if (networkInterface.isLoopback || !networkInterface.isUp) {
                    continue
                }

                // 获取MAC地址字节数组（可能为null，如部分虚拟接口）
                val macBytes = networkInterface.hardwareAddress ?: continue

                // 转换为标准格式（XX:XX:XX:XX:XX:XX）
                val macAddress = formatMacBytes(macBytes)
                if (macAddress.isNotBlank()) {
                    macMap[networkInterface.displayName] = macAddress
                }
            }
        } catch (e: SocketException) {
            println("获取MAC地址失败：${e.message}")
        }
        return macMap
    }

    /**
     * 获取第一个有效MAC地址（通常为优先启用的网卡）
     * @return 格式化的MAC地址，无有效地址时返回空字符串
     */
    fun getFirstValidMacAddress(): String {
        return getAllValidMacAddresses().values.firstOrNull() ?: ""
    }

    /** 大陆 */
    suspend fun isChineseMainland(): Boolean = getString(Res.string.language) == "zh_CN"

    /**
     * 将MAC地址字节数组转换为标准字符串格式（XX:XX:XX:XX:XX:XX）
     */
    private fun formatMacBytes(macBytes: ByteArray): String {
        if (macBytes.size != 6) {
            return "" // MAC地址固定为6字节，不符合则视为无效
        }
        return buildString {
            macBytes.forEachIndexed { index, byte ->
                // 转换为两位十六进制，不足补0
                append(String.format("%02X", byte))
                // 最后一个字节后不加分隔符
                if (index != macBytes.lastIndex) {
                    append(":")
                }
            }
        }
    }

    /** 获取当前活跃网络的主要类型（优先取已连接的接口） */
    fun getCurrentNetworkType(): NetworkType {
        return try {
            // 遍历所有活跃网络接口
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val ni = interfaces.nextElement()
                if (ni.isLoopback || !ni.isUp) continue  // 过滤无效接口

                // 通过接口名称特征判断类型
                val displayName = ni.displayName.lowercase()
                return when {
                    // Wi-Fi 接口名称通常包含 "wifi"、"wlan" 等关键词
                    displayName.contains("wifi")
                            || displayName.contains("wlan") -> NetworkType.WIFI
                    // 以太网接口通常包含 "ethernet"、"eth"、"en"（macOS）等
                    displayName.contains("ethernet")
                            || displayName.contains("eth")
                            || displayName.contains("en") -> NetworkType.ETHERNET
                    // 移动数据共享（如手机热点）可能包含 "mobile"、"tethering" 等
                    displayName.contains("mobile")
                            || displayName.contains("tethering") -> NetworkType.MOBILE
                    else -> continue  // 继续检查下一个接口
                }
            }
            NetworkType.UNKNOWN  // 无活跃接口
        } catch (e: SocketException) {
            e.printStackTrace()
            NetworkType.UNKNOWN
        }
    }
}

fun main() {
    println(DeviceUtils.getFirstValidMacAddress())
    println(DeviceUtils.getCurrentNetworkType())
}