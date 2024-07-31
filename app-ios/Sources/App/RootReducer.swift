import AboutFeature
import ComposableArchitecture
import ContributorFeature
import FavoriteFeature
import StaffFeature
import SponsorFeature
import TimetableFeature
import TimetableDetailFeature
import shared

@Reducer
public struct RootReducer {
    public init() {}

    public enum Path {
        @Reducer(state: .equatable)
        public enum Timetable {
            case timetableDetail(TimetableDetailReducer)
        }

        @Reducer(state: .equatable)
        public enum About {
            case staff(StaffReducer)
            case contributor(ContributorReducer)
            case sponsor(SponsorReducer)
            case acknowledgements
        }
    }

    @ObservableState
    public struct State: Equatable {
        public var appDelegate: AppDelegateReducer.State
        public var timetable: TimetableReducer.State
        public var favorite: FavoriteReducer.State
        public var about: AboutReducer.State
        public var paths: Paths = .init()

        public struct Paths: Equatable {
            public var timetable = StackState<Path.Timetable.State>()
            public var about = StackState<Path.About.State>()
        }

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
        case paths(Paths)

        @CasePathable
        public enum Paths {
            case timetable(StackActionOf<RootReducer.Path.Timetable>)
            case about(StackActionOf<RootReducer.Path.About>)
        }
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
        navigationReducer
    }

    private var navigationReducer: some ReducerOf<Self> {
        Reduce { state, action in
            switch action {
            case .about(.view(.staffsTapped)):
                state.paths.about.append(.staff(.init()))
                return .none

            case .about(.view(.contributorsTapped)):
                state.paths.about.append(.contributor(.init()))
                return .none

            case .about(.view(.sponsorsTapped)):
                state.paths.about.append(.sponsor(.init()))
                return .none

            case .about(.view(.acknowledgementsTapped)):
                state.paths.about.append(.acknowledgements)
                return .none
                
            case .timetable(.view(.timetableItemTapped)):
                state.paths.timetable.append(.timetableDetail(
                    TimetableDetailReducer.State(
                        timetableItem: shared.TimetableItem.Session.companion.fake()
                    )
                ))
                return .none

            default:
                return .none
            }
        }
        .forEach(\.paths.about, action: \.paths.about)
        .forEach(\.paths.timetable, action: \.paths.timetable)
    }
}
