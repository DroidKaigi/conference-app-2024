package io.github.droidkaigi.confsched.main.strings

import io.github.droidkaigi.confsched.designsystem.strings.Lang
import io.github.droidkaigi.confsched.designsystem.strings.Strings
import io.github.droidkaigi.confsched.designsystem.strings.StringsBindings

sealed class MainStrings : Strings<MainStrings>(Bindings) {
    data object Timetable : MainStrings()
    data object EventMap : MainStrings()
    data object Achievements : MainStrings()
    data object About : MainStrings()
    data object Contributors : MainStrings()
    class Time(val hours: Int, val minutes: Int) : MainStrings()

    private object Bindings : StringsBindings<MainStrings>(
        Lang.Japanese to { item, _ ->
            when (item) {
                Timetable -> "Timetable"
                EventMap -> "Event Map"
                Achievements -> "Achievements"
                About -> "About"
                Contributors -> "Contributors"
                is Time -> "${item.hours}時${item.minutes}分"
            }
        },
        Lang.English to { item, bindings ->
            when (item) {
                Timetable -> "Timetable"
                EventMap -> "Event Map"
                Achievements -> "Achievements"
                About -> "About"
                Contributors -> "Contributors"
                is Time -> "${item.hours}:${item.minutes}"
            }
        },
        default = Lang.English,
    )
}
