import shared
import Theme

extension TimetableRoom {
    public var roomTheme: RoomTheme {
        let roomType: Model.RoomType = switch type {
        case .roomF: .roomF
        case .roomG: .roomG
        case .roomH: .roomH
        case .roomI: .roomI
        case .roomJ: .roomJ
        case .roomIj: .roomIj
        }
        return roomType.theme
    }
}
