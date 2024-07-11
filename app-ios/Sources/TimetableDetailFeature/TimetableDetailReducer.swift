import ComposableArchitecture
import CommonComponents

@Reducer
public struct TimetableDetailReducer {
    public init() {}
    
    @ObservableState
    public struct State: Equatable {
        var toast: ToastState?
    }
    
    public enum Action: BindableAction {
        case binding(BindingAction<State>)
        case view(View)

        public enum View {
            case favoriteButtonTapped
        }
    }
    
    public var body: some ReducerOf<Self> {
        BindingReducer()
        Reduce { state, action in
            switch action {
            case .view(.favoriteButtonTapped):
                state.toast = .init(text: String(localized: "TimetableDetailAddBookmark", bundle: .module))
                return .none
            case .binding:
                return .none
            }
        }
    }
}
