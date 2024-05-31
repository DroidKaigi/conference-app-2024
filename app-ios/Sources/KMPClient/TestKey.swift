import Dependencies

extension DependencyValues {
    public var kmpClient: KMPClient {
        get { self[KMPClient.self] }
        set { self[KMPClient.self] = newValue }
    }
}

extension KMPClient: TestDependencyKey {
    public static let previewValue: KMPClient = .noop
}

extension KMPClient {
    public static let noop = Self(
        fetchTimetable: {
            .never
        }
    )
}
