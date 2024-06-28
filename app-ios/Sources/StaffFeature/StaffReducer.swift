import ComposableArchitecture
import KMPClient
import Dependencies
import shared

@Reducer
public struct StaffReducer {
    @Dependency(\.staffClient) var staffsData

    public init() { }
    
    @ObservableState
    public struct State: Equatable {
        var text: String
    }

    public enum Action {
        case onAppear
        case response(Result<[Staff], any Error>)
    }

    public var body: some ReducerOf<Self> {
        Reduce { state, action in
            switch action {
            case .onAppear:
                state.text = "Staff Feature"
                return .run { send in
                    for try await staffs in try staffsData.streamStaffs() {
                        await send(.response(.success(staffs)))
                    }
                }
            case .response(.success(let staffs)):
                print(staffs)
                return .none
            case .response(.failure(let error)):
                return .none
            }
        }
    }
}
