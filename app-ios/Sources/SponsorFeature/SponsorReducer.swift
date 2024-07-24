import ComposableArchitecture
import KMPClient
import shared

@Reducer
public struct SponsorReducer {
    @Dependency(\.sponsorsClient) var sponsorsData
    
    public init() { }
    
    @ObservableState
    public struct State: Equatable {
        public init() { }
    }

    public enum Action {
        case onAppear
        case response(Result<[Sponsor], any Error>)
    }

    public var body: some ReducerOf<Self> {
        Reduce { state, action in
            enum CancelID { case connection }
            
            switch action {
            case .onAppear:
                return .run { send in
                    for try await sponsors in try sponsorsData.streamSponsors() {
                        await send(.response(.success(sponsors)))
                    }
                }
                .cancellable(id: CancelID.connection)
            case .response(.success(let sponsors)):
                print(sponsors)
                return .none
            case .response(.failure(let error)):
                print(error)
                return .none
            }
        }
    }
}
