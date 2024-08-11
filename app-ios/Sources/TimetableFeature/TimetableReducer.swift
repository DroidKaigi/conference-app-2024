import ComposableArchitecture
import CommonComponents
import KMPClient
import shared
import Foundation

@Reducer
public struct TimetableReducer : Sendable{
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
        case requestDay(View)
        case response(Result<[TimetableItemWithFavorite], any Error>)
        
        public enum View : Sendable {
            case selectDay(DayTab)
            case timetableItemTapped
            case favoriteItemTapped
            case searchTapped
        }
    }
    
    private func sortListIntoTimeGroups(timetableItems: [TimetableItemWithFavorite]) -> [TimetableTimeGroupItems] {
        let sortedItems: [(Date, Date, TimetableItemWithFavorite)] = timetableItems.map {
            (Date(timeIntervalSince1970: Double($0.timetableItem.startsAt.epochSeconds)),
             Date(timeIntervalSince1970: Double($0.timetableItem.endsAt.epochSeconds)),
            $0)
        }

        let myDict = sortedItems.reduce(into: [Date: TimetableTimeGroupItems]()) {
            if $0[$1.0] == nil {
                $0[$1.0] = TimetableTimeGroupItems(
                    startsTimeString:$1.0.formatted(.dateTime.hour(.twoDigits(amPM: .omitted)).minute()),
                    endsTimeString:$1.1.formatted(.dateTime.hour(.twoDigits(amPM: .omitted)).minute()),
                    items:[]
                )
            }
            $0[$1.0]?.items.append($1.2)
        }
        
        return myDict.values.sorted {
            $0.items[0].timetableItem.startsAt.epochSeconds < $1.items[0].timetableItem.startsAt.epochSeconds
        }
    }

    public var body: some Reducer<State, Action> {
        Reduce { state, action in
            switch action {
            case .onAppear:
                 return .run { send in
                     await send(.requestDay(.selectDay(.day1)))
                 }
            case .requestDay(.selectDay(let dayTab)):
                return .run { send in
                    let internalDay = switch dayTab {
                    case DayTab.workshopDay:
                        DroidKaigi2024Day.workday
                    case DayTab.day1:
                        DroidKaigi2024Day.conferenceDay1
                    case DayTab.day2:
                        DroidKaigi2024Day.conferenceDay2
                    }
                    
                    for try await timetables in try timetableClient.streamTimetable() {
                        await send(.response(.success(timetables.dayTimetable(droidKaigi2024Day: internalDay).contents)))
                    }
                }
                .cancellable(id: CancelID.connection)
            case .response(.success(let timetables)):
                state.timetableItems = sortListIntoTimeGroups(timetableItems: timetables)
                
                return .none
            case .response(.failure(let error)):
                print(error)
                return .none
            case .view(.timetableItemTapped), .view(.searchTapped):
                return .none
            case .view(.favoriteItemTapped):
                return .none
            case .view(.selectDay(let dayTab)):
                
                return .run { send in
                    await send(.requestDay(.selectDay(dayTab)))
                }
            case .requestDay(.timetableItemTapped):
                return .none

            case .requestDay(.searchTapped):
                return .none
            case .requestDay(.favoriteItemTapped):
                return .none
            }
        }
    }
}



