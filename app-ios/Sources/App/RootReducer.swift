import ComposableArchitecture
import TimetableFeature

@Reducer
public struct RootReducer {
    public init() {}

    @ObservableState
    public struct State: Equatable {
        public var appDelegate: AppDelegateReducer.State
        public var timetable: TimetableCore.State

        public init(
            appDelegate: AppDelegateReducer.State = .init(),
            timetable: TimetableCore.State = .init()
        ) {
            self.appDelegate = appDelegate
            self.timetable = timetable
        }
    }

    public enum Action {
        case appDelegate(AppDelegateReducer.Action)
        case timetable(TimetableCore.Action)
    }

    public var body: some ReducerOf<Self> {
        Scope(state: \.appDelegate, action: \.appDelegate) {
            AppDelegateReducer()
        }
        Scope(state: \.timetable, action: \.timetable) {
            TimetableCore()
        }
    }
}
