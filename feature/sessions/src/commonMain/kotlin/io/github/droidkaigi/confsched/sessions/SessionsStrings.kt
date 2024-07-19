package io.github.droidkaigi.confsched.sessions

import io.github.droidkaigi.confsched.designsystem.strings.Lang
import io.github.droidkaigi.confsched.designsystem.strings.Strings
import io.github.droidkaigi.confsched.designsystem.strings.StringsBindings

// FIXME: Use multiplatform resource instead of this
sealed class SessionsStrings : Strings<SessionsStrings>(Bindings) {
    data object ScheduleIcon : SessionsStrings()
    data object UserIcon : SessionsStrings()
    data object EventDay : SessionsStrings()
    data object ErrorIcon : SessionsStrings()

    private object Bindings : StringsBindings<SessionsStrings>(
        Lang.Japanese to { item, _ ->
            when (item) {
                ScheduleIcon -> "スケジュールアイコン"
                UserIcon -> "ユーザーアイコン"
                EventDay -> "開催日"
                ErrorIcon -> "エラーアイコン"
            }
        },
        Lang.English to { item, _ ->
            when (item) {
                ScheduleIcon -> "Schedule icon"
                UserIcon -> "User icon"
                EventDay -> "Day"
                ErrorIcon -> "Error Icon"
            }
        },
        default = Lang.Japanese,
    )
}
