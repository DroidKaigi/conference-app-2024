import AboutFeature
import ComposableArchitecture
import ContributorFeature
import FavoriteFeature
import LicenseList
import SearchFeature
import SponsorFeature
import StaffFeature
import SwiftUI
import TimetableDetailFeature
import TimetableFeature
import EventMapFeature
import Theme

public enum DroidKaigiAppTab: Hashable {
    case timetable
    case map
    case favorite
    case about
    case idCard
}

public struct RootView: View {
    @Bindable private var store: StoreOf<RootReducer>
    @State private var selection: DroidKaigiAppTab = .timetable

    public init(store: StoreOf<RootReducer>) {
        self.store = store
        UINavigationBar.appearance().barTintColor = AssetColors.Surface.surface.color
    }

    public var body: some View {
        Group {
            switch selection {
            case .timetable:
                timetableTab
            case .map:
                eventMapTab
            case .favorite:
                favoriteTab
            case .about:
                aboutTab
            case .idCard:
                idCardTab
            }
        }
        .navigationBarTitleStyle(
            color: AssetColors.Surface.onSurface.swiftUIColor,
            titleTextStyle: .titleMedium,
            largeTitleTextStyle: .headlineSmall
        )
    }

    @MainActor
    @ViewBuilder
    private var tabItems: some View {
        let items: [(tab: DroidKaigiAppTab, icon: ImageResource)] = [
            (tab: .timetable, icon: .icTimetable),
            (tab: .map, icon: .icMap),
            (tab: .favorite, icon: .icFav),
            (tab: .about, icon: .icInfo),
            (tab: .idCard, icon: .icProfileCard),
        ]
        HStack(spacing: 36) {
            ForEach(items, id: \.tab) { item in
                let isSelected = selection == item.tab
                Button {
                    selection = item.tab
                } label: {
                    Image(item.icon).renderingMode(.template).tint(isSelected ? nil : .white)
                }
            }
        }
        .padding(.vertical)
        .padding(.horizontal, 24)
        .background(.ultraThinMaterial, in: Capsule())
        .overlay(Capsule().stroke(.gray, lineWidth: 1))
        .environment(\.colorScheme, .dark)
    }

    @MainActor
    private var timetableTab: some View {
        NavigationStack(
            path: $store.scope(
                state: \.paths.timetable,
                action: \.paths.timetable
            )
        ) {
            ZStack(alignment: .bottom) {
                TimetableView(
                    store: store.scope(
                        state: \.timetable,
                        action: \.timetable
                    )
                )
                tabItems
            }
        } destination: { store in
            switch store.case {
            case let .timetableDetail(store):
                TimetableDetailView(store: store)

            case let .search(store):
                SearchView(store: store)
            }
        }
    }

    @MainActor
    private var aboutTab: some View {
        NavigationStack(
            path: $store.scope(
                state: \.paths.about,
                action: \.paths.about
            )
        ) {
            ZStack(alignment: .bottom) {
                AboutView(
                    store: store.scope(
                        state: \.about,
                        action: \.about
                    )
                )
                tabItems
            }
        } destination: { store in
            switch store.case {
            case let .staff(store):
                StaffView(store: store)

            case let .contributor(store):
                ContributorView(store: store)

            case let .sponsor(store):
                SponsorView(store: store)

            case .acknowledgements:
                LicenseListView()
            }
        }
    }

    @MainActor
    private var favoriteTab: some View {
        NavigationStack(
            path: $store.scope(
                state: \.paths.favorite,
                action: \.paths.favorite
            )
        ) {
            ZStack(alignment: .bottom) {
                FavoriteView(
                    store: store.scope(
                        state: \.favorite,
                        action: \.favorite
                    )
                )
                tabItems
            }
        } destination: { store in
            switch store.case {
            case let .timetableDetail(store):
                TimetableDetailView(store: store)
            }
        }
    }
    
    @MainActor
    private var eventMapTab: some View {
        NavigationStack {
            ZStack(alignment: .bottom) {
                EventMapView(store: Store(initialState: .init(), reducer: {
                    EventMapReducer()
                }))
                tabItems
            }
        }
    }

    @MainActor
    private var idCardTab: some View {
        NavigationStack {
            ZStack(alignment: .bottom) {
                ScrollView {
                    Text("ID Card Feature")
                }
                tabItems
            }
        }
    }
}

#Preview {
    RootView(
        store: .init(
            initialState: .init(
                timetable: .init()
            ),
            reducer: {}
        )
    )
}
