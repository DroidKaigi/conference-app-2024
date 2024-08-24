import SwiftUI
import ComposableArchitecture
import Theme
import CommonComponents
import shared

public struct TimetableDetailView: View {
    @Bindable private var store: StoreOf<TimetableDetailReducer>
    
    public var body: some View {
        GeometryReader { proxy in
            VStack(spacing: 0) {
                ScrollView {
                    VStack(spacing: 0) {
                        store.timetableItem.room.roomTheme.containerColor
                            .frame(height: proxy.safeAreaInsets.top)
                        headLine
                            .padding(.bottom, 24)
                    }
                    detail
                        .padding(.horizontal, 16)
                    targetAudience
                        .padding(16)
                    if store.timetableItem.asset.videoUrl != nil || 
                       store.timetableItem.asset.slideUrl != nil {
                        archive
                            .padding(16)
                    }
                }
                .toast($store.toast)
                
                footer
            }
            .background(AssetColors.Surface.surface.swiftUIColor)
            .frame(maxWidth: .infinity)
            .ignoresSafeArea(edges: [.top])
        }
        .tint(store.timetableItem.room.roomTheme.primaryColor)
        .confirmationDialog(
            $store.scope(
                state: \.confirmationDialog,
                action: \.confirmationDialog
            )
        )
        .sheet(item: $store.url, content: { url in
            SafariView(url: url.id)
                .ignoresSafeArea()
        })
        .environment(\.openURL, OpenURLAction { url in
            store.send(.view(.urlTapped(url)))
            return .handled
        })
    }
    
    @MainActor var footer: some View {
        HStack(spacing: 28) {
            if let url = URL(string: store.timetableItem.url) {
                ShareLink(item: url) {
                    Image(.icShare)
                        .resizable()
                        .frame(width: 24, height: 24)
                }
            }
            Button {
                store.send(.view(.calendarButtonTapped))
            } label: {
                Image(.icAddCalendar)
                    .resizable()
                    .frame(width: 24, height: 24)
            }
            Spacer()
            Button {
                store.send(.view(.favoriteButtonTapped))
            } label: {
                Group {
                    if store.isFavorited {
                        Image(.icFavoriteFill)
                    } else {
                        Image(.icFavoriteOutline)
                    }
                    
                }
                .frame(width: 56, height: 56)
                .background(AssetColors.Secondary.secondaryContainer.swiftUIColor)
                .clipShape(RoundedRectangle(cornerRadius: 16))
            }
        }
        .frame(height: 80)
        .frame(maxWidth: .infinity)
        .padding(.horizontal, 16)
        .background(AssetColors.Surface.surfaceContainer.swiftUIColor)
    }

    @MainActor var headLine: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(spacing: 4) {
                RoomTag(.init(
                    currentLangTitle: store.timetableItem.room.name.currentLangTitle,
                    enTitle: store.timetableItem.room.name.enTitle,
                    jaTitle: store.timetableItem.room.name.jaTitle
                ))

                ForEach(store.timetableItem.language.labels, id: \.self) { label in
                    LanguageTag(label)
                }
            }
            .padding(.bottom, 8)

            Text(store.timetableItem.title.currentLangTitle)
                .textStyle(.headlineSmall)
                .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
                .padding(.bottom, 20)
                .frame(maxWidth: .infinity, alignment: .leading)
            
            ForEach(store.timetableItem.speakers, id: \.id) { speaker in
                HStack(spacing: 12) {
                    CircularUserIcon(urlString: speaker.iconUrl)
                        .frame(width: 52, height: 52)
                        
                    VStack(alignment: .leading, spacing: 8) {
                        Text(speaker.name)
                            .textStyle(.bodyLarge)
                            .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                        Text(speaker.tagLine)
                            .textStyle(.bodySmall)
                            .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                    }

                    Spacer()
                }
            }
            .padding(.bottom, 20)
        }
        .padding([.top, .horizontal], 16)
        .background(store.timetableItem.room.roomTheme.containerColor)
    }

    @MainActor var detail: some View {
        VStack(alignment: .leading, spacing: 20) {
            VStack(spacing: 16) {
                InformationRow(
                    icon: Image(.icSchedule),
                    title: String(localized: "TimeTableDetailDate", bundle: .module),
                    titleColor: store.timetableItem.room.roomTheme.primaryColor,
                    content: store.timetableItem.formattedDateTimeString
                )
                InformationRow(
                    icon: Image(.icLocationOn),
                    title: String(localized: "TimeTableDetailLocation", bundle: .module),
                    titleColor: store.timetableItem.room.roomTheme.primaryColor,
                    content: store.timetableItem.room.name.currentLangTitle
                )
                InformationRow(
                    icon: Image(.icLanguage),
                    title: String(localized: "TimeTableDetailLanguage", bundle: .module),
                    titleColor: store.timetableItem.room.roomTheme.primaryColor,
                    content: store.timetableItem.getSupportedLangString(isJapaneseLocale: Locale_iosKt.getDefaultLocale() == .japan)
                )
                InformationRow(
                    icon: Image(.icCategory),
                    title: String(localized: "TimeTableDetailCategory", bundle: .module),
                    titleColor: store.timetableItem.room.roomTheme.primaryColor,
                    content: store.timetableItem.category.title.currentLangTitle
                )
            }
            .padding(16)
            .overlay(
                store.timetableItem.room.roomTheme.primaryColor,
                in: RoundedRectangle(cornerRadius: 4)
                    .stroke(style: .init(lineWidth: 1, dash: [2, 2]))
            )
            
            if let session = store.timetableItem as? TimetableItem.Session {
                SessionDescriptionView(
                    content: session.description_.currentLangTitle,
                    themeColor: session.room.roomTheme.primaryColor
                )
                    .padding(.bottom, 24)
            }
        }
    }
    
    @MainActor var targetAudience: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text(String(localized: "TimeTableDetailApplicants", bundle: .module))
                .textStyle(.titleLarge)
                .foregroundStyle(store.timetableItem.room.roomTheme.primaryColor)

            Text(store.timetableItem.targetAudience)
                .textStyle(.bodyLarge)
                .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
    
    @MainActor var archive: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text(String(localized: "TimeTableDetailArchive", bundle: .module))
                .textStyle(.titleLarge)
                .foregroundStyle(store.timetableItem.room.roomTheme.primaryColor)

            HStack {
                if let slideUrlString = store.timetableItem.asset.slideUrl,
                   let slideUrl = URL(string: slideUrlString) {
                    Button {
                        store.send(.view(.slideButtonTapped(slideUrl)))
                    } label: {
                        VStack {
                            Label(
                                title: {
                                    Text(String(localized: "TimeTableDetailSlide", bundle: .module))
                                        .textStyle(.labelLarge)
                                        .foregroundStyle(AssetColors.Primary.onPrimary.swiftUIColor)
                                },
                                icon: { Image(.icDocument) }
                            )
                        }
                        .frame(height: 40)
                        .frame(maxWidth: .infinity)
                        .background(store.timetableItem.room.roomTheme.primaryColor)
                        .clipShape(Capsule())
                    }
                }
                if let videoUrlString = store.timetableItem.asset.videoUrl,
                   let videoUrl = URL(string: videoUrlString) {
                    Button {
                        store.send(.view(.videoButtonTapped(videoUrl)))
                    } label: {
                        VStack {
                            Label(
                                title: {
                                    Text(String(localized: "TimeTableDetailVideo", bundle: .module))
                                        .textStyle(.labelLarge)
                                        .foregroundStyle(AssetColors.Primary.onPrimary.swiftUIColor)
                                },
                                icon: { Image(.icPlay) }
                            )
                        }
                        .frame(height: 40)
                        .frame(maxWidth: .infinity)
                        .background(store.timetableItem.room.roomTheme.primaryColor)
                        .clipShape(Capsule())
                    }
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
        store: .init(initialState: .init(timetableItem: TimetableItem.Session.companion.fake())) {
            TimetableDetailReducer()
        }
    )
}
