import ComposableArchitecture
import KMPClient

@Reducer
public struct AppDelegateReducer {
    @Dependency(\.firebaseAppClient) var firebaseAppClient
    public struct State: Equatable {
      public init() {}
    }

    public enum Action: Equatable {
        case didFinishLaunching
    }

    public var body: some ReducerOf<Self> {
        Reduce { state, action in
            switch action {
            case .didFinishLaunching:
                firebaseAppClient.prepareFirebase()

                return .none
            }
        }
    }
}
