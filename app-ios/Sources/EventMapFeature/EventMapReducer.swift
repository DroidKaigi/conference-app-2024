import ComposableArchitecture
import KMPClient
@preconcurrency import shared

@Reducer
public struct EventMapReducer: Sendable {
    @Dependency(\.eventMapClient) private var eventMapClient

    public init() { }
    
    @ObservableState
    public struct State: Equatable {
        public var selectedFloorMap: FloorMap = .first
        public var events: [EventMapEvent] = []
        
        public init() { }
    }
    
    public enum Action {
        case view(View)
        case `internal`(Internal)

        @CasePathable
        public enum View {
            case onAppear
            case selectFloorMap(FloorMap)
        }
        
        public enum Internal {
            case response(Result<[EventMapEvent], any Error>)
        }
    }
    
    public var body: some ReducerOf<Self> {
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
                
            case let .internal(.response(.success(events))):
                state.events = events
                return .none
                
            case let .internal(.response(.failure(error))):
                print(error.localizedDescription)
                return .none
            }
        }
    }
}
