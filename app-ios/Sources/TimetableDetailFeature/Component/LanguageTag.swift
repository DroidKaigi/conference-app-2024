import SwiftUI
import Theme

struct LanguageTag: View {
    enum Language {
        case japanese, english
        
        var text: String {
            switch self {
            case .japanese: "JA"
            case .english: "EN"
            }
        }
    }

    let language: Language
    
    init(_ language: Language) {
        self.language = language
    }

    var body: some View {
        Text(language.text)
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
    LanguageTag(.english)
}
