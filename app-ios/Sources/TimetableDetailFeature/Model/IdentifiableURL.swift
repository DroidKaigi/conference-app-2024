import Foundation

struct IdentifiableURL: Identifiable {
    let id: URL
    init(_ id: URL) {
        self.id = id
    }
}

extension IdentifiableURL: Equatable {}
