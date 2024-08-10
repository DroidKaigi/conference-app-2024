import ComposableArchitecture
import KMPClient
@preconcurrency import shared

@Reducer
public struct SearchReducer {
    @Dependency(\.timetableClient) var timetableData

    public init() { }

    @ObservableState
    public struct State: Equatable {
        public var filters: Filters = .init()
        public var timetable: Timetable?

        var selectedDay: DroidKaigi2024Day? {
            filters.days.first
        }

        var selectedCategory: TimetableCategory? {
            filters.categories.first
        }

        var selectedSessionType: TimetableSessionType? {
            filters.sessionTypes.first
        }

        var selectedLanguage: Lang? {
            filters.languages.first
        }

        public var timetableItems: [TimetableItemWithFavorite] {
            timetable?
                .filtered(
                    filters: filters
                )
                .contents ?? []
        }

        public init(timetable: Timetable? = nil) {
            self.timetable = timetable
        }
    }

    public enum Action {
        case view(View)
        case `internal`(Internal)
        case destination(Destination)

        @CasePathable
        public enum View {
            case onAppear
            case timetableItemTapped(TimetableItemWithFavorite)
            case toggleFavoriteTapped(TimetableItemId)
            case selectedDayChanged(DroidKaigi2024Day?)
            case selectedCategoryChanged(TimetableCategory?)
            case selectedSessionTypeChanged(TimetableSessionType?)
            case selectedLanguageChanged(Lang?)
            case searchWordChanged(String)
        }

        public enum Internal {
            // TODO: Swift Concurrency
            case timetableResponse(Result<Timetable, any Error>)
            case toggleBookmarkResponse(Result<Void, any Error>)
        }

        // Handled by root navigator
        public enum Destination {
            case timetableDetail(TimetableItemWithFavorite)
        }
    }

    public var body: some ReducerOf<Self> {
        Reduce { state, action in
            switch action {
            case let .view(viewAction):
                switch viewAction {
                case .onAppear:
                    return .run { send in
                        do {
                            for try await timetable in try timetableData.streamTimetable() {
                                await send(.internal(.timetableResponse(.success(timetable))))
                            }
                        } catch {
                            await send(.internal(.timetableResponse(.failure(error))))
                        }
                    }

                case let .timetableItemTapped(timetableItemWithFavorite):
                    return .send( .destination(.timetableDetail(timetableItemWithFavorite)))

                case let .toggleFavoriteTapped(id):
                    return .run { send in
                        await send(.internal(.toggleBookmarkResponse(Result {
                            try await timetableData.toggleBookmark(id: id)
                        })))
                    }

                case let .selectedDayChanged(day):
                    state.filters = state.filters.copyWith(
                        days: day.map { [$0] } ?? []
                    )
                    return .none

                case let .selectedCategoryChanged(category):
                    state.filters = state.filters.copyWith(
                        categories: category.map { [$0] } ?? []
                    )
                    return .none

                case let .selectedSessionTypeChanged(sessionType):
                    state.filters = state.filters.copyWith(
                        sessionTypes: sessionType.map { [$0] } ?? []
                    )
                    return .none

                case let .selectedLanguageChanged(language):
                    state.filters = state.filters.copyWith(
                        languages: language.map { [$0] } ?? []
                    )
                    return .none

                case let .searchWordChanged(searchWord):
                    state.filters = state.filters.copyWith(
                        searchWord: searchWord
                    )
                    return .none
                }

            case let .internal(internalAction):
                switch internalAction {
                case let .timetableResponse(.success(timetable)):
                    state.timetable = timetable
                    return .none

                case .timetableResponse(.failure):
                    return .none

                case .toggleBookmarkResponse:
                    return .none
                }

            case .destination:
                return .none
            }
        }
    }
}

private extension Filters {
    convenience init(
        days: [DroidKaigi2024Day] = [],
        categories: [TimetableCategory] = [],
        sessionTypes: [TimetableSessionType] = [],
        languages: [Lang] = [],
        searchWord: String = ""
    ) {
        self.init(
            days: days,
            categories: categories,
            sessionTypes: sessionTypes,
            languages: languages,
            filterFavorite: false,
            searchWord: searchWord
        )
    }

    func copyWith(
        days: [DroidKaigi2024Day]? = nil,
        categories: [TimetableCategory]? = nil,
        sessionTypes: [TimetableSessionType]? = nil,
        languages: [Lang]? = nil,
        searchWord: String? = nil
    ) -> Filters {
        Filters(
            days: days ?? self.days,
            categories: categories ?? self.categories,
            sessionTypes: sessionTypes ?? self.sessionTypes,
            languages: languages ?? self.languages,
            searchWord: searchWord ?? self.searchWord
        )
    }
}
