import SwiftUI

struct KeyVisual: View {
    var body: some View {
        VStack(spacing: 12) {
            Rectangle()
                .aspectRatio(282 / 105.14, contentMode:.fit)
                .padding(.horizontal, 49)
                .foregroundStyle(.green)
                .padding(.bottom, 12)
            Text("KeyVisualText")
                .foregroundStyle(Color(.surfaceOnSurfaceVariant))
                .font(.body)
                .lineSpacing(5)
            VStack(alignment: .leading, spacing: 12) {
                HStack(spacing: 8) {
                    Image(systemName: "clock")
                        .foregroundStyle(Color(.surfaceOnSurfaceVariant))
                        .fontWeight(.bold)
                    Text("KeyVisualDateKey")
                        .foregroundStyle(Color(.surfaceOnSurfaceVariant))
                        .font(.headline)
                        .padding(.trailing, 4)
                    Text("KeyVisualDateValue")
                        .foregroundStyle(Color(.surfaceOnSurface))
                        .font(.callout)
                }
                HStack(spacing: 8) {
                    Image(systemName: "mappin.circle")
                        .foregroundStyle(Color(.surfaceOnSurfaceVariant))
                        .fontWeight(.bold)
                    Text("KeyVisualPlaceKey")
                        .foregroundStyle(Color(.surfaceOnSurfaceVariant))
                        .font(.headline)
                        .padding(.trailing, 4)
                    Text("KeyVisualPlaceValue")
                        .foregroundStyle(Color(.surfaceOnSurface))
                        .font(.callout)
                    Link("KeyVisualCheckMap", destination: URL(string: "https://2024.droidkaigi.jp/")!)
                        .tint(Color(.primaryPrimary))
                        .fontWeight(.bold)
                }
            }
            .padding(.vertical, 20)
            .padding(.horizontal, 16)
            .background(Color(.keyVisualInformationBase), in: RoundedRectangle(cornerRadius: 10))
        }
    }
}

#Preview {
    VStack {
        Spacer()
        KeyVisual()
            .environment(\.locale, Locale(identifier: "ja"))
            .background(Color(.background))
        Spacer()
    }
    .padding(.horizontal, 16)
}
