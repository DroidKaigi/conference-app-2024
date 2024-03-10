package io.github.droidkaigi.confsched.designsystem.strings

sealed class AppStrings : Strings<AppStrings>(Bindings) {
    data object Retry : AppStrings()

    private object Bindings : StringsBindings<AppStrings>(
        Lang.Japanese to { item, _ ->
            when (item) {
                Retry -> "リトライ"
            }
        },
        Lang.English to { item, bindings ->
            when (item) {
                Retry -> "Retry"
            }
        },
        default = Lang.Japanese,
    )
}
