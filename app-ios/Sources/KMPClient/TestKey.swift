import Dependencies

extension TimetableClient: TestDependencyKey {
    public static let previewValue: Self = Self()
    public static let testValue: Self = Self()
}

extension StaffClient: TestDependencyKey {
    public static let previewValue: Self = .init {
        .init {
            [
                .init(id: 0, username: "Hoge", profileUrl: .init(), iconUrl: .init()),
                
            ]
        }
    }
    public static let testValue: Self = Self()
}

extension SponsorsClient: TestDependencyKey {
    public static let previewValue: Self = Self()
    public static let testValue: Self = Self()
}
