import ComposableArchitecture
import KMPClient
import Model
import Foundation
@preconcurrency import class shared.Contributor

@Reducer
public struct ContributorReducer: Sendable {
    @Dependency(\.contributorClient) var contributorClient
    public init() { }
    
    @ObservableState
    public struct State: Equatable {
        var contributors: [Contributor]?
        var url: IdentifiableURL?

        public init(contributors: [Contributor]? = nil) {
            self.contributors = contributors
        }
    }

    public enum Action: Sendable, BindableAction {
        case binding(BindingAction<State>)
        case onAppear
        case response(Result<[Contributor], any Error>)
        case view(View)
        
        public enum View: Sendable {
            case contributorButtonTapped(URL)
        }
    }
    
    private enum CancelID { case request }

    public var body: some ReducerOf<Self> {
        BindingReducer()
        Reduce { state, action in
            switch action {
            case .onAppear:
                return .run { send in
                    try await contributorClient.refresh()
                    for try await contributors in try contributorClient.streamContributors() {
                        await send(.response(.success(contributors)))
                    }
                }
                .cancellable(id: CancelID.request)

            case let .response(.success(contributors)):
                state.contributors = contributors
                return .none

            case let .response(.failure(error)):
                print(error.localizedDescription)
                return .none
                
            case let .view(.contributorButtonTapped(url)):
                state.url = IdentifiableURL(url)
                return .none
                
            case .binding:
                return .none
            }
        }
    }
}
