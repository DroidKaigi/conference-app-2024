import SwiftUI
import Theme
import shared
import Model

public struct RoomTag: View {
    let roomName: MultiLangText

    public init(_ roomName: MultiLangText) {
        self.roomName = roomName
    }

    public var body: some View {
        HStack(spacing: 4) {
            roomName.roomType.shape
            Text(roomName.currentLangTitle)
                .textStyle(.labelMedium)
        }
        .foregroundStyle(roomName.roomType.theme.primaryColor)
        .padding(.horizontal, 6)
        .overlay(
            RoundedRectangle(cornerRadius: 2)
                .stroke(roomName.roomType.theme.primaryColor, lineWidth: 1)
        )
    }
}

enum ThemeKey {
    static let iguana = "iguana"
    static let hedgehog = "hedgehog"
    static let giraffe = "giraffe"
    static let flamingo = "flamingo"
    static let jellyfish = "jellyfish"
}


extension MultiLangText {
    var roomType: RoomType {
        switch enTitle.lowercased() {
        case ThemeKey.flamingo: .roomF
        case ThemeKey.giraffe: .roomG
        case ThemeKey.hedgehog: .roomH
        case ThemeKey.iguana: .roomI
        case ThemeKey.jellyfish: .roomJ
        default: .roomIj
        }
    }
}

extension RoomType {
    public var shape: some View {
        Group {
            switch self {
            case .roomG: Image(.icCircleFill).renderingMode(.template)
            case .roomH: Image(.icDiamondFill).renderingMode(.template)
            case .roomF: Image(.icSharpDiamondFill).renderingMode(.template)
            case .roomI: Image(.icSquareFill).renderingMode(.template)
            case .roomJ: Image(.icTriangleFill).renderingMode(.template)
            case .roomIj: Image(.icSquareFill).renderingMode(.template)
            }
        }
        .foregroundStyle(theme.primaryColor)
        .frame(width: 12, height: 12)
    }
}

#Preview {
    RoomTag(
        MultiLangText(
            jaTitle: "Iguana",
            enTitle: "Iguana"
        )
    )
}
