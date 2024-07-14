package io.github.droidkaigi.confshed.about.strings

import io.github.droidkaigi.confsched.designsystem.strings.Lang
import io.github.droidkaigi.confsched.designsystem.strings.Strings
import io.github.droidkaigi.confsched.designsystem.strings.StringsBindings

sealed class AboutStrings : Strings<AboutStrings>(Bindings) {
    data object Description : AboutStrings()
    data object DateTitle : AboutStrings()
    data object DateDescription : AboutStrings()
    data object PlaceTitle : AboutStrings()
    data object PlaceDescription : AboutStrings()
    class PlaceLink(
        val url: String = "https://goo.gl/maps/vv9sE19JvRjYKtSP9",
    ) : AboutStrings()
    data object CreditsTitle : AboutStrings()
    data object Staff : AboutStrings()
    data object Contributor : AboutStrings()
    data object Sponsor : AboutStrings()
    data object OthersTitle : AboutStrings()
    data object CodeOfConduct : AboutStrings()
    data object License : AboutStrings()
    data object PrivacyPolicy : AboutStrings()
    data object AppVersion : AboutStrings()
    data object LicenceDescription : AboutStrings()

    private object Bindings : StringsBindings<AboutStrings>(
        Lang.Japanese to { item, _ ->
            when (item) {
                Description -> "DroidKaigiはAndroid技術情報の共有とコミュニケーションを目的に開催されるエンジニアが主役のAndroidカンファレンスです。"
                DateTitle -> "日時"
                DateDescription -> "2024.09.11(水) 〜 13(金)"
                PlaceTitle -> "場所"
                PlaceDescription -> "ベルサール渋谷ガーデン"
                is PlaceLink -> "地図を見る"
                CreditsTitle -> "Credits"
                Staff -> "スタッフ"
                Contributor -> "コントリビューター"
                Sponsor -> "スポンサー"
                OthersTitle -> "Others"
                CodeOfConduct -> "行動規範"
                License -> "ライセンス"
                PrivacyPolicy -> "プライバシーポリシー"
                AppVersion -> "アプリバージョン"
                LicenceDescription -> "The Android robot is reproduced or modified from work created and shared by Google and used according to terms described in the Creative Commons 3.0 Attribution License."
            }
        },
        Lang.English to { item, bindings ->
            when (item) {
                Description -> "DroidKaigi is a conference tailored for Android developers."
                DateTitle -> "Date"
                DateDescription -> "2024.09.11(Wed) - 13(Fri)"
                PlaceTitle -> "Place"
                PlaceDescription -> "Bellesalle Shibuya Garden"
                is PlaceLink -> "View Map"
                CreditsTitle -> bindings.defaultBinding(item, bindings)
                Staff -> "Staff"
                Contributor -> "Contributor"
                Sponsor -> "Sponsor"
                OthersTitle -> bindings.defaultBinding(item, bindings)
                CodeOfConduct -> "Code Of Conduct"
                License -> "License"
                PrivacyPolicy -> "Privacy Policy"
                AppVersion -> "App Version"
                LicenceDescription -> bindings.defaultBinding(item, bindings)
            }
        },
        default = Lang.Japanese,
    )
}
