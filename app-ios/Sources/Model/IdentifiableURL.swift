import Foundation

public struct IdentifiableURL: Identifiable {
    public let id: URL

    public init(_ id: URL) {
        self.id = id
    }
}

extension IdentifiableURL: Equatable {}
