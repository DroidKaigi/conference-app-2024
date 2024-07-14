import ComposableArchitecture
import CommonComponents
import KMPClient
import shared
import Foundation
import EventKitClient

@Reducer
public struct TimetableDetailReducer: Sendable {
    @Dependency(\.timetableClient) private var timetableClient
    @Dependency(\.eventKitClient) private var eventKitClient

    public init() {}
    
    @ObservableState
    public struct State: Equatable {
        public init(timetableItem: TimetableItem, isBookmarked: Bool = false) {
            self.timetableItem = timetableItem
            self.isBookmarked = isBookmarked
            self.toast = toast
        }

        var timetableItem: TimetableItem
        var isBookmarked = false
        var toast: ToastState?
        var tappedUrl: IdentifiableURL?
        @Presents var confirmationDialog: ConfirmationDialogState<ConfirmationDialog>?
    }

    public enum Action: BindableAction {
        case binding(BindingAction<State>)
        case view(View)
        case confirmationDialog(PresentationAction<ConfirmationDialog>)
        case bookmarkResponse(Result<Void, any Error>)
        case requestEventAccessResponse(Result<Bool, any Error>)
        case addEventResponse(Result<Void, any Error>)

        public enum View {
            case bookmarkButtonTapped
            case calendarButtonTapped
            case slideButtonTapped
            case videoButtonTapped
            case urlTapped(URL)
        }
    }
    
    @CasePathable
    public enum ConfirmationDialog {
        case addEvent
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
                return .run { send in
                    await send(.requestEventAccessResponse(Result {
                        try await eventKitClient.requestAccessIfNeeded()
                    }))
                }
                
            case let .bookmarkResponse(.failure(error)):
                print(error.localizedDescription)
                return .none

            case let .requestEventAccessResponse(.success(isAccessAllowed)):
                if isAccessAllowed {
                    state.confirmationDialog = ConfirmationDialogState(title: {
                        TextState("")
                    }, actions: {
                        ButtonState(action: .addEvent) {
                            TextState(String(localized: "TimetableDetailAddEvent", bundle: .module))
                        }
                    })
                }
                return .none
                
            case let .requestEventAccessResponse(.failure(error)):
                print(error.localizedDescription)
                return .none

            case .view(.slideButtonTapped):
                return .none

            case .view(.videoButtonTapped):
                return .none
                
            case let .view(.urlTapped(url)):
                state.tappedUrl = IdentifiableURL(url)
                return .none

            case .bookmarkResponse(.success):
                if !state.isBookmarked {
                    state.toast = .init(text: String(localized: "TimetableDetailAddBookmark", bundle: .module))
                }
                state.isBookmarked.toggle()
                return .none
                
            case .confirmationDialog(.presented(.addEvent)):
                return .run { [state] send in
                    await send(.addEventResponse(Result {
                        try eventKitClient.addEvent(
                            title: state.timetableItem.title.currentLangTitle,
                            startDate: state.timetableItem.startsAt.toDate(),
                            endDate: state.timetableItem.endsAt.toDate()
                        )
                    }))
                }

            case .addEventResponse(.success):
                return .none
                
            case let .addEventResponse(.failure(error)):
                print(error.localizedDescription)
                return .none

            case .binding:
                return .none
                
            case .confirmationDialog:
                return .none
            }
        }
        .ifLet(\.$confirmationDialog, action: \.confirmationDialog)
    }
}
