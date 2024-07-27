import ComposableArchitecture
import CommonComponents
import KMPClient
import shared
import Foundation
//import EventKitClient

@Reducer
public struct TimetableReducer {
    let sampleData = SampleData()
    @Dependency(\.timetableClient) private var timetableClient
    
    public init() {}

    @ObservableState
    public struct State: Equatable {
        var timetableItems: [TimetableTimeGroupItems] = [] //Should be simple objects
        
        public init(timetableItems: [TimetableTimeGroupItems] = []) {
            self.timetableItems = timetableItems
        }
    }

    public enum Action {
        case view(View)
        case onAppear
        
        public enum View {
            case selectDay(DayTab)
            case timetableItemTapped
        }
    }

    public var body: some Reducer<State, Action> {
        Reduce { state, action in
            switch action {
            case .onAppear:
                state.timetableItems = timetableClient.streamTimetable //sampleData.workdayResults
                return .none
            case .view(.timetableItemTapped):
                return .none
            case .view(.selectDay(let dayTab)):
                //TODO: Replace with real data
                
                switch dayTab {
                case .workshopDay:
                    state.timetableItems = sampleData.workdayResults
                    return .none
                case .day1:
                    state.timetableItems = sampleData.day1Results
                    return .none
                case .day2:
                    state.timetableItems = sampleData.day2Results
                    return .none
                }
            }
        }
    }
}



