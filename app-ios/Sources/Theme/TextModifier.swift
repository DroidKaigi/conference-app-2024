import SwiftUI

extension Text {
    public func textStyle(_ style: TextStyle) -> some View {
        self.font(style.font)
            .lineSpacing(style.lineHeight)
            .modifier(LetterSpacingModifier(spacing: style.letterSpacing ?? 0))
    }
}

private struct LetterSpacingModifier: ViewModifier {
    var spacing: CGFloat
    func body(content: Content) -> some View {
        content.padding(.horizontal, spacing / 2)
    }
}
