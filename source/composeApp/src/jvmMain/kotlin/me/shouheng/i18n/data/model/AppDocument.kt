package me.shouheng.i18n.data.model

import easy_i18n.composeapp.generated.resources.*
import org.jetbrains.compose.resources.StringResource

/** 应用文档 */
object AppDocument {

    val items: List<Item> = listOf(
        Item(Res.string.doc_android, Res.string.doc_android_detail),
        Item(Res.string.doc_ios, Res.string.doc_ios_detail),
        Item(Res.string.doc_compose, Res.string.doc_compose_detail),
        Item(Res.string.doc_flutter, Res.string.doc_flutter_detail),
        Item(Res.string.doc_properties, Res.string.doc_properties_detail),
        Item(Res.string.doc_add, Res.string.doc_add_detail),
    )

    class Item(val titleRes: StringResource, val detailRes: StringResource)
}
