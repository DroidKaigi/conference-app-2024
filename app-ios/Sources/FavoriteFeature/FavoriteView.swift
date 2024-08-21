import CommonComponents
import ComposableArchitecture
import Model
import KMPClient
import shared
import SwiftUI
import Theme

public struct FavoriteView: View {
    @Bindable private var store: StoreOf<FavoriteReducer>

    public init(store: StoreOf<FavoriteReducer>) {
        self.store = store
    }

    public var body: some View {
        let timetableItems = store.timetableItems
        VStack {
            daySelection
            Group {
                if timetableItems.isEmpty {
                    empty
                        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
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
                        // bottom floating tabbar padding
                        Color.clear.padding(.bottom, 60)
                    }
                }
            }
        }
        .background(AssetColors.Surface.surface.swiftUIColor)
        .onAppear {
            store.send(.view(.onAppear))
        }
        .navigationBarTitleDisplayMode(.large)
        .navigationTitle(String(localized: "Favorite", bundle: .module))
    }

    private var empty: some View {
        VStack(spacing: 24) {
            Image(.icFavFill)
                .renderingMode(.template)
                .resizable()
                .frame(width: 36, height: 36)
                .foregroundStyle(AssetColors.Primary.primaryFixed.swiftUIColor)
                .padding(24)
                .background(AssetColors.Primary.onPrimary.swiftUIColor)
                .clipShape(RoundedRectangle(cornerRadius: 24))

            VStack {
                Text("NoSessions.Title", bundle: .module)
                    .textStyle(.titleLarge)
                Text("NoSessions.Description", bundle: .module)
                    .textStyle(.bodyMedium)

            }
            .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
            .multilineTextAlignment(.center)
        }
    }

    private var daySelection: some View {
        SelectionChips<DroidKaigi2024Day>(
            selected: $store.selectedDay.sending(\.view.selectedDayChanged),
            notSelectedTitle: String(localized: "All", bundle: .module),
            options: DroidKaigi2024Day.options
        )
    }
}

#if hasFeature(RetroactiveAttribute)
extension DroidKaigi2024Day: @retroactive Selectable {}
#else
extension DroidKaigi2024Day: Selectable {}
#endif

extension DroidKaigi2024Day {
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

    public static var options: [DroidKaigi2024Day] {
        [.conferenceDay1, .conferenceDay2]
    }
}

#Preview {
    FavoriteView(
        store: .init(
            initialState: .init(),
            reducer: {}
        )
    )
}
