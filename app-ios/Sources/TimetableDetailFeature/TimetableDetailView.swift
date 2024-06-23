import SwiftUI
import ComposableArchitecture
import Theme

public struct TimetableDetailView: View {
    private let store: StoreOf<TimetableDetailReducer>

    public var body: some View {
        VStack {
            ScrollView {
                sessionDetail
                    .padding(.horizontal, 16)
                
                Divider().background(AssetColors.Outline.outlineVariant.swiftUIColor)

                targetUser
                    .padding(16)

                Divider().background(AssetColors.Outline.outlineVariant.swiftUIColor)
                
                speaker
                    .padding(16)
                
                Divider().background(AssetColors.Outline.outlineVariant.swiftUIColor)
                
                archive
                    .padding(16)
            }
            
            footer
        }
        .background(AssetColors.Surface.surface.swiftUIColor)
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
                .background(AssetColors.Surface.surfaceContainer.swiftUIColor)
                .clipShape(RoundedRectangle(cornerRadius: 16))
            }
        }
        .frame(height: 80)
        .frame(maxWidth: .infinity)
        .padding(.horizontal, 16)
        .background(AssetColors.Surface.surfaceContainer.swiftUIColor)
    }
    
    @ViewBuilder var sessionDetail: some View {
        VStack(alignment: .leading, spacing: 20) {
            Text(SampleData.title)
                .font(.title)
                .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
            VStack(spacing: 16) {
                InformationRow(
                    icon: Image(.icSchedule),
                    title: String(localized: "TimeTableDetailDate", bundle: .module),
                    content: SampleData.dateValue
                )
                InformationRow(
                    icon: Image(.icLocationOn),
                    title: String(localized: "TimeTableDetailLocation", bundle: .module),
                    content: SampleData.locationValue
                )
                InformationRow(
                    icon: Image(.icLanguage),
                    title: String(localized: "TimeTableDetailLanguage", bundle: .module),
                    content: SampleData.languageValue
                )
                InformationRow(
                    icon: Image(.icCategory),
                    title: String(localized: "TimeTableDetailCategory", bundle: .module),
                    content: SampleData.categoryValue
                )
            }
            .padding(16)
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(AssetColors.Surface.onSurface.swiftUIColor, lineWidth: 1)
            )
            
            SessionDescriptionView(content: SampleData.sessionDescription)
                .padding(.bottom, 24)
        }
    }
    
    @ViewBuilder var targetUser: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text(String(localized: "TimeTableDetailApplicants", bundle: .module))
                .font(.title3)
                .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)

            Text(SampleData.applicants)
                .font(.callout)
                .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
        }
    }
    
    @ViewBuilder var speaker: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text(String(localized: "TimeTableDetailSpeaker", bundle: .module))
                .font(.title3)
                .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)

            HStack {
                Image(.avatar)
                    .padding(.trailing, 24)
                Text(SampleData.name)
                    .font(.callout)
                    .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                Spacer()
            }
        }
    }
    
    @ViewBuilder var archive: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text(String(localized: "TimeTableDetailArchive", bundle: .module))
                .font(.title3)
                .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)

            HStack {
                Button {
                    // do something
                } label: {
                    VStack {
                        Label(
                            title: { Text(String(localized: "TimeTableDetailSlide", bundle: .module)).foregroundStyle(AssetColors.Primary.onPrimary.swiftUIColor) },
                            icon: { Image(.icDocument) }
                        )
                    }
                    .frame(height: 40)
                    .frame(maxWidth: .infinity)
                    .background(AssetColors.Custom.arcticFox.swiftUIColor)
                    .clipShape(Capsule())
                }
                Button {
                    // do something
                } label: {
                    VStack {
                        Label(
                            title: { Text(String(localized: "TimeTableDetailVideo", bundle: .module)).foregroundStyle(AssetColors.Primary.onPrimary.swiftUIColor) },
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
        store: .init(initialState: .init(title: "")) {
            TimetableDetailReducer()
        }
    )
}
