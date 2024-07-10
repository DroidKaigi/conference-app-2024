import ComposableArchitecture
import CommonComponents

@Reducer
public struct TimetableDetailReducer {
    public init() {}
    
    @ObservableState
    public struct State: Equatable {
        var toast: ToastState?
    }
    
    public enum Action {
        case view(View)
        case setToast(ToastState?)

        public enum View {
            case favoriteButtonTapped
        }
    }
    
    public var body: some ReducerOf<Self> {
        Reduce { state, action in
            switch action {
            case .view(.favoriteButtonTapped):
                state.toast = .init(text: String(localized: "TimetableDetailAddBookmark", bundle: .module))
                return .none
            case let .setToast(toast):
                state.toast = toast
                return .none
            }
        }
    }
}
