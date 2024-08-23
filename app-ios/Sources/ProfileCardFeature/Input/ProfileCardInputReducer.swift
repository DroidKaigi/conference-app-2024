import ComposableArchitecture
import _PhotosUI_SwiftUI
import KMPClient
@preconcurrency import shared

@Reducer
public struct ProfileCardInputReducer: Sendable {
    @Dependency(\.profileCardClient) private var profileCardClient

    public init() { }

    @ObservableState
    public struct State: Equatable {
        public var nickname: String
        public var occupation: String
        public var link: String
        public var photo: PhotosPickerItem?
        public var cardType: ProfileCardType?

        public init(
            nickname: String = "",
            occupation: String = "",
            link: String = "",
            photo: PhotosPickerItem? = nil,
            cardType: ProfileCardType? = nil
        ) {
            self.nickname = nickname
            self.occupation = occupation
            self.link = link
            self.photo = photo
            self.cardType = cardType
        }
    }

    public enum Action {
        case view(View)
        case `internal`(Internal)

        @CasePathable
        public enum View {
            case onAppear
            case nicknameChanged(String)
            case occupationChanged(String)
            case linkChanged(String)
            case photoChanged(PhotosPickerItem?)
            case cardTypeChanged(ProfileCardType?)
            case createCardTapped
        }

        public enum Internal {
            case profileCardSaved
        }
    }

    public var body: some ReducerOf<Self> {
        Reduce { state, action in
            switch action {
            case let .view(viewAction):
                switch viewAction {
                case .onAppear:
                    return .none

                case let .nicknameChanged(nickname):
                    state.nickname = nickname
                    return .none

                case let .occupationChanged(occupation):
                    state.occupation = occupation
                    return .none

                case let .linkChanged(link):
                    state.link = link
                    return .none

                case let .photoChanged(photo):
                    state.photo = photo
                    return .none

                case let .cardTypeChanged(cardType):
                    state.cardType = cardType
                    return .none

                case .createCardTapped:
                    let nickname = state.nickname
                    let link = state.link
                    let occupation = state.occupation
                    let photo = state.photo
                    let cardType = state.cardType ?? .none

                    return .run { send in
                        let base64String = try await photo?.loadTransferable(type: Data.self)?.base64EncodedString() ?? ""

                        try await profileCardClient.save(
                            .init(
                                nickname: nickname,
                                link: link,
                                occupation: occupation,
                                image: base64String,
                                cardType: cardType
                            )
                        )

                        await send(.internal(.profileCardSaved))
                    }
                }
            case let .internal(internalAction):
                return .none
            }
        }
    }
}
