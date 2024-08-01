import shared
import SwiftUI
import Theme

public struct RoomTheme {
    public let primaryColor: Color
    public let containerColor: Color
    public let dimColor: Color
}

enum ThemeKey {
    static let iguana = "iguana"
    static let hedgehog = "hedgehog"
    static let giraffe = "giraffe"
    static let flamingo = "flamingo"
    static let jellyfish = "jellyfish"
}

extension TimetableRoom {
    public var roomTheme: RoomTheme {
        switch getThemeKey().lowercased() {
        case ThemeKey.iguana:
            return RoomTheme(
                primaryColor: AssetColors.Custom.iguana.swiftUIColor,
                containerColor: AssetColors.Custom.iguanaContainer.swiftUIColor,
                dimColor: AssetColors.Custom.iguanaDim.swiftUIColor
            )
        case ThemeKey.hedgehog:
            return RoomTheme(
                primaryColor: AssetColors.Custom.hedgehog.swiftUIColor,
                containerColor: AssetColors.Custom.hedgehogContainer.swiftUIColor,
                dimColor: AssetColors.Custom.hedgehogDim.swiftUIColor
            )
        case ThemeKey.giraffe:
            return RoomTheme(
                primaryColor: AssetColors.Custom.giraffe.swiftUIColor,
                containerColor: AssetColors.Custom.giraffeContainer.swiftUIColor,
                dimColor: AssetColors.Custom.giraffeDim.swiftUIColor
            )
        case ThemeKey.flamingo:
            return RoomTheme(
                primaryColor: AssetColors.Custom.flamingo.swiftUIColor,
                containerColor: AssetColors.Custom.flamingoContainer.swiftUIColor,
                dimColor: AssetColors.Custom.flamingoDim.swiftUIColor
            )
        case ThemeKey.jellyfish:
            return RoomTheme(
                primaryColor: AssetColors.Custom.jellyfish.swiftUIColor,
                containerColor: AssetColors.Custom.jellyfishContainer.swiftUIColor,
                dimColor: AssetColors.Custom.jellyfishDim.swiftUIColor
            )
        default:
            return RoomTheme(
                primaryColor: AssetColors.Custom.iguana.swiftUIColor,
                containerColor: AssetColors.Custom.iguanaContainer.swiftUIColor,
                dimColor: AssetColors.Custom.iguanaDim.swiftUIColor
            )
        }
    }
}
