import CommonComponents
import ComposableArchitecture
import KMPClient
import Model
import shared
import SwiftUI
import Theme

public struct SearchView: View {
    @Bindable private var store: StoreOf<SearchReducer>

    public init(store: StoreOf<SearchReducer>) {
        self.store = store
    }

    public var body: some View {
        let timetableItems = store.timetableItems
        VStack {
            filters
            Group {
                if timetableItems.isEmpty {
                } else {
                    ScrollView {
                        LazyVStack {
                            ForEach(store.timetableItems, id: \.timetableItem.id) { timetableItemWithFavorite in
                                let timetableItem = timetableItemWithFavorite.timetableItem
                                TimetableCard(
                                    timetableItem: timetableItem,
                                    isFavorite: timetableItemWithFavorite.isFavorited
                                ) { _ in
                                    store.send(.view(.timetableItemTapped(timetableItemWithFavorite)))
                                } onTapFavorite: { _ in
                                    store.send(.view(.toggleFavoriteTapped(timetableItem.id)))
                                }
                            }
                        }
                        .padding(.horizontal, 16)
                    }
                }
            }
        }
        .background(AssetColors.Surface.surface.swiftUIColor)
        .searchable(
            text: .init(
                get: {
                    store.filters?.searchWord ?? ""
                }, set: {
                    store.send(.view(.searchWordChanged($0)))
                }
            ),
            placement: .navigationBarDrawer(displayMode: .always)
        )
        .onAppear {
            store.send(.view(.onAppear))
        }
    }

    private var filters: some View {
        ScrollView(.horizontal) {
            HStack(spacing: 6) {
                searchFilterChip(
                    selection: store.selectedDay,
                    defaultTitle: "開催日"
                )
                searchFilterChip(
                    selection: store.selectedCategory,
                    defaultTitle: "カテゴリ"
                )
                searchFilterChip(
                    selection: store.selectedSessionType,
                    defaultTitle: "セッション種別"
                )
                searchFilterChip(
                    selection: store.selectedLanguage,
                    defaultTitle: "対合言語"
                )
            }
            .padding(.horizontal, 16)
            .padding(.top, 8)
            .padding(.bottom, 12)
        }
    }

    private func searchFilterChip<T: Selectable>(selection: T?, defaultTitle: String) -> some View {
        SelectionChip(
            title: selection?.caseTitle ?? defaultTitle,
            isMultiSelect: true,
            isSelected: selection != nil
        ) {
            // TODO: show menu
        }
    }
}

extension DroidKaigi2024Day: Selectable {
    public var id: Self {
        self
    }

    public var caseTitle: String {
        switch self {
        case .workday:
            String(localized: "9/11", bundle: .module)

        case .conferenceDay1:
            String(localized: "9/12", bundle: .module)

        case .conferenceDay2:
            String(localized: "9/13", bundle: .module)
        }
    }
}

extension TimetableCategory: Selectable {
    public var caseTitle: String {
        title.currentLangTitle
    }
    
    static public var allCases: [TimetableCategory] {
        // TODO: use correct
        []
    }
}

extension TimetableSessionType: Selectable {
    public var id: Self {
        self
    }

    public var caseTitle: String {
        label.currentLangTitle
    }
}

extension Lang: Selectable {
    public var id: Self {
        self
    }

    public var caseTitle: String {
        name
    }
}

#Preview {
    SearchView(
        store: .init(
            initialState: .init(),
            reducer: {}
        )
    )
}
