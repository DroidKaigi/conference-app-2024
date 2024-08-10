import ComposableArchitecture

@Reducer
public struct EventMapReducer {
    public init() { }
    
    @ObservableState
    public struct State: Equatable {
        public var selectedFloorMap: FloorMap = .first
        
        public init() { }
    }
    
    public enum Action {
        case view(View)

        @CasePathable
        public enum View {
            case selectFloorMap(FloorMap)
        }
    }
    
    public var body: some ReducerOf<Self> {
        Reduce { state, action in
            switch action {
            case let .view(.selectFloorMap(floor)):
                state.selectedFloorMap = floor
                return .none
            }
        }
    }
}
