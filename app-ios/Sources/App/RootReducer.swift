import AboutFeature
import ComposableArchitecture
import ContributorFeature
import FavoriteFeature
import SearchFeature
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
            case search(SearchReducer)
        }

        @Reducer(state: .equatable)
        public enum Favorite {
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
            public var favorite = StackState<Path.Favorite.State>()
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
        case view(View)
        case paths(Paths)

        @CasePathable
        public enum Paths {
            case timetable(StackActionOf<RootReducer.Path.Timetable>)
            case favorite(StackActionOf<RootReducer.Path.Favorite>)
            case about(StackActionOf<RootReducer.Path.About>)
        }
        
        public enum View {
            case sameTabTapped(Tab)
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

            case .timetable(.view(.searchTapped)):
                state.paths.timetable.append(.search(.init()))
                return .none

            case let .favorite(.destination(destination)):
                switch destination {
                case let .timetableDetail(timetableItemWithFavorite):
                    state.paths.favorite.append(
                        .timetableDetail(
                            .init(
                                timetableItem: timetableItemWithFavorite.timetableItem,
                                isBookmarked: timetableItemWithFavorite.isFavorited
                            )
                        )
                    )
                    return .none
                }
                
            case let .view(.sameTabTapped(tab)):
                switch tab {
                case .timetable: state.paths.timetable.removeAll()
                case .favorite: state.paths.favorite.removeAll()
                case .about: state.paths.about.removeAll()
                case .map: break
                case .idCard: break
                }
                
                return .none
                
            default:
                return .none
            }
        }
        .forEach(\.paths.about, action: \.paths.about)
        .forEach(\.paths.favorite, action: \.paths.favorite)
        .forEach(\.paths.timetable, action: \.paths.timetable)
    }
}
