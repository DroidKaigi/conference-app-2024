import Dependencies

extension EventKitClient: TestDependencyKey {
    public static let previewValue: Self = Self()

    public static let testValue: Self = Self(
        requestAccessIfNeeded: unimplemented("EventKitClient.requestAccessIfNeeded"),
        addEvent: unimplemented("EventKitClient.addEvent")
    )
}
