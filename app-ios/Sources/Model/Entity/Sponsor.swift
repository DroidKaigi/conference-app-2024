import Foundation

public struct Sponsor: Equatable, Identifiable, Sendable {
    public enum Plan: Equatable, Sendable {
        case platinum
        case gold
        case supporter
    }
    public let id: String
    public let logo: URL
    public let link: URL
    public let plan: Plan
    
    public init(id: String, logo: URL, link: URL, plan: Sponsor.Plan) {
        self.id = id
        self.logo = logo
        self.link = link
        self.plan = plan
    }
}
