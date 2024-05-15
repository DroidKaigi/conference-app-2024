import ComposableArchitecture
import SessionFeature
import TimetableFeature

@Reducer
public struct RootCore {
    public init() {}

    @ObservableState
    public struct State: Equatable {
        public var timetable: TimetableCore.State

        public init(
            timetable: TimetableCore.State = .init()
        ) {
            self.timetable = timetable
        }
    }

    public enum Action {
        case timetable(TimetableCore.Action)
    }

    public var body: some Reducer<State, Action> {
        Scope(state: \.timetable, action: \.timetable) {
            TimetableCore()
        }
    }
}
