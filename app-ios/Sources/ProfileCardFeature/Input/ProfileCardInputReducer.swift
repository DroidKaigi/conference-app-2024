import ComposableArchitecture
import KMPClient
@preconcurrency import shared

@Reducer
public struct ProfileCardInputReducer: Sendable {
    public init() { }

    @ObservableState
    public struct State: Equatable {
        public var nickname: String
        public var occupation: String
        public var link: String

        public init(
            nickname: String = "",
            occupation: String = "",
            link: String = ""
        ) {
            self.nickname = nickname
            self.occupation = occupation
            self.link = link
        }
    }

    public enum Action {
        case view(View)
        case `internal`(Internal)

        @CasePathable
        public enum View {
            case onAppear
            case onNicknameChanged(String)
            case onOccupationChanged(String)
            case onLinkChanged(String)
        }

        public enum Internal {
        }
    }

    public var body: some ReducerOf<Self> {
        Reduce { state, action in
            switch action {
            case let .view(viewAction):
                switch viewAction {
                case .onAppear:
                    return .none

                case let .onNicknameChanged(nickname):
                    state.nickname = nickname
                    return .none

                case let .onOccupationChanged(occupation):
                    state.occupation = occupation
                    return .none

                case let .onLinkChanged(link):
                    state.link = link
                    return .none
                }
            }
        }
    }
}
