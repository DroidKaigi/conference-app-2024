import ComposableArchitecture

@Reducer
public struct AboutReducer {
    public init() { }
    
    @ObservableState
    public struct State: Equatable {
        var path = StackState<Path.State>()
        var isCodeOfConductSheetPresented = false
        var isPrivacyPolicySheetPresented = false
        
        public init(path: StackState<Path.State> = .init()) {
          self.path = path
        }
    }

    public enum Action: ViewAction {
        case path(StackAction<Path.State, Path.Action>)
        case view(View)
        
        @CasePathable
        public enum View {
            case staffsTapped
            case contributersTapped
            case sponsorsTapped
            case setCodeOfConductSheet(isPresented: Bool)
            case acknowledgementsTapped
            case setPrivacyPolicySheet(isPresented: Bool)
        }
    }

    @Reducer(state: .equatable)
    public enum Path {
        case staffs
        case contributers
        case sponsors
        case acknowledgements
    }

    public var body: some ReducerOf<Self> {
        Reduce { state, action in
            switch action {
            case .view(.staffsTapped):
                state.path.append(.staffs)
                return .none
            case .view(.contributersTapped):
                state.path.append(.contributers)
                return .none
            case .view(.sponsorsTapped):
                state.path.append(.sponsors)
                return .none
            case .view(.setCodeOfConductSheet(let isPresented)):
                state.isCodeOfConductSheetPresented = isPresented
                return .none
            case .view(.acknowledgementsTapped):
                state.path.append(.acknowledgements)
                return .none
            case .view(.setPrivacyPolicySheet(let isPresented)):
                state.isPrivacyPolicySheetPresented = isPresented
                return .none
            case .path:
                return .none
            }
        }
        .forEach(\.path, action: \.path)
    }
}
