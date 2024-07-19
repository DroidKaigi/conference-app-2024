import SwiftUI
import Theme

struct LanguageTag: View {
    let languageLabel: String
    
    init(_ languageLabel: String) {
        self.languageLabel = languageLabel
    }

    var body: some View {
        Text(languageLabel)
            .textStyle(.labelMedium)
            .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
            .padding(.horizontal, 4)
            .overlay(
                RoundedRectangle(cornerRadius: 2)
                    .stroke(AssetColors.Outline.outline.swiftUIColor, lineWidth: 1)
            )
    }
}

#Preview {
    LanguageTag("EN")
}
