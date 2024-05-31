import DependenciesMacros
import shared

@DependencyClient
public struct KMPClient: Sendable {
    public let fetchTimetable: @Sendable () -> AsyncThrowingStream<Timetable, any Error>
}
