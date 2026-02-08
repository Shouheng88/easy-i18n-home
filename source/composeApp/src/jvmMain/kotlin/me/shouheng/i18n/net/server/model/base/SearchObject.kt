package me.shouheng.i18n.net.server.model.base

import java.io.Serializable

/**
 * 用来查询的对象
 *
 * @author [WngShhng](mailto:shouheng2015@gmail.com)
 * @date 2019-08-01  20:31
 */
open class SearchObject : Serializable {

    var currentPage: Int? = null

    var pageSize: Int? = null

    val sorts = mutableListOf<Sort>()

    /** 添加排序条件 */
    fun addSort(sort: Sort) {
        sorts.add(sort)
    }
}
