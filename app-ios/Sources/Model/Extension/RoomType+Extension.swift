import Foundation
import Theme

extension RoomType {
    public var theme: RoomTheme {
        switch self {
        case .roomF:
            return RoomTheme(
                primaryColor: AssetColors.Custom.flamingo.swiftUIColor,
                containerColor: AssetColors.Custom.flamingoContainer.swiftUIColor,
                dimColor: AssetColors.Custom.flamingoDim.swiftUIColor
            )
        case .roomG:
            return RoomTheme(
                primaryColor: AssetColors.Custom.giraffe.swiftUIColor,
                containerColor: AssetColors.Custom.giraffeContainer.swiftUIColor,
                dimColor: AssetColors.Custom.giraffeDim.swiftUIColor
            )
        case .roomH:
            return RoomTheme(
                primaryColor: AssetColors.Custom.hedgehog.swiftUIColor,
                containerColor: AssetColors.Custom.hedgehogContainer.swiftUIColor,
                dimColor: AssetColors.Custom.hedgehogDim.swiftUIColor
            )
        case .roomI:
            return RoomTheme(
                primaryColor: AssetColors.Custom.iguana.swiftUIColor,
                containerColor: AssetColors.Custom.iguanaContainer.swiftUIColor,
                dimColor: AssetColors.Custom.iguanaDim.swiftUIColor
            )
        case .roomJ:
            return RoomTheme(
                primaryColor: AssetColors.Custom.jellyfish.swiftUIColor,
                containerColor: AssetColors.Custom.jellyfishContainer.swiftUIColor,
                dimColor: AssetColors.Custom.jellyfishDim.swiftUIColor
            )
        case .roomIj:
            return RoomTheme(
                primaryColor: AssetColors.Custom.iguana.swiftUIColor,
                containerColor: AssetColors.Custom.iguanaContainer.swiftUIColor,
                dimColor: AssetColors.Custom.iguanaDim.swiftUIColor
            )
        }
    }

}
