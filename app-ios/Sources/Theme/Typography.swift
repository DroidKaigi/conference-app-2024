import SwiftUI

public struct TextStyle: Sendable {
    var font: UIFont
    var lineHeight: CGFloat
    var letterSpacing: CGFloat?
    var lineSpacing: CGFloat {
        lineHeight - font.lineHeight
    }
}

extension TextStyle {
    public static let displayLarge: TextStyle = TypographyTokens.displayLarge
    public static let displayMedium: TextStyle = TypographyTokens.displayMedium
    public static let displaySmall: TextStyle = TypographyTokens.displaySmall
    public static let headlineLarge: TextStyle = TypographyTokens.headlineLarge
    public static let headlineMedium: TextStyle = TypographyTokens.headlineMedium
    public static let headlineSmall: TextStyle = TypographyTokens.headlineSmall
    public static let titleLarge: TextStyle = TypographyTokens.titleLarge
    public static let titleMedium: TextStyle = TypographyTokens.titleMedium
    public static let titleSmall: TextStyle = TypographyTokens.titleSmall
    public static let bodyLarge: TextStyle = TypographyTokens.bodyLarge
    public static let bodyMedium: TextStyle = TypographyTokens.bodyMedium
    public static let bodySmall: TextStyle = TypographyTokens.bodySmall
    public static let labelLarge: TextStyle = TypographyTokens.labelLarge
    public static let labelMedium: TextStyle = TypographyTokens.labelMedium
    public static let labelSmall: TextStyle = TypographyTokens.labelSmall
}

enum TypographyTokens {
    static let displayLarge = TextStyle(font: FontAssets.DotGothic16.regular.font(size: 57), lineHeight: 64, letterSpacing: -0.25)
    static let displayMedium = TextStyle(font: FontAssets.DotGothic16.regular.font(size: 45), lineHeight: 52)
    static let displaySmall = TextStyle(font: FontAssets.DotGothic16.regular.font(size: 36), lineHeight: 44)
    static let headlineLarge = TextStyle(font: FontAssets.DotGothic16.regular.font(size: 32), lineHeight: 40)
    static let headlineMedium = TextStyle(font: FontAssets.DotGothic16.regular.font(size: 28), lineHeight: 36 - 28)
    static let headlineSmall = TextStyle(font: FontAssets.DotGothic16.regular.font(size: 24), lineHeight: 32)
    static let titleLarge = TextStyle(font: FontAssets.DotGothic16.regular.font(size: 22), lineHeight: 28)
    static let titleMedium = TextStyle(font: FontAssets.DotGothic16.regular.font(size: 16), lineHeight: 24, letterSpacing: 0.15)
    static let titleSmall = TextStyle(font: FontAssets.DotGothic16.regular.font(size: 14), lineHeight: 20, letterSpacing: 0.1)
    static let labelLarge = TextStyle(font: FontAssets.DotGothic16.regular.font(size: 14), lineHeight: 20, letterSpacing: 0.1)
    static let labelMedium = TextStyle(font: FontAssets.DotGothic16.regular.font(size: 12), lineHeight: 16, letterSpacing: 0.5)
    static let labelSmall = TextStyle(font: FontAssets.DotGothic16.regular.font(size: 11), lineHeight: 16, letterSpacing: 0.5)
    static let bodyLarge = TextStyle(font: FontAssets.Roboto.regular.font(size: 16), lineHeight: 24, letterSpacing: 0.5)
    static let bodyMedium = TextStyle(font: FontAssets.Roboto.regular.font(size: 14), lineHeight: 20, letterSpacing: 0.25)
    static let bodySmall = TextStyle(font: FontAssets.Roboto.regular.font(size: 12), lineHeight: 16, letterSpacing: 0.4)
}
