package me.shouheng.i18n.net.server.model.base

import easy_i18n.composeapp.generated.resources.Res
import easy_i18n.composeapp.generated.resources.language
import me.shouheng.i18n.net.server.model.enums.DeviceType
import me.shouheng.i18n.utils.AppUtils
import me.shouheng.i18n.utils.DeviceIdFactory
import me.shouheng.i18n.utils.DeviceUtils
import me.shouheng.i18n.utils.JSON
import org.jetbrains.compose.resources.getString
import java.io.Serializable
import java.util.*

/**
 * The client info definition.
 *
 * @author [Shouheng.W](mailto:shouheng2015@gmail.com)
 * @version 1.0
 * @date 2020/11/5 14:38
 */
class ClientInfo private constructor(): Serializable {

    /** APP version etc.  */
    var version: String? = null

    /** Device time.  */
    var time: Date = Date()

    /** Device id.  */
    var id: String? = null

    /** Device type of client.  */
    var type: DeviceType? = null

    /** Device user id of client.  */
    private var userId: Long? = null

    /** Token of client.  */
    private var token: String? = null

    /** Language of client.  */
    var language: String? = null

    /** Timezone of client.  */
    var timezone: String? = null

    /** Ip address of client.  */
    var ip: String? = null

    /** Details about platform version, Android or iOS version, browser version etc.  */
    var platform: String? = null

    /** The network type of client.  */
    var networkType: String? = null

    /** The app channel */
    var channel: String? = null

    companion object {
        suspend fun newInstance() = ClientInfo().apply {
            this.version = AppUtils.getAppVersionName()
            this.id = DeviceIdFactory.get()
            this.language = getString(Res.string.language)
            this.timezone = TimeZone.getDefault().id
            this.platform = JSON.toJson(Platform.newInstance())
            this.networkType = DeviceUtils.getCurrentNetworkType().name
            this.type = DeviceUtils.getDeviceType()
            this.channel = "Desktop"
        }
    }
}
