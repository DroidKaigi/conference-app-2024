import Foundation

public struct Staff: Equatable, Identifiable, Sendable {
    public let id: Int
    public let name: String
    public let icon: URL
    public let github: URL

    public init(id: Int, name: String, icon: URL, github: URL) {
        self.id = id
        self.name = name
        self.icon = icon
        self.github = github
    }
}
