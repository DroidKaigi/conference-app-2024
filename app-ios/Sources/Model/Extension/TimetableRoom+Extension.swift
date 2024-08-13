import shared
import SwiftUI
import Theme

public struct RoomTheme {
    public let primaryColor: Color
    public let containerColor: Color
    public let dimColor: Color
}

extension TimetableRoom {
    public var roomTheme: RoomTheme { type.theme }
}
