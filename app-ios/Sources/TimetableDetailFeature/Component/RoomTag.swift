import SwiftUI
import Theme

struct RoomTag: View {
    enum Room {
        case arcticFox
        case bumblebee
        case chipmunk
        case dolphin
        case electricEel
    }

    let room: Room

    init(_ room: Room) {
        self.room = room
    }

    var body: some View {
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
        case .flamingo: "Flamingo"
        case .giraffe: "Giraffe"
        case .hedgehog: "Hedgehog"
        case .iguana: "Iguana"
        case .jellyfish: "Jellyfish"
        }
    }

    var color: Color {
        switch self {
        case .flamingo: AssetColors.Custom.flamingo.swiftUIColor
        case .giraffe: AssetColors.Custom.giraffe.swiftUIColor
        case .hedgehog: AssetColors.Custom.hedgehog.swiftUIColor
        case .iguana: AssetColors.Custom.iguana.swiftUIColor
        case .jellyfish: AssetColors.Custom.jellyfish.swiftUIColor
        }
    }
}


#Preview {
    RoomTag(.flamingo)
}
