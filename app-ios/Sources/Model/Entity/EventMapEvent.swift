import Foundation

public struct EventMapEvent: Equatable, Sendable, Hashable {
    public let name: MultiLangText
    public let roomName: MultiLangText
    public let roomIcon: RoomIcon
    public let description_: MultiLangText
    public let moreDetailsUrl: URL?
    public let message: MultiLangText?

    public init(name: MultiLangText, roomName: MultiLangText, roomIcon: RoomIcon, description_: MultiLangText, moreDetailsUrl: URL?, message: MultiLangText?) {
        self.name = name
        self.roomName = roomName
        self.roomIcon = roomIcon
        self.description_ = description_
        self.moreDetailsUrl = moreDetailsUrl
        self.message = message
    }

    public func hash(into hasher: inout Hasher) {
        hasher.combine(name.enTitle)
        hasher.combine(roomName.enTitle)
    }
}
