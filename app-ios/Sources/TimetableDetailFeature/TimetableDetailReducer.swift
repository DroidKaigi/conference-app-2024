import ComposableArchitecture
import CommonComponents
import KMPClient
import shared

@Reducer
public struct TimetableDetailReducer: Sendable {
    @Dependency(\.timetableClient) private var timetableClient

    public init() {}
    
    @ObservableState
    public struct State: Equatable {
        public init(isBookmarked: Bool = false, toast: ToastState? = nil) {
            self.isBookmarked = isBookmarked
            self.toast = toast
        }

        var isBookmarked = false
        var toast: ToastState?
    }

    public enum Action: BindableAction {
        case binding(BindingAction<State>)
        case view(View)
        case bookmarkResponse(Result<Void, any Error>)

        public enum View {
            case bookmarkButtonTapped
            case shareButtonTapped
            case calendarButtonTapped
            case slideButtonTapped
            case videoButtonTapped
        }
    }
    
    public var body: some ReducerOf<Self> {
        BindingReducer()
        Reduce { state, action in
            enum CancelID { case request }

            switch action {
            case .view(.bookmarkButtonTapped):
                return .run { send in
                    await send(.bookmarkResponse(Result {
                        try await timetableClient.toggleBookmark(id: TimetableItemId(value: ""))
                    }))
                }
                .cancellable(id: CancelID.request)

            case .view(.calendarButtonTapped):
                return .none

            case .view(.shareButtonTapped):
                return .none

            case .view(.slideButtonTapped):
                return .none

            case .view(.videoButtonTapped):
                return .none

            case .bookmarkResponse(.success):
                if !state.isBookmarked {
                    state.toast = .init(text: String(localized: "TimetableDetailAddBookmark", bundle: .module))
                }
                state.isBookmarked.toggle()
                return .none

            case let .bookmarkResponse(.failure(error)):
                print(error)
                return .none

            case .binding:
                return .none
            }
        }
    }
}
