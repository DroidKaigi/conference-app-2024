import Dependencies
import shared
import Model
import Foundation
import Firebase
import KMPClient

extension FirebaseAppClient: DependencyKey {
    public static var liveValue: Self {
        Self(
            prepareFirebase: {
                FirebaseApp.configure()
            }
        )
    }
}

extension ContainerClient: DependencyKey {
    public static var liveValue: Self {
        Self(
            repositories: {
                Container.shared.get(type: (any Repositories).self)
            }
        )
    }
}

extension TimetableClient: DependencyKey {
    private static var sessionsRepository: any SessionsRepository {
        Container.shared.get(type: (any SessionsRepository).self)
    }

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
    private static var staffRepository: any StaffRepository {
        Container.shared.get(type: (any StaffRepository).self)
    }

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
    private static var sponsorsRepository: any SponsorsRepository {
        Container.shared.get(type: (any SponsorsRepository).self)
    }

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
    private static var contributorRepository: any ContributorsRepository {
        Container.shared.get(type: (any ContributorsRepository).self)
    }

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
    private static var eventMapRepository: any EventMapRepository {
        Container.shared.get(type: (any EventMapRepository).self)
    }

    public static let liveValue: EventMapClient = .init {
        eventMapRepository
            .getEventMapStream()
            .map {
                $0.map {
                    Model.EventMapEvent(
                        name: .init(
                            currentLangTitle: $0.name.currentLangTitle,
                            enTitle: $0.name.enTitle,
                            jaTitle: $0.name.jaTitle
                        ),
                        roomName: .init(
                            currentLangTitle: $0.roomName.currentLangTitle,
                            enTitle: $0.roomName.enTitle,
                            jaTitle: $0.roomName.jaTitle
                        ),
                        roomIcon: RoomIcon(rawValue: $0.roomIcon.name.lowercased()) ?? .none,
                        description: .init(
                            currentLangTitle: $0.description_.currentLangTitle,
                            enTitle: $0.description_.enTitle,
                            jaTitle: $0.description_.jaTitle
                        ),
                        moreDetailsUrl: $0.moreDetailsUrl.flatMap(URL.init(string:)),
                        message: $0.message.map {
                            .init(
                                currentLangTitle: $0.currentLangTitle,
                                enTitle: $0.enTitle,
                                jaTitle: $0.jaTitle
                            )
                        }
                    )
                }
            }
            .eraseToThrowingStream()
    }
}

extension ProfileCardClient: DependencyKey {
    private static var profileCardRepository: any ProfileCardRepository {
        Container.shared.get(type: (any ProfileCardRepository).self)
    }

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
