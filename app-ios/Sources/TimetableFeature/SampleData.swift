import Foundation
import shared

public enum DayTab: String, CaseIterable, Identifiable, Sendable {
    public var id : RawValue { rawValue }
    
    case workshopDay = "WorkshopDay"
    case day1 = "Day 1"
    case day2 = "Day 2"
}

public enum TimetableMode {
    case list
    case grid
}

public struct TimetableTimeGroupItems: Identifiable, Equatable, Hashable {
    public var id: String {
        UUID().uuidString
    }

    public let startsTimeString: String
    public let endsTimeString: String
    public var items: [shared.TimetableItem]

    public init(startsTimeString: String, endsTimeString: String, items: [shared.TimetableItem]) {
        self.startsTimeString = startsTimeString
        self.endsTimeString = endsTimeString
        self.items = items
    }
}

struct SampleData {
    let workdayResults = [
        TimetableTimeGroupItems(startsTimeString:"12:00", endsTimeString:"13:00", items: [
            shared.TimetableItem.Session.companion.fake(),
            shared.TimetableItem.Session.companion.fake(),
            shared.TimetableItem.Session.companion.fake()]),
        TimetableTimeGroupItems(startsTimeString:"13:00", endsTimeString:"14:00", items: [
            shared.TimetableItem.Session.companion.fake(),
            shared.TimetableItem.Session.companion.fake(),
            shared.TimetableItem.Session.companion.fake()]),
    ]
    
    let day1Results = [
        TimetableTimeGroupItems(startsTimeString:"12:00", endsTimeString:"13:00", items: [
            shared.TimetableItem.Session.companion.fake(),
            shared.TimetableItem.Session.companion.fake(),
            shared.TimetableItem.Session.companion.fake()]),
        TimetableTimeGroupItems(startsTimeString:"13:00", endsTimeString:"14:00", items: [
            shared.TimetableItem.Session.companion.fake(),
            shared.TimetableItem.Session.companion.fake(),
            shared.TimetableItem.Session.companion.fake()]),
    ]
    
    let day2Results = [
        TimetableTimeGroupItems(startsTimeString:"12:00", endsTimeString:"13:00", items: [
            shared.TimetableItem.Session.companion.fake(),
            shared.TimetableItem.Session.companion.fake(),
            shared.TimetableItem.Session.companion.fake()]),
        TimetableTimeGroupItems(startsTimeString:"13:00", endsTimeString:"14:00", items: [
            shared.TimetableItem.Session.companion.fake(),
            shared.TimetableItem.Session.companion.fake(),
            shared.TimetableItem.Session.companion.fake()]),
    ]
}
