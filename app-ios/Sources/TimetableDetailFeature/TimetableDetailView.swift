import SwiftUI
import ComposableArchitecture
import Theme
import CommonComponents

public struct TimetableDetailView: View {
    @Bindable private var store: StoreOf<TimetableDetailReducer>
    
    public var body: some View {
        GeometryReader { proxy in
            VStack(spacing: 0) {
                ScrollView {
                    VStack(spacing: 0) {
                        AssetColors.Custom.arcticFoxContainer.swiftUIColor
                            .frame(height: proxy.safeAreaInsets.top)
                        headLine
                            .padding(.bottom, 24)
                    }
                    detail
                        .padding(.horizontal, 16)
                    applicants
                        .padding(16)
                    archive
                        .padding(16)
                }
                .toast($store.toast)
                
                footer
            }
            .background(AssetColors.Surface.surface.swiftUIColor)
            .frame(maxWidth: .infinity)
            .ignoresSafeArea(edges: [.top])
        }
        .toolbarBackground(AssetColors.Surface.surface.swiftUIColor, for: .navigationBar)
    }
    
    @MainActor var footer: some View {
        HStack(spacing: 8) {
            Button {
                store.send(.view(.shareButtonTapped))
            } label: {
                Group {
                    Image(.icShare)
                }
                .frame(width: 40, height: 40)
            }
            Button {
                store.send(.view(.calendarButtonTapped))
            } label: {
                Group {
                    Image(.icAddCalendar)
                }
                .frame(width: 40, height: 40)
            }
            Spacer()
            Button {
                store.send(.view(.bookmarkButtonTapped))
            } label: {
                Group {
                    if store.isBookmarked {
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
                RoomTag(.arcticFox)
                LanguageTag(.japanese)
                LanguageTag(.english)
            }
            .padding(.bottom, 8)

            Text(SampleData.title)
                .textStyle(.headlineSmall)
                .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
                .padding(.bottom, 20)
            
            HStack(spacing: 12) {
                Image(.avatar)
                    .clipShape(Circle())
                VStack(spacing: 8) {
                    Text(SampleData.name)
                        .textStyle(.bodyLarge)
                        .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                    Text(SampleData.tagLine)
                        .textStyle(.bodySmall)
                        .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                }

                Spacer()
            }
            .padding(.bottom, 20)
        }
        .padding([.top, .horizontal], 16)
        .background(AssetColors.Custom.arcticFoxContainer.swiftUIColor)
    }

    @MainActor var detail: some View {
        VStack(alignment: .leading, spacing: 20) {
            VStack(spacing: 16) {
                InformationRow(
                    icon: Image(.icSchedule),
                    title: String(localized: "TimeTableDetailDate", bundle: .module),
                    titleColor: AssetColors.Custom.arcticFox.swiftUIColor,
                    content: SampleData.dateValue
                )
                InformationRow(
                    icon: Image(.icLocationOn),
                    title: String(localized: "TimeTableDetailLocation", bundle: .module),
                    titleColor: AssetColors.Custom.arcticFox.swiftUIColor,
                    content: SampleData.locationValue
                )
                InformationRow(
                    icon: Image(.icLanguage),
                    title: String(localized: "TimeTableDetailLanguage", bundle: .module),
                    titleColor: AssetColors.Custom.arcticFox.swiftUIColor,
                    content: SampleData.languageValue
                )
                InformationRow(
                    icon: Image(.icCategory),
                    title: String(localized: "TimeTableDetailCategory", bundle: .module),
                    titleColor: AssetColors.Custom.arcticFox.swiftUIColor,
                    content: SampleData.categoryValue
                )
            }
            .padding(16)
            .overlay(
                AssetColors.Custom.arcticFox.swiftUIColor,
                in: RoundedRectangle(cornerRadius: 4)
                    .stroke(style: .init(lineWidth: 1, dash: [2, 2]))
            )
            
            SessionDescriptionView(content: SampleData.sessionDescription)
                .padding(.bottom, 24)
        }
    }
    
    @MainActor var applicants: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text(String(localized: "TimeTableDetailApplicants", bundle: .module))
                .textStyle(.titleLarge)
                .foregroundStyle(AssetColors.Custom.arcticFox.swiftUIColor)

            Text(SampleData.applicants)
                .textStyle(.bodyLarge)
                .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
        }
    }
    
    @MainActor var archive: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text(String(localized: "TimeTableDetailArchive", bundle: .module))
                .textStyle(.titleLarge)
                .foregroundStyle(AssetColors.Custom.arcticFox.swiftUIColor)

            HStack {
                Button {
                    store.send(.view(.slideButtonTapped))
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
                    .background(AssetColors.Custom.arcticFox.swiftUIColor)
                    .clipShape(Capsule())
                }
                Button {
                    store.send(.view(.videoButtonTapped))
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
                    .background(AssetColors.Custom.arcticFox.swiftUIColor)
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
        store: .init(initialState: .init()) {
            TimetableDetailReducer()
        }
    )
}
