import SwiftUI
import Theme

struct SessionDescriptionView: View {
    @State private var isDescriptionExpanded: Bool = false
    @State private var canBeExpanded: Bool = false
    let content: String

    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text(content)
                .textStyle(.bodyLarge)
                .textSelection(.enabled)
                .lineLimit(isDescriptionExpanded ? nil : 5)
                .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
                .background {
                    ViewThatFits(in: .vertical) {
                        Text(content)
                            .hidden()
                        // Just for receiving onAppear event if the description exceeds its line limit
                        Color.clear
                            .onAppear {
                                canBeExpanded = true
                            }
                    }
                }
            if canBeExpanded {
                Button {
                    isDescriptionExpanded = true
                    canBeExpanded = false
                } label: {
                    Text(String(localized: "TimeTableDetailReadMore", bundle: .module))
                        .textStyle(.labelLarge)
                        .foregroundStyle(AssetColors.Custom.arcticFox.swiftUIColor)
                        .frame(height: 40)
                        .frame(maxWidth: .infinity, alignment: .center)
                        .overlay {
                            Capsule()
                                .stroke(AssetColors.Outline.outline.swiftUIColor)
                        }
                }
            }
        }
    }
}

#Preview {
    VStack {
        SessionDescriptionView(content: SampleData.sessionDescription)
            .padding(.horizontal, 16)
    }
    .background(AssetColors.Surface.surface.swiftUIColor)
}
