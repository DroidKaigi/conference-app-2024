import ComposableArchitecture
import Foundation

@Reducer
public struct TimetableReducer {
    public init() {}

    @ObservableState
    public struct State: Equatable {
        var timetableItems: [TimetableItem] = [] //Should be simple objects
        
        public init(timetableItems: [TimetableItem]? = []) {
            self.timetableItems = timetableItems ?? []
        }
    }

    public enum Action {
        case onAppear
        case selectDay(DayTab)
    }

    public var body: some Reducer<State, Action> {
        Reduce { state, action in
            switch action {
            case .onAppear:
                state.timetableItems = SampleData.init().day1Data
                return .none
            case .selectDay(let dayTab):
                //TODO: Replace with real data
                
                switch dayTab {
                case .day1:
                    state.timetableItems = SampleData.init().day1Data
                    return .none
                case .day2:
                    state.timetableItems = SampleData.init().day2Data
                    return .none
                case .day3:
                    state.timetableItems = SampleData.init().day3Data
                    return .none
                }
            }
        }
    }
}

public enum DayTab: String, CaseIterable, Identifiable {
    public var id : RawValue { rawValue }
    
    case day1 = "Day1"
    case day2 = "Day2"
    case day3 = "Day3"
}

//public struct TimetableTimeGroupItems: Identifiable, Equatable, Hashable {
//    public var id: String {
//        UUID().uuidString
//    }
//
//    public var startsTimeString: String
//    public var endsTimeString: String
//    public var items: [TimetableItem]
//
//    public init(startsTimeString: String, endsTimeString: String, items: [TimetableItem]) {
//        self.startsTimeString = startsTimeString
//        self.endsTimeString = endsTimeString
//        self.items = items
//    }
//}

public struct TimetableItem: Equatable, Hashable {
    let id: String //Not used yet
    let title: String
    let startsAt: String
    let endsAt: String
    let category: String
    let sessionType: String
    let room: String
    let targetAudience: String
    let languages: [String]
    let asset: String
    let levels: [String]
    let speakers: [String]
    
    let isFavorite: Bool
    
    
    // TODO: The actual class will look more like this
//    public abstract val id: TimetableItemId
//    public abstract val title: MultiLangText
//    public abstract val startsAt: Instant
//    public abstract val endsAt: Instant
//    public abstract val category: TimetableCategory
//    public abstract val sessionType: TimetableSessionType
//    public abstract val room: TimetableRoom
//    public abstract val targetAudience: String
//    public abstract val language: TimetableLanguage
//    public abstract val asset: TimetableAsset
//    public abstract val levels: PersistentList<String>
//    public abstract val speakers: PersistentList<TimetableSpeaker>
}



public struct SampleData {
    public let day1Data = [
    TimetableItem(
        id: "",
        title: "DroidKaigiアプリで見るアーキテクチャの変遷",
        startsAt: "12:00PM", endsAt: "13:00PM",
        category: "",
        sessionType: "",
        room: "Arctic Fox", targetAudience: "", languages: ["EN", "JA"],
        asset:"", levels: [""],
        speakers: ["Maria Rodriguez"],
        isFavorite:false
    ),
    TimetableItem(
        id: "",
        title: "DroidKaigiアプリで見るアーキテクチャの変遷",
        startsAt: "12:00PM", endsAt: "13:00PM",
        category: "",
        sessionType: "",
        room: "Arctic Fox", targetAudience: "", languages: ["EN", "JA"],
        asset:"", levels: [""],
        speakers: ["Maria Rodriguez"],
        isFavorite:false
     ),
     TimetableItem(
        id: "",
        title: "DroidKaigiアプリで見るアーキテクチャの変遷",
        startsAt: "12:00PM", endsAt: "13:00PM",
        category: "",
        sessionType: "",
        room: "Arctic Fox", targetAudience: "", languages: ["EN", "JA"],
        asset:"", levels: [""],
        speakers: ["Maria Rodriguez"],

        isFavorite:false)
     ]
    
    public let day2Data = [
    TimetableItem(
        id: "",
        title: "A Beginner's Guide to Understanding the Latest Android Technology",
        startsAt: "12:00PM", endsAt: "13:00PM",
        category: "",
        sessionType: "",
        room: "Arctic Fox", targetAudience: "", languages: ["EN", "JA"],
        asset:"", levels: [""],
        speakers: ["Kelvin Lueilwitz"],
        isFavorite:false),
    TimetableItem(
        id: "",
        title: "How to Stay Updated on the Newest Android Technology Innovations",
        startsAt: "12:00PM", endsAt: "13:00PM",
        category: "",
        sessionType: "",
        room: "Arctic Fox", targetAudience: "", languages: ["EN", "JA"],
        asset:"", levels: [""],
        speakers: ["Mallory Turner"],
        isFavorite:false),
    TimetableItem(
        id: "",
        title: "The Ultimate Guide to Exploring the Latest Android Technology Features",
        startsAt: "12:00PM", endsAt: "13:00PM",
        category: "",
        sessionType: "",
        room: "Arctic Fox", targetAudience: "", languages: ["EN", "JA"],
        asset:"", levels: [""],
        speakers: ["Clementina Mills"],
        isFavorite:false)
    ]
    
    public let day3Data = [
        TimetableItem(
            id: "",
            title: "Why Should You Invest in the Newest Android Technology?",
            startsAt: "12:00PM", endsAt: "13:00PM",
            category: "",
            sessionType: "",
            room: "Arctic Fox", targetAudience: "", languages: ["EN", "JA"],
            asset:"", levels: [""],
            speakers: ["Waldo Torp"],
            isFavorite:false),
        TimetableItem(
            id: "",
            title: "5 Ways the Newest Android Technology is Changing the Tech Industry",
            startsAt: "12:00PM", endsAt: "13:00PM",
            category: "",
            sessionType: "",
            room: "Arctic Fox", targetAudience: "", languages: ["EN", "JA"],
            asset:"", levels: [""],
            speakers: ["Baron Stracke"],
            isFavorite:false),
        TimetableItem(
            id: "", 
            title: "The Ultimate List of Resources for Learning More About the Latest Android Technology",
            startsAt: "12:00PM", endsAt: "13:00PM",
            category: "",
            sessionType: "",
            room: "Arctic Fox", targetAudience: "", languages: ["EN", "JA"],
            asset:"", levels: [""],
            speakers: ["Carole Volkman"],
            isFavorite:false)
       ]
}

