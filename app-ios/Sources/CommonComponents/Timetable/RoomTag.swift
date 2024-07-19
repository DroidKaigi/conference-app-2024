import SwiftUI
import Theme

public struct RoomTag: View {
    public enum Room {
        case arcticFox
        case bumblebee
        case chipmunk
        case dolphin
        case electricEel
    }

    let room: Room

    public init(_ room: Room) {
        self.room = room
    }

    public var body: some View {
        HStack(spacing: 5) {
            Rectangle()
                .frame(width: 10, height: 10)
            Text(room.name)
                .textStyle(.labelMedium)
        }
        .foregroundStyle(room.color)
        .padding(.horizontal, 6)
        .overlay(
            RoundedRectangle(cornerRadius: 2)
                .stroke(room.color, lineWidth: 1)
        )
    }
}

extension RoomTag.Room {
    var name: String {
        switch self {
        case .arcticFox: "Arctic Fox"
        case .bumblebee: "Bumblebee"
        case .chipmunk: "Chipmunk"
        case .dolphin: "Dolphin"
        case .electricEel: "Electric eel"
        }
    }

    var color: Color {
        switch self {
        case .arcticFox: AssetColors.Custom.arcticFox.swiftUIColor
        case .bumblebee: AssetColors.Custom.bumblebee.swiftUIColor
        case .chipmunk: AssetColors.Custom.chipmunk.swiftUIColor
        case .dolphin: AssetColors.Custom.dolphin.swiftUIColor
        case .electricEel: AssetColors.Custom.electricEel.swiftUIColor
        }
    }
}


#Preview {
    RoomTag(.arcticFox)
}
