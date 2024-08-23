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

    public var profileCardClient: ProfileCardClient {
        get { self[ProfileCardClient.self] }
        set { self[ProfileCardClient.self] = newValue }
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
    public var streamSponsors: @Sendable () throws -> AsyncThrowingStream<[Model.Sponsor], any Error>
}

@DependencyClient
public struct ContributorClient: Sendable {
    public var streamContributors: @Sendable () throws -> AsyncThrowingStream<[Model.Contributor], any Error>
    public var refresh: @Sendable () async throws -> Void
}

@DependencyClient
public struct EventMapClient: Sendable {
    public var streamEvents: @Sendable () throws -> AsyncThrowingStream<[EventMapEvent], any Error>
}

@DependencyClient
public struct ProfileCardClient: Sendable {
    public struct SaveParameter: Sendable {
        public var nickname: String
        public var link: String
        public var occupation: String
        public var image: String
        public var cardType: ProfileCardType

        public init(nickname: String, link: String, occupation: String, image: String, cardType: ProfileCardType) {
            self.nickname = nickname
            self.link = link
            self.occupation = occupation
            self.image = image
            self.cardType = cardType
        }
    }

    public var save: @Sendable (_ param: SaveParameter) async throws -> Void
}
