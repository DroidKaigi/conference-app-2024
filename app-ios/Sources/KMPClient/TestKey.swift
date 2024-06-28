import Dependencies

extension TimetableClient: TestDependencyKey {
    public static let previewValue: Self = Self()
    public static let testValue: Self = Self()
}

extension StaffClient: TestDependencyKey {
    public static let previewValue: Self = Self()
    public static let testValue: Self = .init {
        AsyncThrowingStream {
            $0.yield([.init(id: 0, username: "testValue", profileUrl: "https://2024.droidkaigi.jp/", iconUrl: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4"),])
            $0.finish()
        }
    }
}

extension SponsorsClient: TestDependencyKey {
    public static let previewValue: Self = Self()
    public static let testValue: Self = Self()
}
