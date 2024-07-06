import SwiftUI

extension Text {
    public func textStyle(_ style: TextStyle) -> some View {
        self.modifier(TextStyleModifier(style: style))
    }
}

private struct TextStyleModifier: ViewModifier {
    var style: TextStyle
    func body(content: Content) -> some View {
        content
            .font(SwiftUI.Font(style.font))
            .lineSpacing(style.lineSpacing)
            .padding(.vertical, style.lineSpacing / 2)
            .tracking(style.letterSpacing ?? 0)
    }
}
