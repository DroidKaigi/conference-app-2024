import Dependencies
import shared
import Model
import Foundation

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

private var eventMapRepository: any EventMapRepository {
    Container.shared.get(type: (any EventMapRepository).self)
}

private var profileCardRepository: any ProfileCardRepository {
    Container.shared.get(type: (any ProfileCardRepository).self)
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
            staffRepository
                .staffs()
                .map {
                    $0.map {
                        Model.Staff(
                            id: Int($0.id),
                            name: $0.username,
                            icon: URL(string: $0.iconUrl)!,
                            github: URL(string: $0.profileUrl)!
                        )
                    }
                }
                .eraseToThrowingStream()
        }
    )
}

extension SponsorsClient: DependencyKey {
    public static let liveValue: SponsorsClient = .init(
        streamSponsors: {
            sponsorsRepository
                .getSponsorStream()
                .map{
                    $0.map {
                        let plan = switch $0.plan {
                        case .platinum: Model.Sponsor.Plan.platinum
                        case .gold: Model.Sponsor.Plan.gold
                        case .supporter: Model.Sponsor.Plan.supporter
                        }
                        return Model.Sponsor(
                            id: $0.name,
                            logo: URL(string: $0.logo)!,
                            link: URL(string: $0.link)!, 
                            plan: plan
                        )
                    }
                }
                .eraseToThrowingStream()
        }
    )
}

extension ContributorClient: DependencyKey {
    public static let liveValue: ContributorClient = Self {
        contributorRepository
            .getContributorStream()
            .map {
                $0.map {
                    Model.Contributor(
                        id: Int($0.id),
                        userName: $0.username,
                        profileUrl: $0.profileUrl.map { URL(string: $0)! } ,
                        iconUrl: URL(string: $0.iconUrl)!
                    )
                }
            }
            .eraseToThrowingStream()
    } refresh: {
        try await contributorRepository.refresh()
    }
}

extension EventMapClient: DependencyKey {
    public static let liveValue: EventMapClient = .init {
        eventMapRepository.getEventMapStream().eraseToThrowingStream()
    }
}

extension ProfileCardClient: DependencyKey {
    public static let liveValue: ProfileCardClient = .init { param in
        try await profileCardRepository.save(
            profileCard: .init(
                nickname: param.nickname,
                occupation: param.occupation,
                link: param.link,
                image: param.image,
                cardType: param.cardType
            )
        )
    }
}
