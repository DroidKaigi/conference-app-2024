import SwiftUI
import Theme
import Model

public struct RoomTag: View {
    let roomName: MultiLangText
    let roomType: RoomType

    public init(_ roomName: MultiLangText) {
        self.roomName = roomName
        self.roomType = .init(enTitle: roomName.enTitle)
    }

    public var body: some View {
        HStack(spacing: 4) {
            RoomTypeShape(roomType: roomType)
            Text(roomName.currentLangTitle)
                .textStyle(.labelMedium)
        }
        .foregroundStyle(roomType.theme.primaryColor)
        .padding(.horizontal, 6)
        .overlay(
            RoundedRectangle(cornerRadius: 2)
                .stroke(roomType.theme.primaryColor, lineWidth: 1)
        )
    }
}

#Preview {
    RoomTag(
        MultiLangText(
            currentLangTitle: "Iguana",
            enTitle: "Iguana",
            jaTitle: "Iguana"
        )
    )
}
