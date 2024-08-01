import SwiftUI
import Theme
import shared
import Model

public struct RoomTag: View {
    let room: TimetableRoom

    public init(_ room: TimetableRoom) {
        self.room = room
    }

    public var body: some View {
        HStack(spacing: 4) {
            room.shape
            Text(room.name.currentLangTitle)
                .textStyle(.labelMedium)
        }
        .foregroundStyle(room.roomTheme.primaryColor)
        .padding(.horizontal, 6)
        .overlay(
            RoundedRectangle(cornerRadius: 2)
                .stroke(room.roomTheme.primaryColor, lineWidth: 1)
        )
    }
}

extension TimetableRoom {
    var shape: some View {
        Group {
            switch getShape() {
            case .circle: Image(.icCircleFill).renderingMode(.template)
            case .diamond: Image(.icDiamondFill).renderingMode(.template)
            case .sharpDiamond: Image(.icSharpDiamondFill).renderingMode(.template)
            case .square: Image(.icSquareFill).renderingMode(.template)
            default: Image(.icTriangleFill).renderingMode(.template)
            }
        }
        .foregroundStyle(roomTheme.primaryColor)
        .frame(width: 12, height: 12)
    }
}

#Preview {
    RoomTag(
        TimetableRoom(
            id: 1,
            name: MultiLangText(
                jaTitle: "Iguana",
                enTitle: "Iguana"
            ),
            type: .roomF,
            sort: 1
        )
    )
}
