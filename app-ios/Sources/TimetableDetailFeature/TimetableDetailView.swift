import SwiftUI
import ComposableArchitecture

public struct TimetableDetailView: View {
    private let store: StoreOf<TimetableDetailReducer>

    public var body: some View {
        VStack {
            ScrollView {
                sessionDetail
                    .padding(.horizontal, 16)
                
                Divider().background(Color(.outlineVariant))

                targetUser
                    .padding(16)

                Divider().background(Color(.outlineVariant))
                
                speaker
                    .padding(16)
                
                Divider().background(Color(.outlineVariant))
                
                archive
                    .padding(16)
            }
            
            footer
        }
        .background(Color(.background))
        .frame(maxWidth: .infinity)
    }
    
    var footer: some View {
        HStack(spacing: 8) {
            Button {
                // do something
            } label: {
                Group {
                    Image(.icShare)
                }
                .frame(width: 40, height: 40)
            }
            Button {
                // do something
            } label: {
                Group {
                    Image(.icAddCalendar)
                }
                .frame(width: 40, height: 40)
            }
            Spacer()
            Button {
                // do something
            } label: {
                Group {
                    Image(.icFavorite)
                }
                .frame(width: 56, height: 56)
                .background(Color(.secondaryContainer))
                .clipShape(RoundedRectangle(cornerRadius: 16))
            }
        }
        .frame(height: 80)
        .frame(maxWidth: .infinity)
        .padding(.horizontal, 16)
        .background(Color(.surfaceContainer))
    }
    
    var sessionDetail: some View {
        VStack(alignment: .leading, spacing: 20) {
            Text("DroidKaigiアプリで見るアーキテクチャの変遷")
                .font(.title)
                .foregroundStyle(Color(.surfaceVariant))
            VStack(spacing: 16) {
                InformationRow(
                    icon: Image(.icSchedule),
                    title: String(localized: "TimeTableDetailDate", bundle: .module),
                    content: "2023.09.14 / 11:20 ~ 12:00 (40分)"
                )
                InformationRow(
                    icon: Image(.icLocationOn),
                    title: String(localized: "TimeTableDetailLocation", bundle: .module),
                    content: "Arctic Fox (1F)"
                )
                InformationRow(
                    icon: Image(.icLanguage),
                    title: String(localized: "TimeTableDetailLanguage", bundle: .module),
                    content: "日本語(英語通訳あり)"
                )
                InformationRow(
                    icon: Image(.icCategory),
                    title: String(localized: "TimeTableDetailCategory", bundle: .module),
                    content: "Jetpack Compose"
                )
            }
            .padding(16)
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(Color(.onSurface), lineWidth: 1)
            )
            
            SessionDescriptionView(content: "Kotlin Coroutinesは非同期処理をシンプルに記述できるKotlinの言語機能です。実験的な機能としてこれまでも提供されてきましたがKotlin 1.3で正式にリリース予定です。Androidの誕生から10年たちアプリの利用シーンが増えたKotlin Coroutinesは非同期処理をシンプルに記述できるKotlinの言語機能です。実験的な機能としてこれまでも提供されてきましたがKotlin 1.3で正式にリリース予定です。Androidの誕生から10年たちアプリの利用シーンが増えた.")
                .padding(.bottom, 24)
        }
    }
    
    var targetUser: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text(String(localized: "TimeTableDetailApplicants", bundle: .module))
                .font(.title3)
                .foregroundStyle(Color(.surfaceVariant))

            Text("手を動かして実践してひとへのヒントとなります\nCoroutinesを使いたいと感じているひと\nよりモダンで効率的なAndroidアプリ開発に興味があるひと\n新しいパラダイムをいち早く知りたいひと\nアプリ開発が複雑だと感じるひと\nアプリ開発経験を前提としてあったほうが楽しめます")
                .font(.callout)
                .foregroundStyle(Color(.surfaceVariant))
        }
    }
    
    var speaker: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text(String(localized: "TimeTableDetailSpeaker", bundle: .module))
                .font(.title3)
                .foregroundStyle(Color(.surfaceVariant))

            HStack {
                Image(.avatar)
                    .padding(.trailing, 24)
                Text("name")
                    .font(.callout)
                    .foregroundStyle(Color(.onSurface))
                Spacer()
            }
        }
    }
    
    var archive: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text(String(localized: "TimeTableDetailArchive", bundle: .module))
                .font(.title3)
                .foregroundStyle(Color(.surfaceVariant))

            HStack {
                Button {
                    // do something
                } label: {
                    VStack {
                        Label(
                            title: { Text(String(localized: "TimeTableDetailSlide", bundle: .module)).foregroundStyle(Color(.onPrimary)) },
                            icon: { Image(.icDocument) }
                        )
                    }
                    .frame(height: 40)
                    .frame(maxWidth: .infinity)
                    .background(Color(.button))
                    .clipShape(Capsule())
                }
                Button {
                    // do something
                } label: {
                    VStack {
                        Label(
                            title: { Text(String(localized: "TimeTableDetailVideo", bundle: .module)).foregroundStyle(Color(.onPrimary)) },
                            icon: { Image(.icPlay) }
                        )
                    }
                    .frame(height: 40)
                    .frame(maxWidth: .infinity)
                    .background(Color(.button))
                    .clipShape(Capsule())
                }
            }
        }
    }
    
    public init(store: StoreOf<TimetableDetailReducer>) {
        self.store = store
    }
}

#Preview {
    TimetableDetailView(
        store: .init(initialState: .init(title: "")) {
            TimetableDetailReducer()
        }
    )
}
