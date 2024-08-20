import Foundation

public struct Contributor: Equatable, Sendable, Identifiable {
    public var id: Int
    public let userName: String
    public let profileUrl: URL?
    public let iconUrl: URL
    
    public init(id: Int, userName: String, profileUrl: URL?, iconUrl: URL) {
        self.id = id
        self.userName = userName
        self.profileUrl = profileUrl
        self.iconUrl = iconUrl
    }
}
