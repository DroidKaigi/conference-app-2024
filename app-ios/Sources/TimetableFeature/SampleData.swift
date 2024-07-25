import Foundation
import SwiftUI
import Theme

public enum DayTab: String, CaseIterable, Identifiable {
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
    public let items: [TimetableItem]

    public init(startsTimeString: String, endsTimeString: String, items: [TimetableItem]) {
        self.startsTimeString = startsTimeString
        self.endsTimeString = endsTimeString
        self.items = items
    }
}

public enum Room: String {
    public var id : RawValue { rawValue }
    
    case allRooms = ""
    case giraffe = "Giraffe"
    case hedgeHog = "Hedgehog"
    case flamingo = "Flamingo"
    case jellyfish = "Jellyfish"
    case iguana = "Iguana"
    
    public func getBackgroundColor() -> Color {
        switch(self) {
        case .allRooms:
            return AssetColors.Surface.surfaceContainer.swiftUIColor
        case .giraffe:
            return AssetColors.Custom.bumblebeeContainer.swiftUIColor
        case .hedgeHog:
            return AssetColors.Custom.chipmunkContainer.swiftUIColor
        case .flamingo:
            return AssetColors.Custom.dolphinContainer.swiftUIColor
        case .jellyfish:
            return AssetColors.Custom.electricEelContainer.swiftUIColor
        case .iguana:
            return AssetColors.Custom.arcticFoxContainer.swiftUIColor
        }
    }
    
    public func getForegroundColor() -> Color {
        switch(self) {
        case .allRooms:
            return AssetColors.Surface.onSurface.swiftUIColor
        case .giraffe:
            return AssetColors.Custom.bumblebee.swiftUIColor
        case .hedgeHog:
            return AssetColors.Custom.chipmunk.swiftUIColor
        case .flamingo:
            return AssetColors.Custom.dolphin.swiftUIColor
        case .jellyfish:
            return AssetColors.Custom.electricEel.swiftUIColor
        case .iguana:
            return AssetColors.Custom.arcticFox.swiftUIColor
        }
    }
}

public struct TimetableItem: Identifiable, Equatable, Hashable {
    public let id: String //Not used yet
    let title: String
    let startsAt: Date
    let endsAt: Date
    let category: String
    let sessionType: String
    let room: Room
    let targetAudience: String
    let languages: [String]
    let asset: String
    let levels: [String]
    let speakers: [String]
    
    let isFavorite: Bool
    
    //TODO: This object is likely to change a lot when we get live data changes
}

struct SampleData {
    let workdayResults = [
        TimetableTimeGroupItems(startsTimeString:"12:00", endsTimeString:"13:00", items: [
            TimetableItem(
                id: "",
                title: "DroidKaigiアプリで見るアーキテクチャの変遷",
                startsAt: try! Date("2024-09-11T12:00:00Z", strategy: .iso8601),
                endsAt: try! Date("2024-09-11T13:00:00Z", strategy: .iso8601),
                category: "",
                sessionType: "",
                room: Room.iguana, targetAudience: "", languages: ["EN", "JA"],
                asset:"", levels: [""],
                speakers: ["Maria Rodriguez"],
                isFavorite:false
            ),
            TimetableItem(
                id: "",
                title: "DroidKaigiアプリで見るアーキテクチャの変遷",
                startsAt: try! Date("2024-09-11T12:00:00Z", strategy: .iso8601),
                endsAt: try! Date("2024-09-11T13:00:00Z", strategy: .iso8601),
                category: "",
                sessionType: "",
                room: Room.iguana, targetAudience: "", languages: ["EN", "JA"],
                asset:"", levels: [""],
                speakers: ["Maria Rodriguez"],
                isFavorite:false
             ),
             TimetableItem(
                id: "",
                title: "DroidKaigiアプリで見るアーキテクチャの変遷",
                startsAt: try! Date("2024-09-11T12:00:00Z", strategy: .iso8601),
                endsAt: try! Date("2024-09-11T13:00:00Z", strategy: .iso8601),
                category: "",
                sessionType: "",
                room: Room.iguana, targetAudience: "", languages: ["EN", "JA"],
                asset:"", levels: [""],
                speakers: ["Maria Rodriguez"],
                isFavorite:false)
             ]),
        TimetableTimeGroupItems(startsTimeString:"13:00", endsTimeString:"14:00", items: [
            TimetableItem(
                id: "",
                title: "DroidKaigiアプリで見るアーキテクチャの変遷",
                startsAt: try! Date("2024-09-11T13:00:00Z", strategy: .iso8601),
                endsAt: try! Date("2024-09-11T14:00:00Z", strategy: .iso8601),
                category: "",
                sessionType: "",
                room: Room.iguana, targetAudience: "", languages: ["EN", "JA"],
                asset:"", levels: [""],
                speakers: ["Maria Rodriguez"],
                isFavorite:false
            ),
            TimetableItem(
                id: "",
                title: "DroidKaigiアプリで見るアーキテクチャの変遷",
                startsAt: try! Date("2024-09-11T13:00:00Z", strategy: .iso8601),
                endsAt: try! Date("2024-09-11T14:00:00Z", strategy: .iso8601),
                category: "",
                sessionType: "",
                room: Room.iguana, targetAudience: "", languages: ["EN", "JA"],
                asset:"", levels: [""],
                speakers: ["Maria Rodriguez"],
                isFavorite:false
             ),
             TimetableItem(
                id: "",
                title: "DroidKaigiアプリで見るアーキテクチャの変遷",
                startsAt: try! Date("2024-09-11T13:00:00Z", strategy: .iso8601),
                endsAt: try! Date("2024-09-11T14:00:00Z", strategy: .iso8601),
                category: "",
                sessionType: "",
                room: Room.iguana, targetAudience: "", languages: ["EN", "JA"],
                asset:"", levels: [""],
                speakers: ["Maria Rodriguez"],
                isFavorite:false)
        ])
    ]
    
    let day1Results = [
        TimetableTimeGroupItems(startsTimeString:"12:00", endsTimeString:"13:00", items: [
            TimetableItem(
                id: "",
                title: "A Beginner's Guide to Understanding the Latest Android Technology",
                startsAt: try! Date("2024-09-12T12:00:00Z", strategy: .iso8601),
                endsAt: try! Date("2024-09-12T13:00:00Z", strategy: .iso8601),
                category: "",
                sessionType: "",
                room: Room.iguana, targetAudience: "", languages: ["EN", "JA"],
                asset:"", levels: [""],
                speakers: ["Kelvin Lueilwitz"],
                isFavorite:false),
            TimetableItem(
                id: "",
                title: "How to Stay Updated on the Newest Android Technology Innovations",
                startsAt: try! Date("2024-09-12T12:00:00Z", strategy: .iso8601),
                endsAt: try! Date("2024-09-12T13:00:00Z", strategy: .iso8601),
                category: "",
                sessionType: "",
                room: Room.iguana, targetAudience: "", languages: ["EN", "JA"],
                asset:"", levels: [""],
                speakers: ["Mallory Turner"],
                isFavorite:false),
            TimetableItem(
                id: "",
                title: "The Ultimate Guide to Exploring the Latest Android Technology Features",
                startsAt: try! Date("2024-09-12T12:00:00Z", strategy: .iso8601),
                endsAt: try! Date("2024-09-12T13:00:00Z", strategy: .iso8601),
                category: "",
                sessionType: "",
                room: Room.iguana, targetAudience: "", languages: ["EN", "JA"],
                asset:"", levels: [""],
                speakers: ["Clementina Mills"],
                isFavorite:false)
        ]),
        TimetableTimeGroupItems(startsTimeString:"13:00", endsTimeString:"14:00", items: [
            TimetableItem(
                id: "",
                title: "A Beginner's Guide to Understanding the Latest Android Technology",
                startsAt: try! Date("2024-09-12T13:00:00Z", strategy: .iso8601),
                endsAt: try! Date("2024-09-12T14:00:00Z", strategy: .iso8601),
                category: "",
                sessionType: "",
                room: Room.iguana, targetAudience: "", languages: ["EN", "JA"],
                asset:"", levels: [""],
                speakers: ["Kelvin Lueilwitz"],
                isFavorite:false),
            TimetableItem(
                id: "",
                title: "How to Stay Updated on the Newest Android Technology Innovations",
                startsAt: try! Date("2024-09-12T13:00:00Z", strategy: .iso8601),
                endsAt: try! Date("2024-09-12T14:00:00Z", strategy: .iso8601),
                category: "",
                sessionType: "",
                room: Room.iguana, targetAudience: "", languages: ["EN", "JA"],
                asset:"", levels: [""],
                speakers: ["Mallory Turner"],
                isFavorite:false),
            TimetableItem(
                id: "",
                title: "The Ultimate Guide to Exploring the Latest Android Technology Features",
                startsAt: try! Date("2024-09-12T13:00:00Z", strategy: .iso8601),
                endsAt: try! Date("2024-09-12T14:00:00Z", strategy: .iso8601),
                category: "",
                sessionType: "",
                room: Room.iguana, targetAudience: "", languages: ["EN", "JA"],
                asset:"", levels: [""],
                speakers: ["Clementina Mills"],
                isFavorite:false)
        ])
    ]
    
    let day2Results = [
        TimetableTimeGroupItems(startsTimeString:"12:00", endsTimeString:"13:00", items: [
            TimetableItem(
                id: "",
                title: "Why Should You Invest in the Newest Android Technology?",
                startsAt: try! Date("2024-09-13T12:00:00Z", strategy: .iso8601),
                endsAt: try! Date("2024-09-13T13:00:00Z", strategy: .iso8601),
                category: "",
                sessionType: "",
                room: Room.iguana, targetAudience: "", languages: ["EN", "JA"],
                asset:"", levels: [""],
                speakers: ["Waldo Torp"],
                isFavorite:false),
            TimetableItem(
                id: "",
                title: "5 Ways the Newest Android Technology is Changing the Tech Industry",
                startsAt: try! Date("2024-09-13T12:00:00Z", strategy: .iso8601),
                endsAt: try! Date("2024-09-13T13:00:00Z", strategy: .iso8601),
                category: "",
                sessionType: "",
                room: Room.iguana, targetAudience: "", languages: ["EN", "JA"],
                asset:"", levels: [""],
                speakers: ["Baron Stracke"],
                isFavorite:false),
            TimetableItem(
                id: "",
                title: "The Ultimate List of Resources for Learning More About the Latest Android Technology",
                startsAt: try! Date("2024-09-13T12:00:00Z", strategy: .iso8601),
                endsAt: try! Date("2024-09-13T13:00:00Z", strategy: .iso8601),
                category: "",
                sessionType: "",
                room: Room.iguana, targetAudience: "", languages: ["EN", "JA"],
                asset:"", levels: [""],
                speakers: ["Carole Volkman"],
                isFavorite:false)
        ]),
        TimetableTimeGroupItems(startsTimeString:"13:00", endsTimeString:"14:00", items: [
            TimetableItem(
                id: "",
                title: "Why Should You Invest in the Newest Android Technology?",
                startsAt: try! Date("2024-09-13T13:00:00Z", strategy: .iso8601),
                endsAt: try! Date("2024-09-13T14:00:00Z", strategy: .iso8601),
                category: "",
                sessionType: "",
                room: Room.iguana, targetAudience: "", languages: ["EN", "JA"],
                asset:"", levels: [""],
                speakers: ["Waldo Torp"],
                isFavorite:false),
            TimetableItem(
                id: "",
                title: "5 Ways the Newest Android Technology is Changing the Tech Industry",
                startsAt: try! Date("2024-09-13T13:00:00Z", strategy: .iso8601),
                endsAt: try! Date("2024-09-13T14:00:00Z", strategy: .iso8601),
                category: "",
                sessionType: "",
                room: Room.iguana, targetAudience: "", languages: ["EN", "JA"],
                asset:"", levels: [""],
                speakers: ["Baron Stracke"],
                isFavorite:false),
            TimetableItem(
                id: "",
                title: "The Ultimate List of Resources for Learning More About the Latest Android Technology",
                startsAt: try! Date("2024-09-13T13:00:00Z", strategy: .iso8601),
                endsAt: try! Date("2024-09-13T14:00:00Z", strategy: .iso8601),
                category: "",
                sessionType: "",
                room: Room.iguana, targetAudience: "", languages: ["EN", "JA"],
                asset:"", levels: [""],
                speakers: ["Carole Volkman"],
                isFavorite:false)
        ])
    ]
}
