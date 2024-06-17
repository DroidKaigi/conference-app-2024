import ComposableArchitecture

@Reducer
public struct AboutReducer {
    public init() { }
    
    @ObservableState
    public struct State: Equatable {
        var path = StackState<Path.State>()
        @Presents var presentation: Presentation.State?
        
        public init(path: StackState<Path.State> = .init()) {
          self.path = path
        }
    }

    public enum Action: ViewAction {
        case path(StackAction<Path.State, Path.Action>)
        case view(View)
        case presentation(PresentationAction<Presentation.Action>)
        
        @CasePathable
        public enum View {
            case staffsTapped
            case contributersTapped
            case sponsorsTapped
            case codeOfConductTapped
            case acknowledgementsTapped
            case privacyPolicyTapped
        }
    }

    @Reducer(state: .equatable)
    public enum Presentation {
        case codeOfConduct
        case privacyPolicy
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
            case .view(.codeOfConductTapped):
                state.presentation = .codeOfConduct
                return .none
            case .view(.acknowledgementsTapped):
                state.path.append(.acknowledgements)
                return .none
            case .view(.privacyPolicyTapped):
                state.presentation = .privacyPolicy
                return .none
            case .presentation:
                return .none
            case .path:
                return .none
            }
        }
        .forEach(\.path, action: \.path)
    }
}
