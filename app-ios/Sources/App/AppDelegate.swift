import ComposableArchitecture
import KMPClient

@Reducer
public struct AppDelegateReducer {
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
                prepareFirebase()

                return .none
            }
        }
    }
}
