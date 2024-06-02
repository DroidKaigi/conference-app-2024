import Dependencies
import shared

private var sessionsRepository: any SessionsRepository {
    Container.shared.get(type: (any SessionsRepository).self)
}

extension KMPClient: DependencyKey {
    public static let liveValue: KMPClient = .init(
        fetchTimetable: {
            sessionsRepository.getTimetableStream().eraseToThrowingStream()
        }
    )
}
