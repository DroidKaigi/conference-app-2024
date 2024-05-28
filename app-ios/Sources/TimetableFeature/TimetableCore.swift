import ComposableArchitecture

@Reducer
public struct TimetableCore {
    public init() {}

    @ObservableState
    public struct State: Equatable {
        public var timetableItems: [TimetableItem] = [] //Should be simple objects

        public init() {}
    }

    public enum Action {
        case onAppear
        //case showList
        //case selectListItem //Should navigate?
    }

    public var body: some Reducer<State, Action> {
        Reduce { state, action in
            switch action {
            case .onAppear:
                //TODO: Load actual data here
                return .none
            }
        }
    }
    
    struct ListView: View {
        
    }
    
    //TODO: Reformat this with proper data
    public struct TimetableItem: Equatable {
        let title: String
        let description: String
        let startTime: String
        let endTime: String
        
    }
}
