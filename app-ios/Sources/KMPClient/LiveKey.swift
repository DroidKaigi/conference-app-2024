import Dependencies
import shared

private var sessionsRepository: any SessionsRepository {
    Container.shared.get(type: (any SessionsRepository).self)
}

private var staffRepository: any StaffRepository {
    Container.shared.get(type: (any StaffRepository).self)
}

private var sponsorsRepository: any SponsorsRepository {
    Container.shared.get(type: (any SponsorsRepository).self)
}

private var contributorRepository: any ContributorsRepository {
    Container.shared.get(type: (any ContributorsRepository).self)
}

extension TimetableClient: DependencyKey {
    public static let liveValue: TimetableClient = .init(
        streamTimetable: {
            sessionsRepository.getTimetableStream().eraseToThrowingStream()
        },
        streamTimetableItemWithFavorite: { id in
            sessionsRepository
                .getTimetableItemWithBookmarkStream(id: id)
                .compactMap { pair in
                    guard let item = pair.first, let isFavorited = pair.second?.boolValue else { return nil }
                    return (item, isFavorited)
                }
                .eraseToThrowingStream()
        },
        toggleBookmark: { id in
            try await sessionsRepository.toggleBookmark(id: id)
        }
    )
}

extension StaffClient: DependencyKey {
    public static let liveValue: StaffClient = .init(
        streamStaffs: {
            staffRepository.staffs().eraseToThrowingStream()
        }
    )
}

extension SponsorsClient: DependencyKey {
    public static let liveValue: SponsorsClient = .init(
        streamSponsors: {
            sponsorsRepository.getSponsorStream().eraseToThrowingStream()
        }
    )
}

extension ContributorClient: DependencyKey {
    public static let liveValue: ContributorClient = Self {
        contributorRepository.getContributorStream().eraseToThrowingStream()
    } refresh: {
        try await contributorRepository.refresh()
    }
}
