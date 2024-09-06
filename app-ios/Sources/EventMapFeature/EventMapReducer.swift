import ComposableArchitecture
import KMPClient
import Model
import Foundation

@Reducer
public struct EventMapReducer: Sendable {
    @Dependency(\.eventMapClient) private var eventMapClient

    public init() { }
    
    @ObservableState
    public struct State: Equatable {
        public var selectedFloorMap: FloorMap = .first
        public var events: [EventMapEvent] = []
        public var url: IdentifiableURL?
        
        public init() { }
    }
    
    public enum Action: Sendable, BindableAction {
        case binding(BindingAction<State>)
        case view(View)
        case `internal`(Internal)

        @CasePathable
        public enum View: Sendable {
            case onAppear
            case selectFloorMap(FloorMap)
            case moreDetailButtonTapped(URL)
        }
        
        public enum Internal: Sendable {
            case response(Result<[EventMapEvent], any Error>)
        }
    }
    
    public var body: some ReducerOf<Self> {
        BindingReducer()
        Reduce { state, action in
            switch action {
            case .view(.onAppear):
                return .run { send in
                    do {
                        for try await events in try eventMapClient.streamEvents() {
                            await send(.internal(.response(.success(events))))
                        }
                    } catch {
                        await send(.internal(.response(.failure(error))))
                    }
                }

            case let .view(.selectFloorMap(floor)):
                state.selectedFloorMap = floor
                return .none
                
            case let .view(.moreDetailButtonTapped(url)):
                state.url = IdentifiableURL(url)
                return .none
                
            case let .internal(.response(.success(events))):
                state.events = events
                return .none
                
            case let .internal(.response(.failure(error))):
                print(error.localizedDescription)
                return .none
                
            case .binding:
                return .none
            }
        }
    }
}
