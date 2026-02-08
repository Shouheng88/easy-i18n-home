package me.shouheng.i18n.utils.extension

import me.shouheng.i18n.I18nPath
import me.shouheng.i18n.data.model.I18nResourceType

/** 获取资源的类型 */
fun I18nPath?.getResourceType(): I18nResourceType? {
    val resourceType = this?.resourceType ?: return null
    return I18nResourceType.from(resourceType.toInt())
}
