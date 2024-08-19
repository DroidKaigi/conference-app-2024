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
            RoomTypeShape(roomType: roomName.roomType)
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

enum ThemeKey: String {
    case iguana
    case hedgehog
    case giraffe
    case flamingo
    case jellyfish
}

extension MultiLangText {
    var roomType: RoomType {
        switch enTitle.lowercased() {
        case ThemeKey.flamingo.rawValue: .roomF
        case ThemeKey.giraffe.rawValue: .roomG
        case ThemeKey.hedgehog.rawValue: .roomH
        case ThemeKey.iguana.rawValue: .roomI
        case ThemeKey.jellyfish.rawValue: .roomJ
        default: .roomIj
        }
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
