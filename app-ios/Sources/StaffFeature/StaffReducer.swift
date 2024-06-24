import ComposableArchitecture

@Reducer
public struct StaffReducer {
    public init() { }
    
    @ObservableState
    public struct State: Equatable {
        var text: String
    }

    public enum Action {
        case onAppear
    }

    public var body: some ReducerOf<Self> {
        Reduce { state, action in
            switch action {
            case .onAppear:
                state.text = "Staff Feature"
                return .none
            }
        }
    }
}
