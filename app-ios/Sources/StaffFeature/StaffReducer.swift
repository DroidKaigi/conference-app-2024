import ComposableArchitecture
import Foundation
import KMPClient
import Dependencies
import shared

struct StaffData: Equatable, Identifiable {
    let id: Int64
    let name: String
    let icon: URL
    let github: URL
}

@Reducer
public struct StaffReducer {
    @Dependency(\.staffClient) var staffsData

    public init() { }
    
    @ObservableState
    public struct State: Equatable {
        var list: [StaffData] = []
        public init() { }
    }

    public enum Action {
        case onAppear
        case response(Result<[Staff], any Error>)
    }

    public var body: some ReducerOf<Self> {
        Reduce { state, action in
            switch action {
            case .onAppear:
                return .run { send in
                    for try await staffs in try staffsData.streamStaffs() {
                        await send(.response(.success(staffs)))
                    }
                }
            case .response(.success(let staffs)):
                state.list = staffs.map {
                    StaffData(
                        id: $0.id,
                        name: $0.username,
                        icon: URL(string: $0.iconUrl)!,
                        github: URL(string: $0.profileUrl)!
                    )
                }
                return .none
            case .response(.failure(let error)):
                return .none
            }
        }
    }
}
