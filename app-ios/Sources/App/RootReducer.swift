import AboutFeature
import ComposableArchitecture
import FavoriteFeature
import TimetableFeature

@Reducer
public struct RootReducer {
    public init() {}

    @ObservableState
    public struct State: Equatable {
        public var appDelegate: AppDelegateReducer.State
        public var timetable: TimetableReducer.State
        public var favorite: FavoriteReducer.State
        public var about: AboutReducer.State

        public init(
            appDelegate: AppDelegateReducer.State = .init(),
            timetable: TimetableReducer.State = .init(),
            favorite: FavoriteReducer.State = .init(),
            about: AboutReducer.State = .init()
        ) {
            self.appDelegate = appDelegate
            self.timetable = timetable
            self.favorite = favorite
            self.about = about
        }
    }

    public enum Action {
        case appDelegate(AppDelegateReducer.Action)
        case timetable(TimetableReducer.Action)
        case favorite(FavoriteReducer.Action)
        case about(AboutReducer.Action)
    }

    public var body: some ReducerOf<Self> {
        Scope(state: \.appDelegate, action: \.appDelegate) {
            AppDelegateReducer()
        }
        Scope(state: \.timetable, action: \.timetable) {
            TimetableReducer()
        }
        Scope(state: \.favorite, action: \.favorite) {
            FavoriteReducer()
        }
        Scope(state: \.about, action: \.about) {
            AboutReducer()
        }
    }
}
