import Dependencies

extension TimetableClient: TestDependencyKey {
    public static let previewValue: Self = Self()

    public static let testValue: Self = Self(
        streamTimetable: unimplemented("TimetableClient.streamTimetable"),
        streamTimetableItemWithFavorite: unimplemented("TimetableClient.streamTimetableItemWithFavorite"),
        toggleBookmark: unimplemented("TimetableClient.toggleBookmark")
    )
}

extension StaffClient: TestDependencyKey {
    public static let previewValue: Self = Self()
    public static let testValue: Self = Self(streamStaffs: unimplemented("StaffClient.streamStaffs"))
}

extension SponsorsClient: TestDependencyKey {
    public static let previewValue: Self = Self()
    public static let testValue: Self = Self(streamSponsors: unimplemented("SponsorsClient.streamSponsors"))
}

extension ContributorClient: TestDependencyKey {
    public static let previewValue: Self = Self()
    public static let testValue: Self = Self(
        streamContributors: unimplemented("ContributorClient.streamContributors"),
        refresh: unimplemented("ContributorClient.refresh")
    )
}

extension EventMapClient: TestDependencyKey {
    public static let previewValue: Self = Self()
    public static let testValue: Self = Self()
}

extension ProfileCardClient: TestDependencyKey {
    public static let previewValue: Self = Self()
    public static let testValue: Self = Self()
}
