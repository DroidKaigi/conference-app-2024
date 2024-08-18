import ComposableArchitecture
import Foundation
import KMPClient
import shared

struct SponsorData: Equatable, Identifiable {
    let id: String
    let logo: URL
    let link: URL
}

@Reducer
public struct SponsorReducer {
    @Dependency(\.sponsorsClient) var sponsorsData
    
    public init() { }
    
    @ObservableState
    public struct State: Equatable {
        var platinums = [SponsorData]()
        var golds = [SponsorData]()
        var supporters = [SponsorData]()

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
                    do {
                        for try await sponsors in try sponsorsData.streamSponsors() {
                            await send(.response(.success(sponsors)))
                        }
                    } catch {
                        await send(.response(.failure(error)))
                    }
                }
                .cancellable(id: CancelID.connection)
            case .response(.success(let sponsors)):
                var platinums = [SponsorData]()
                var golds = [SponsorData]()
                var supporters = [SponsorData]()
                
                sponsors.forEach {
                    switch $0.plan {
                    case .platinum:
                        platinums.append(.init(id: $0.name, logo: .init(string: $0.logo)!, link: .init(string: $0.link)!))
                    case .gold:
                        golds.append(.init(id: $0.name, logo: .init(string: $0.logo)!, link: .init(string: $0.link)!))
                    case .supporter:
                        supporters.append(.init(id: $0.name, logo: .init(string: $0.logo)!, link: .init(string: $0.link)!))
                    }
                }
                
                state.platinums = platinums
                state.golds = golds
                state.supporters = supporters
                return .none
            case .response(.failure(let error)):
                print(error)
                return .none
            }
        }
    }
}
