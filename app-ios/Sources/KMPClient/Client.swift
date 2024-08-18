import DependenciesMacros
import Dependencies
import shared
import Model

extension DependencyValues {
    public var timetableClient: TimetableClient {
        get { self[TimetableClient.self] }
        set { self[TimetableClient.self] = newValue }
    }

    public var staffClient: StaffClient {
        get { self[StaffClient.self] }
        set { self[StaffClient.self] = newValue }
    }

    public var sponsorsClient: SponsorsClient {
        get { self[SponsorsClient.self] }
        set { self[SponsorsClient.self] = newValue }
    }
    
    public var contributorClient: ContributorClient {
        get { self[ContributorClient.self] }
        set { self[ContributorClient.self] = newValue }
    }
    
    public var eventMapClient: EventMapClient {
        get { self[EventMapClient.self] }
        set { self[EventMapClient.self] = newValue }
    }
}

@DependencyClient
public struct TimetableClient: Sendable {
    public var streamTimetable: @Sendable () throws -> AsyncThrowingStream<Timetable, any Error>
    public var streamTimetableItemWithFavorite: @Sendable (_ id: TimetableItemId) throws -> AsyncThrowingStream<(TimetableItem, Bool), any Error>
    public var toggleBookmark: @Sendable (_ id: TimetableItemId) async throws -> Void
}

@DependencyClient
public struct StaffClient: Sendable {
    public var streamStaffs: @Sendable () throws -> AsyncThrowingStream<[Model.Staff], any Error>
}

@DependencyClient
public struct SponsorsClient: Sendable {
    public var streamSponsors: @Sendable () throws -> AsyncThrowingStream<[Sponsor], any Error>
}

@DependencyClient
public struct ContributorClient: Sendable {
    public var streamContributors: @Sendable () throws -> AsyncThrowingStream<[Contributor], any Error>
    public var refresh: @Sendable () async throws -> Void
}

@DependencyClient
public struct EventMapClient: Sendable {
    public var streamEvents: @Sendable () throws -> AsyncThrowingStream<[EventMapEvent], any Error>
}
