import ComposableArchitecture

@Reducer
public struct AboutReducer {
    public init() { }
    
    @ObservableState
    public struct State: Equatable {
        @Presents var destination: Destination.State?
        
        public init() {}
    }

    public enum Action: ViewAction {
        case view(View)
        case presentation(PresentationAction<Destination.Action>)
        case delegate(Delegate)
        
        @CasePathable
        public enum View {
            case staffsTapped
            case contributorsTapped
            case sponsorsTapped
            case codeOfConductTapped
            case acknowledgementsTapped
            case privacyPolicyTapped
            case youtubeTapped
            case xcomTapped
            case mediumTapped
            case switchComposeModeTapped
        }
        
        @CasePathable
        public enum Delegate {
            case switchComposeMode
        }
        
        @CasePathable
        public enum Alert: Equatable {
            case switchComposeMode
        }
    }
    
    @Reducer(state: .equatable)
    public enum Destination {
        case codeOfConduct
        case privacyPolicy
        case youtube
        case xcom
        case medium
        case alert(AlertState<AboutReducer.Action.Alert>)
    }

    public var body: some ReducerOf<Self> {
        Reduce { state, action in
            switch action {
            case .view(.codeOfConductTapped):
                state.destination = .codeOfConduct
                return .none
            case .view(.privacyPolicyTapped):
                state.destination = .privacyPolicy
                return .none
            case .view(.youtubeTapped):
                state.destination = .youtube
                return .none
            case .view(.xcomTapped):
                state.destination = .xcom
                return .none
            case .view(.mediumTapped):
                state.destination = .medium
                return .none
            case .view(.switchComposeModeTapped):
                state.destination = .alert(AlertState(title: {
                    TextState("SwitchComposeModeAlertTitle", bundle: .module)
                }, actions: {
                    ButtonState(action: .switchComposeMode) {
                        TextState("Switch", bundle: .module)
                    }
                    ButtonState(role: .cancel) {
                        TextState("Cancel", bundle: .module)
                    }
                }, message: {
                    TextState("SwitchComposeModeAlertMessage", bundle: .module)
                }))
                return .none
            case .view:
                return .none
            case .presentation(.dismiss):
                state.destination = nil
                return .none
            case .presentation(.presented(.alert(.switchComposeMode))):
                return .send(.delegate(.switchComposeMode))
            case .presentation:
                return .none
            case .delegate:
                return .none
            }
        }
    }
}
