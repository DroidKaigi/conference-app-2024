import ComposableArchitecture

@Reducer
public struct TimetableCore {
    public init() {}

    @ObservableState
    public struct State: Equatable {
        public var timetableItems: [String] = ["aaa"]

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
