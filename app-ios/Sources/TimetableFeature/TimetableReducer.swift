import ComposableArchitecture
import CommonComponents
import KMPClient
import shared
import Foundation
//import EventKitClient

@Reducer
public struct TimetableReducer : Sendable{
    let sampleData = SampleData()
    @Dependency(\.timetableClient) private var timetableClient
    enum CancelID { case connection }
    
    public init() {}

    @ObservableState
    public struct State: Equatable {
        var timetableItems: [TimetableTimeGroupItems] = [] //Should be simple objects
        
        public init(timetableItems: [TimetableTimeGroupItems] = []) {
            self.timetableItems = timetableItems
        }
    }

    public enum Action : Sendable{
        case view(View)
        case onAppear
        case response(Result<[shared.TimetableItem], any Error>)
        
        public enum View : Sendable {
            case selectDay(DayTab)
            case timetableItemTapped
        }
    }

    public var body: some Reducer<State, Action> {
        Reduce { state, action in
            switch action {
            case .onAppear:
                 state.timetableItems = sampleData.workdayResults //TODO: Replace with a "loading" text?
                 return .run { send in
                     for try await timetables in try timetableClient.streamTimetable() {
                         await send(.response(.success(timetables.timetableItems)))
                     }
                 }
                 .cancellable(id: CancelID.connection)
                
            case .response(.success(let timetables)):
                let sortedItems: [(Date, Date, TimetableItem)] = timetables.map {
                    (try! Date($0.startsTimeString, strategy: .iso8601),
                     try! Date($0.endsTimeString, strategy: .iso8601),
                     TimetableItem(
                        id: "", //is there an ID we actually need?
                        title: $0.title.currentLangTitle,
                        startsAt: try! Date($0.startsTimeString, strategy: .iso8601),
                        endsAt: try! Date($0.endsTimeString, strategy: .iso8601),
                        category: $0.category.title.currentLangTitle,
                        sessionType: $0.sessionType.name,
                        room: $0.room.name.currentLangTitle,
                        targetAudience: $0.targetAudience.localizedLowercase,
                        languages: $0.language.labels,
                        asset: $0.asset,
                        levels: $0.levels,
                        speakers: $0.speakers.map {$0.name},
                        isFavorite: false //TODO: May need to pull this info separately
                     ))
                }
                
                let myDict = sortedItems.reduce(into: [Date: TimetableTimeGroupItems]()) {
                    if $0[$1.0] == nil {
                        $0[$1.0] = TimetableTimeGroupItems(
                            startsTimeString:$1.0.formatted(),
                            endsTimeString:$1.1.formatted(),
                            items:[]
                        )
                    }
                    $0[$1.0]?.items.append($1.2)
                }
                
                //TODO: this filter shouldn't be necessary but state.timetableItems = myDict.values generates an assignment error
                state.timetableItems = myDict.values.filter {$0 != nil}
                
                return .none
            case .response(.failure(let error)):
                print(error)
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



