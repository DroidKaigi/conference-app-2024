import ComposableArchitecture

@Reducer
public struct FavoriteReducer {
    public init() { }

    @ObservableState
    public struct State: Equatable {
        public var text: String

        public init(text: String = "") {
            self.text = text
        }
    }

    public enum Action {
        case onAppear
    }

    public var body: some ReducerOf<Self> {
        Reduce { state, action in
            switch action {
            case .onAppear:
                state.text = "Favorite Feature"
                return .none
            }
        }
    }
}

