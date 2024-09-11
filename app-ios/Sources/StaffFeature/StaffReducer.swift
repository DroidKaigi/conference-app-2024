import ComposableArchitecture
import Foundation
import KMPClient
import Model
import shared

@Reducer
public struct StaffReducer : Sendable {
    @Dependency(\.staffClient) var staffsData

    public init() { }
    
    @ObservableState
    public struct State: Equatable {
        var list: [Model.Staff] = []
        public init() { }
    }

    public enum Action: Sendable, ViewAction {
        case view(View)
        case response(Result<[Model.Staff], any Error>)

        public enum View: Sendable {
            case onAppear
        }
    }

    public var body: some ReducerOf<Self> {
        Reduce { state, action in
            enum CancelID { case connection }
            
            switch action {
            case .view(.onAppear):
                return .run { send in
                    do {
                        for try await staffs in try staffsData.streamStaffs() {
                            await send(.response(.success(staffs)))
                        }
                    } catch {
                        await send(.response(.failure(error)))
                    }
                }
                .cancellable(id: CancelID.connection)
            case .response(.success(let staffs)):
                state.list = staffs
                return .none
            case .response(.failure(let error)):
                print(error)
                return .none
            }
        }
    }
}
