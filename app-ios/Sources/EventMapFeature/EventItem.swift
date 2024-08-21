import SwiftUI
import CommonComponents
import Theme
import shared

struct EventItem: View {
    @State private var isDescriptionExpanded: Bool = false
    @State private var canBeExpanded: Bool = false
    let event: EventMapEvent

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
                Text(event.description_.currentLangTitle)
                    .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                    .textStyle(.bodyLarge)
                    .lineLimit(isDescriptionExpanded ? nil : 3)
                    .background {
                        ViewThatFits(in: .vertical) {
                            Text(event.description_.currentLangTitle)
                                .textStyle(.bodyLarge)
                                .hidden()
                            // Just for receiving onAppear event if the description exceeds its line limit
                            Color.clear
                                .onAppear {
                                    canBeExpanded = true
                                }
                        }
                    }
                if let message = event.message {
                    Text(message.currentLangTitle)
                        .foregroundStyle(AssetColors.Tertiary.tertiary.swiftUIColor)
                        .textStyle(.bodyMedium)
                }
                
                if canBeExpanded {
                    Button {
                        isDescriptionExpanded = true
                        canBeExpanded = false
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
        .animation(.default, value: isDescriptionExpanded)
    }
}

#Preview {
    EventItem(event: EventMapEvent.companion.fakes().first!)
}
