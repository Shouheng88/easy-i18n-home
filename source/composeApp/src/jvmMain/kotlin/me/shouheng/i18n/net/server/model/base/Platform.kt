package me.shouheng.i18n.net.server.model.base

import me.shouheng.i18n.utils.DeviceUtils
import java.lang.reflect.Modifier

/** 平台/设备属性 */
class Platform private constructor (
    var display: String? = DeviceUtils.getOSName() + " " + DeviceUtils.getOSVersion() + " " + DeviceUtils.getOSArch(),
    var device: String? = null,
    var sdkName: String? = DeviceUtils.getJavaVersion(),
    var sdkCode: Int? = null,
    var model: String? = null,
    var release: String? = null,
    var mac: String? = null,
    var rooted: Boolean? = null
) {

    /** 获取文本信息 */
    fun toText(): String = toMap().entries.joinToString("\n") { "${it.key}: ${it.value}" }

    fun toMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        Platform::class.java.declaredFields.filter {
            // 排除静态字段
            !Modifier.isStatic(it.modifiers)
        }.forEach {
            map[it.name] = "${it.get(this)}"
        }
        return map
    }

    companion object {

        private var isPrivacyAgreed = false

        @JvmStatic
        fun setPrivacyAgreed(agreed: Boolean) {
            this.isPrivacyAgreed = agreed
        }

        @JvmStatic
        fun newInstance(): Platform {
            return if (isPrivacyAgreed) {
                Platform(mac = DeviceUtils.getFirstValidMacAddress())
            } else {
                Platform()
            }
        }
    }
}
