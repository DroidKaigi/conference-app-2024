import ComposableArchitecture
import Foundation

extension DependencyValues {
    public var eventKitClient: EventKitClient {
        get { self[EventKitClient.self] }
        set { self[EventKitClient.self] = newValue }
    }
}

@DependencyClient
public struct EventKitClient: Sendable {
    public var requestAccessIfNeeded: @Sendable () async throws -> Bool
    public var addEvent: @Sendable (_ title: String, _ startDate: Date, _ endDate: Date) throws -> Void
}
