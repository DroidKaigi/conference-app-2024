import ComposableArchitecture
import shared

@Reducer
public struct TimetableCore {
    public init() {}

    @ObservableState
    public struct State: Equatable {
        public var timetableItems: [TimetableItemWithFavorite] = [TimetableItemWithFavorite.companion.fake()]

        public init() {}
    }

    public enum Action {
    }

    public var body: some Reducer<State, Action> {
        Reduce { state, action in
            .none
        }
    }
}
