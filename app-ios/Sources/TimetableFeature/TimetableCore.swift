import ComposableArchitecture
import shared

@Reducer
public struct TimetableCore {
    @ObservableState
    public struct State: Equatable {
        public var timetableItems: [TimetableItemWithFavorite] = []
    }

    public enum Action {}

    public var body: some Reducer<State, Action> {
        Reduce { state, action in
            .none
        }
    }
}
