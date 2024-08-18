public protocol Selectable: CaseIterable, Equatable, Identifiable, Hashable {
    associatedtype Options: RandomAccessCollection = [Self] where Self == Self.Options.Element
    static var options: Options { get }
    var caseTitle: String { get }

}

public extension Selectable {
    static var options: Options { allCases as! Options }
}
