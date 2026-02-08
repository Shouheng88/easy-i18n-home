package me.shouheng.i18n.data

class Event(
    val name: String,
    val data: Any? = null
) {
    companion object {
        const val EVENT_NAME_TO_PAGE = "__event_nav_to_page__"
        const val EVENT_THEME_CHANGED = "__event_theme_changed__"
        const val EVENT_SESSION_EXPIRED = "__event_user_session_expired__"
        const val EVENT_PREMIUM_REQUIRE = "__event_premium_required__"
    }
}