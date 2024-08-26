import Foundation
import CommonComponents
import shared

public enum DayTab: String, CaseIterable, Identifiable, Sendable {
    public var id : RawValue { rawValue }
    
    case day1 = "9/12"
    case day2 = "9/13"
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
    public var items: [shared.TimetableItemWithFavorite]

    public init(startsTimeString: String, endsTimeString: String, items: [shared.TimetableItemWithFavorite]) {
        self.startsTimeString = startsTimeString
        self.endsTimeString = endsTimeString
        self.items = items
    }
    
    func getItem(for room: RoomType) -> TimetableItemWithFavorite? {
        items.filter {
            $0.timetableItem.room.type == room   //TODO: roomIj handling not decided?
        }.first
    }
    
    func isTopLunch() -> Bool {        
        return (items[0].timetableItem.title.enTitle.lowercased().contains("lunch")
                && items[0].timetableItem.startsTimeString.contains("13:00"))
    }
}

// This exists only for previews now.
struct SampleData {
    let workdayResults = [
        TimetableTimeGroupItems(startsTimeString:"12:00", endsTimeString:"13:00", items: [
            TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false),
            TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false),
            TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false)]),
        TimetableTimeGroupItems(startsTimeString:"13:00", endsTimeString:"14:00", items: [
            TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false),
            TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false),
            TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false)]),
    ]
    
    let day1Results = [
        TimetableTimeGroupItems(startsTimeString:"12:00", endsTimeString:"13:00", items: [
            TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false),
            TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false),
            TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false)]),
        TimetableTimeGroupItems(startsTimeString:"13:00", endsTimeString:"14:00", items: [
            TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false),
            TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false),
            TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false)]),
    ]
    
    let day2Results = [
        TimetableTimeGroupItems(startsTimeString:"12:00", endsTimeString:"13:00", items: [
            TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false),
            TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false),
            TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false)]),
        TimetableTimeGroupItems(startsTimeString:"13:00", endsTimeString:"14:00", items: [
            TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false),
            TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false),
            TimetableItemWithFavorite(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false)]),
    ]
}
