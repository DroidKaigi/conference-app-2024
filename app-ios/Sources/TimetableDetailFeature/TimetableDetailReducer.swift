import ComposableArchitecture
import CommonComponents
import KMPClient
@preconcurrency import shared
import Foundation
import EventKitClient
import Model

@Reducer
public struct TimetableDetailReducer: Sendable {
    @Dependency(\.timetableClient) private var timetableClient
    @Dependency(\.eventKitClient) private var eventKitClient

    public init() {}
    
    @ObservableState
    public struct State: Equatable {
        public init(timetableItem: TimetableItem, isFavorited: Bool = false) {
            self.timetableItem = timetableItem
            self.isFavorited = isFavorited
            self.toast = toast
        }

        var timetableItem: TimetableItem
        var isFavorited = false
        var toast: ToastState?
        var url: IdentifiableURL?
        @Presents var confirmationDialog: ConfirmationDialogState<ConfirmationDialog>?
    }

    public enum Action: BindableAction, Sendable {
        case binding(BindingAction<State>)
        case view(View)
        case confirmationDialog(PresentationAction<ConfirmationDialog>)
        case favoriteResponse(Result<Void, any Error>)
        case requestEventAccessResponse(Result<Bool, any Error>)
        case addEventResponse(Result<Void, any Error>)

        public enum View: Sendable{
            case favoriteButtonTapped
            case calendarButtonTapped
            case slideButtonTapped(URL)
            case videoButtonTapped(URL)
            case urlTapped(URL)
        }
    }
    
    @CasePathable
    public enum ConfirmationDialog: Sendable {
        case addEvent
    }
    
    public var body: some ReducerOf<Self> {
        BindingReducer()
        Reduce { state, action in
            enum CancelID { case request }

            switch action {
            case .view(.favoriteButtonTapped):
                return .run { [state] send in
                    await send(.favoriteResponse(Result {
                        try await timetableClient.toggleBookmark(id: state.timetableItem.id)
                    }))
                }
                .cancellable(id: CancelID.request)

            case .view(.calendarButtonTapped):
                return .run { send in
                    await send(.requestEventAccessResponse(Result {
                        try await eventKitClient.requestAccessIfNeeded()
                    }))
                }
                
            case let .favoriteResponse(.failure(error)):
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

            case let .view(.slideButtonTapped(url)):
                state.url = IdentifiableURL(url)
                return .none

            case let .view(.videoButtonTapped(url)):
                state.url = IdentifiableURL(url)
                return .none
                
            case let .view(.urlTapped(url)):
                state.url = IdentifiableURL(url)
                return .none

            case .favoriteResponse(.success):
                if !state.isFavorited {
                    state.toast = .init(text: String(localized: "TimetableDetailAddBookmark", bundle: .module))
                }
                state.isFavorited.toggle()
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
