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

public struct TimetableItem: Equatable, Hashable {
    let room: String
    let languages: [String]
    let title: String
    let speaker: String
    let startTime: String
    let endTime: String
    let isFavorite: Bool
}

public struct SampleData {
    public let day1Data = [TimetableItem(room: "Arctic Fox", languages: ["EN", "JA"],
                                  title: "DroidKaigiアプリで見るアーキテクチャの変遷",
                                  speaker: "Maria Rodriguez",
                                  startTime: "12:00PM",endTime: "13:00PM",isFavorite:false),
                             TimetableItem(room: "Arctic Fox", languages: ["EN", "JA"],
                                 title: "DroidKaigiアプリで見るアーキテクチャの変遷",
                                 speaker: "Maria Rodriguez",
                                 startTime: "12:00PM",endTime: "13:00PM",isFavorite:false),
                             TimetableItem(room: "Arctic Fox", languages: ["EN", "JA"],
                             title: "DroidKaigiアプリで見るアーキテクチャの変遷",
                                 speaker: "Maria Rodriguez",
                                 startTime: "12:00PM",
                                 endTime: "13:00PM",isFavorite:false)
                          ]
    
    public let day2Data = [TimetableItem(room: "Arctic Fox", languages: ["EN", "JA"],
                                  title: "A Beginner's Guide to Understanding the Latest Android Technology",
                                  speaker: "Kelvin Lueilwitz",
                                  startTime: "12:00PM",endTime: "13:00PM",isFavorite:false),
                             TimetableItem(room: "Arctic Fox", languages: ["EN", "JA"],
                                 title: "How to Stay Updated on the Newest Android Technology Innovations",
                                 speaker: "Mallory Turner",
                                 startTime: "12:00PM",endTime: "13:00PM",isFavorite:false),
                             TimetableItem(room: "Arctic Fox", languages: ["EN", "JA"],
                             title: "The Ultimate Guide to Exploring the Latest Android Technology Features",
                                 speaker: "Clementina Mills",
                                 startTime: "12:00PM",
                                 endTime: "13:00PM",isFavorite:false)
                          ]
    
    public let day3Data = [TimetableItem(room: "Arctic Fox", languages: ["EN", "JA"],
                                  title: "Why Should You Invest in the Newest Android Technology?",
                                  speaker: "Waldo Torp",
                                  startTime: "12:00PM",endTime: "13:00PM",isFavorite:false),
                             TimetableItem(room: "Arctic Fox", languages: ["EN", "JA"],
                                 title: "5 Ways the Newest Android Technology is Changing the Tech Industry",
                                 speaker: "Baron Stracke",
                                 startTime: "12:00PM",endTime: "13:00PM",isFavorite:false),
                             TimetableItem(room: "Arctic Fox", languages: ["EN", "JA"],
                             title: "The Ultimate List of Resources for Learning More About the Latest Android Technology",
                                 speaker: "Carole Volkman",
                                 startTime: "12:00PM",
                                 endTime: "13:00PM",isFavorite:false)
                          ]
}

