import ComposableArchitecture

@Reducer
public struct SponsorReducer {
    public init() { }
    
    @ObservableState
    public struct State: Equatable {
        var text: String

        public init(text: String) {
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
                state.text = "Sponsor Feature"
                return .none
            }
        }
    }
}
