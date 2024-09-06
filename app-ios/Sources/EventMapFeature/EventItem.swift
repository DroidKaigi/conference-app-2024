import SwiftUI
import CommonComponents
import Theme
import Model

struct EventItem: View {
    let event: EventMapEvent
    let onTappedMoreDetail: (URL) -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(spacing: 12) {
                RoomTag(.init(
                    currentLangTitle: event.roomName.currentLangTitle,
                    enTitle: event.roomName.enTitle,
                    jaTitle: event.roomName.jaTitle
                ))

                Text(event.name.currentLangTitle)
                    .foregroundStyle(AssetColors.Primary.primaryFixed.swiftUIColor)
                    .textStyle(.titleMedium)
                Spacer()
            }
            .padding(.bottom, 8)

            VStack(alignment: .leading ,spacing: 8) {
                Text(event.description.currentLangTitle)
                    .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                    .textStyle(.bodyLarge)

                if let message = event.message {
                    Text(message.currentLangTitle)
                        .foregroundStyle(AssetColors.Tertiary.tertiary.swiftUIColor)
                        .textStyle(.bodyMedium)
                }
                
                if let url = event.moreDetailsUrl {
                    Button {
                        onTappedMoreDetail(url)
                    } label: {
                        Text(String(localized: "Detail", bundle: .module))
                            .textStyle(.labelLarge)
                            .foregroundStyle(AssetColors.Primary.primaryFixed.swiftUIColor)
                            .frame(height: 40)
                            .frame(maxWidth: .infinity, alignment: .center)
                            .overlay {
                                Capsule()
                                    .stroke(AssetColors.Outline.outline.swiftUIColor)
                            }
                    }
                }
            }
            .padding(.bottom, 24)
            
            Divider()
                .background(AssetColors.Outline.outlineVariant.swiftUIColor)
        }
        .frame(maxWidth: .infinity)
        .padding(.horizontal, 16)
    }
}

#Preview {
    EventItem(
        event: .init(
            name: .init(currentLangTitle: "name", enTitle: "name", jaTitle: "name"),
            roomName: .init(currentLangTitle: "roomName", enTitle: "roomName", jaTitle: "roomName"),
            roomIcon: .square,
            description: .init(currentLangTitle: "description", enTitle: "description", jaTitle: "description"),
            moreDetailsUrl: nil,
            message: nil
        )
    ) { _ in }
}
