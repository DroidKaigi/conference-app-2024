import SwiftUI
import Theme

struct KeyVisual: View {
    var body: some View {
        VStack(spacing: 0) {
            Image(.headerLogo)
                .resizable()
                .frame(maxWidth: .infinity)
                .scaledToFit()
                .padding(.bottom, 16)
            Text(String(localized: "KeyVisualText", bundle: .module))
                .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                .textStyle(.titleMedium)
                .multilineTextAlignment(.center)
                .padding(.bottom, 20)
            VStack(alignment: .leading, spacing: 12) {
                HStack(spacing: 8) {
                    Image(.icSchedule)
                        .resizable()
                        .frame(width: 16, height: 16)
                        .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                    Text(String(localized: "KeyVisualDateKey", bundle: .module))
                        .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                        .textStyle(.titleSmall)
                        .padding(.trailing, 4)
                    Text(String(localized: "KeyVisualDateValue", bundle: .module))
                        .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                        .textStyle(.titleSmall)
                    Spacer()
                }
                HStack(spacing: 8) {
                    Image(.icLocation)
                        .resizable()
                        .frame(width: 16, height: 16)
                        .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                    Text(String(localized: "KeyVisualPlaceKey", bundle: .module))
                        .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                        .textStyle(.titleSmall)
                        .padding(.trailing, 4)
                    Text(String(localized: "KeyVisualPlaceValue", bundle: .module))
                        .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                        .textStyle(.titleSmall)
                    Link(destination: URL(string: "https://2024.droidkaigi.jp/")!) {
                        Text(String(localized: "KeyVisualCheckMap", bundle: .module))
                            .textStyle(.titleSmall)
                            .foregroundStyle(AssetColors.Custom.jellyfish.swiftUIColor)
                            .underline()
                    }
                    Spacer()
                }
            }
            .frame(maxWidth: .infinity)
            .padding(.vertical, 20)
            .padding(.horizontal, 16)
            .background(AssetColors.Surface.surfaceContainerLow.swiftUIColor, in: RoundedRectangle(cornerRadius: 10))
            .overlay(
                AssetColors.Surface.onSurfaceVariant.swiftUIColor,
                in: RoundedRectangle(cornerRadius: 4)
                    .stroke(style: .init(lineWidth: 1, dash: [2, 2]))
            )
        }
    }
}

#Preview {
    VStack {
        Spacer()
        KeyVisual()
            .background(AssetColors.Surface.surface.swiftUIColor)
        Spacer()
    }
    .padding(.horizontal, 16)
}
