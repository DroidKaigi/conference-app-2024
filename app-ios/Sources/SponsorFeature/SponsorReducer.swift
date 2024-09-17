import ComposableArchitecture
import Foundation
import KMPClient
import Model

@Reducer
public struct SponsorReducer : Sendable {
    @Dependency(\.sponsorsClient) var sponsorsData
    
    public init() { }
    
    @ObservableState
    public struct State: Equatable {
        var platinums = [Sponsor]()
        var golds = [Sponsor]()
        var supporters = [Sponsor]()
        var url: IdentifiableURL?

        public init() { }
    }

    public enum Action: Sendable, BindableAction, ViewAction {
        case binding(BindingAction<State>)
        case response(Result<[Sponsor], any Error>)
        case view(View)
        
        public enum View: Sendable {
            case onAppear
            case sponsorTapped(URL)
        }
    }

    public var body: some ReducerOf<Self> {
        BindingReducer()
        Reduce { state, action in
            enum CancelID { case connection }
            
            switch action {
            case .view(.onAppear):
                return .run { send in
                    do {
                        for try await sponsors in try sponsorsData.streamSponsors() {
                            await send(.response(.success(sponsors)))
                        }
                    } catch {
                        await send(.response(.failure(error)))
                    }
                }
                .cancellable(id: CancelID.connection)
                
            case let .view(.sponsorTapped(url)):
                state.url = IdentifiableURL(url)
                return .none
                
            case .response(.success(let sponsors)):
                var platinums = [Sponsor]()
                var golds = [Sponsor]()
                var supporters = [Sponsor]()
                
                sponsors.forEach {
                    switch $0.plan {
                    case .platinum:
                        platinums.append($0)
                    case .gold:
                        golds.append($0)
                    case .supporter:
                        supporters.append($0)
                    }
                }
                
                state.platinums = platinums
                state.golds = golds
                state.supporters = supporters
                return .none

            case .response(.failure(let error)):
                print(error)
                return .none
                
            case .binding:
                return .none
            }
        }
    }
}
