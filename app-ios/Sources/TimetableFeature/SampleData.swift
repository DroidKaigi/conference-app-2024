import Foundation
import SwiftUI
import Theme
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
    public var items: [shared.TimetableItemWithFavorite]

    public init(startsTimeString: String, endsTimeString: String, items: [shared.TimetableItemWithFavorite]) {
        self.startsTimeString = startsTimeString
        self.endsTimeString = endsTimeString
        self.items = items
    }
}

//public enum Room: String {
//    public var id : RawValue { rawValue }
//    
//    case allRooms = ""
//    case giraffe = "Giraffe"
//    case hedgeHog = "Hedgehog"
//    case flamingo = "Flamingo"
//    case jellyfish = "Jellyfish"
//    case iguana = "Iguana"
//    
//    public func getBackgroundColor() -> Color {
//        switch(self) {
//        case .allRooms:
//            return AssetColors.Surface.surfaceContainer.swiftUIColor
//        case .giraffe:
//            return AssetColors.Custom.bumblebeeContainer.swiftUIColor
//        case .hedgeHog:
//            return AssetColors.Custom.chipmunkContainer.swiftUIColor
//        case .flamingo:
//            return AssetColors.Custom.dolphinContainer.swiftUIColor
//        case .jellyfish:
//            return AssetColors.Custom.electricEelContainer.swiftUIColor
//        case .iguana:
//            return AssetColors.Custom.arcticFoxContainer.swiftUIColor
//        }
//    }
//    
//    public func getForegroundColor() -> Color {
//        switch(self) {
//        case .allRooms:
//            return AssetColors.Surface.onSurface.swiftUIColor
//        case .giraffe:
//            return AssetColors.Custom.bumblebee.swiftUIColor
//        case .hedgeHog:
//            return AssetColors.Custom.chipmunk.swiftUIColor
//        case .flamingo:
//            return AssetColors.Custom.dolphin.swiftUIColor
//        case .jellyfish:
//            return AssetColors.Custom.electricEel.swiftUIColor
//        case .iguana:
//            return AssetColors.Custom.arcticFox.swiftUIColor
//        }
//    }
//}

//public struct TimetableItem: Identifiable, Equatable, Hashable {
//    public let id: String //Not used yet
//    let title: String
//    let startsAt: Date
//    let endsAt: Date
//    let category: String
//    let sessionType: String
//    let room: Room
//    let targetAudience: String
//    let languages: [String]
//    let asset: String
//    let levels: [String]
//    let speakers: [String]
//    
//    let isFavorite: Bool
//    
//    //TODO: This object is likely to change a lot when we get live data changes
//}

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
