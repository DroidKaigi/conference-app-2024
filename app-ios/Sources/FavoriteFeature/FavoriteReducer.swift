import ComposableArchitecture
import KMPClient
import shared

@Reducer
public struct FavoriteReducer {
    @Dependency(\.timetableClient) var timetableData

    public init() { }

    @ObservableState
    public struct State: Equatable {
        public var selectedDay: DroidKaigi2024Day?
        public var timetable: Timetable?

        public var timetableItems: [TimetableItemWithFavorite] {
            timetable?
                .filtered(
                    filters: .init(
                        days: selectedDay.map { [$0] }
                            ?? DroidKaigi2024Day.allCases
                    )
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
                        do {
                            try await timetableData.toggleBookmark(id: id)
                            await send(.internal(.toggleBookmarkResponse(.success(()))))
                        } catch {
                            await send(.internal(.toggleBookmarkResponse(.failure(error))))
                        }
                    }

                case let .selectedDayChanged(day):
                    state.selectedDay = day
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
        days: [DroidKaigi2024Day] = []
    ) {
        self.init(
            days: days,
            categories: [],
            sessionTypes: [],
            languages: [],
            filterFavorite: true,
            searchWord: ""
        )
    }
}
