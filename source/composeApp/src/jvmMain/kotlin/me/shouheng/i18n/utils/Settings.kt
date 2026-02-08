package me.shouheng.i18n.utils

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences

private val prefs = Preferences.userRoot().node("me.shouheng.i18n.settings")

val settings: Settings = PreferencesSettings(prefs)
