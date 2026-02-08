package me.shouheng.i18n.data.model

/** 单词查询条件 */
data class WordQuery(
    var keyword: String? = null,
    var nameOrder: DisplayOrder = DisplayOrder.NONE,
    var rateOrder: DisplayOrder = DisplayOrder.NONE,
    var updatedOrder: DisplayOrder = DisplayOrder.NONE,
) {
    /** 处理结果 */
    fun handle(words: List<I18nWordModel>): List<I18nWordModel> {
        var list = words
        val keyword = this.keyword
        if (keyword != null) {
            list = list.filter { it.name.contains(keyword) }
        }
        list = when(nameOrder) {
            DisplayOrder.NONE -> list
            DisplayOrder.ORDER -> list.sortedBy { it.name }
            DisplayOrder.REVERSED -> list.sortedByDescending { it.name }
        }
        list = when(rateOrder) {
            DisplayOrder.NONE -> list
            DisplayOrder.ORDER -> list.sortedBy { it.percentage }
            DisplayOrder.REVERSED -> list.sortedBy { -it.percentage }
        }
        list = when(updatedOrder) {
            DisplayOrder.NONE -> list
            DisplayOrder.ORDER -> list.sortedBy { it.dto?.updatedTime?:0 }
            DisplayOrder.REVERSED -> list.sortedBy { -(it.dto?.updatedTime?:0) }
        }
        return list
    }

    /** 搜索关键字 */
    fun ofKeyword(keyword: String?) {
        this.keyword = keyword
        nameOrder = DisplayOrder.NONE
        rateOrder = DisplayOrder.NONE
        updatedOrder = DisplayOrder.NONE
    }

    /** 修改名称的顺序 */
    fun nextNameOrder() {
        nameOrder = if (keyword != null) {
            DisplayOrder.NONE
        } else {
            nameOrder.next()
        }
        keyword = null
        rateOrder = DisplayOrder.NONE
        updatedOrder = DisplayOrder.NONE
    }

    /** 修改翻译比的顺序 */
    fun nextRateOrder() {
        keyword = null
        nameOrder = DisplayOrder.NONE
        rateOrder = rateOrder.next()
        updatedOrder = DisplayOrder.NONE
    }

    /** 修改更新时间的顺序 */
    fun nextUpdatedOrder() {
        keyword = null
        nameOrder = DisplayOrder.NONE
        rateOrder = DisplayOrder.NONE
        updatedOrder = updatedOrder.next()
    }
}