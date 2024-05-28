import ComposableArchitecture

@Reducer
public struct TimetableDetailReducer {
    public init() {}
    
    @ObservableState
    public struct State: Equatable {
        var title: String
    }
    
    public enum Action {
        case onAppear
    }
    
    public var body: some ReducerOf<Self> {
        Reduce { state, action in
            switch action {
            case .onAppear:
                state.title = "Timetable Detail"
                return .none
            }
        }
    }
}
