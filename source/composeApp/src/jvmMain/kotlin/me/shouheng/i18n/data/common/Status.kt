package me.shouheng.i18n.data.common

enum class Status(val id: Int) {
    /** Status success  */
    SUCCESS(0),
    /** Status failed  */
    FAILED(1),
    /** Status loading  */
    LOADING(2),
    /** Status progress changed  */
    PROGRESS(3)
}