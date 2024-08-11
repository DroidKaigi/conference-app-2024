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
                    empty
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
                    store.filters.searchWord
                }, set: {
                    store.send(.view(.searchWordChanged($0)))
                }
            ),
            placement: .navigationBarDrawer(displayMode: .always)
        )
        .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
        .onAppear {
            store.send(.view(.onAppear))
        }
    }

    @ViewBuilder
    private var empty: some View {
        let searchWord = store.filters.searchWord

        Group {
            if !searchWord.isEmpty {
                VStack(spacing: 32) {
                    Image(.searchEmpty)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 165)

                    Text(String(localized: "「\(searchWord)」と一致する検索結果がありません", bundle: .module))
                        .textStyle(.titleMedium)
                        .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                }
            } else {
                Color.clear
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
    }

    private var filters: some View {
        ScrollView(.horizontal) {
            HStack(spacing: 6) {
                searchFilterChip(
                    selection: store.selectedDay,
                    defaultTitle: String(localized: "開催日", bundle: .module),
                    onSelect: {
                        store.send(.view(.selectedDayChanged($0)))
                    }
                )
                searchFilterChip(
                    selection: store.selectedCategory,
                    defaultTitle: String(localized: "カテゴリ", bundle: .module),
                    onSelect: {
                        store.send(.view(.selectedCategoryChanged($0)))
                    }
                )
                searchFilterChip(
                    selection: store.selectedSessionType,
                    defaultTitle: String(localized: "セッション種別", bundle: .module),
                    onSelect: {
                        store.send(.view(.selectedSessionTypeChanged($0)))
                    }
                )
                searchFilterChip(
                    selection: store.selectedLanguage,
                    defaultTitle: String(localized: "対合言語", bundle: .module),
                    onSelect: {
                        store.send(.view(.selectedLanguageChanged($0)))
                    }
                )
            }
            .padding(.horizontal, 16)
            .padding(.top, 8)
            .padding(.bottom, 12)
        }
    }

    private func searchFilterChip<T: Selectable>(
        selection: T?,
        defaultTitle: String,
        onSelect: @escaping (T) -> Void
    ) -> some View where T.AllCases: RandomAccessCollection {
        Menu {
            ForEach(T.allCases, id: \.id) { menuSelection in
                Button {
                    onSelect(menuSelection)
                } label: {
                    HStack {
                        if menuSelection == selection {
                            Image(.icCheck)
                        }
                        Text(menuSelection.caseTitle)
                    }
                }
            }

        } label: {
            SelectionChip(
                title: selection?.caseTitle ?? defaultTitle,
                isMultiSelect: true,
                isSelected: selection != nil
            ) {}
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
