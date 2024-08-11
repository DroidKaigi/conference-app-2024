public protocol Selectable: CaseIterable, Equatable, Identifiable, Hashable {
    var caseTitle: String { get }
}
