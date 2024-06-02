import ComposableArchitecture
import SwiftUI
import TimetableFeature

private enum Tab {
    case timetable
    case map
    case favorite
    case about
    case idCard
}

public struct RootView: View {
    private let store: StoreOf<RootCore>
    @State private var selection: Tab = .timetable

    public init(store: StoreOf<RootCore>) {
        self.store = store
    }

    public var body: some View {
        TabView(selection: $selection) {
            TimetableListView(
                store: store.scope(
                    state: \.timetable,
                    action: \.timetable
                )
            )
            .tag(Tab.timetable)
            .tabItem {
                Label(
                    title: { Text("Timetable") },
                    icon: { Image(systemName: "42.circle") }
                )
            }

            Text("Map Feature")
                .tag(Tab.map)
                .tabItem {
                    Label(
                        title: { Text("Event Map") },
                        icon: { Image(systemName: "42.circle") }
                    )
                }

            Text("Favorite Feature")
                .tag(Tab.favorite)
                .tabItem {
                    Label(
                        title: { Text("Favorite") },
                        icon: { Image(systemName: "42.circle") }
                    )
                }

            Text("About Feature")
                .tag(Tab.about)
                .tabItem {
                    Label(
                        title: { Text("About") },
                        icon: { Image(systemName: "42.circle") }
                    )
                }

            Text("ID Card Feature")
                .tag(Tab.idCard)
                .tabItem {
                    Label(
                        title: { Text("ID Card") },
                        icon: { Image(systemName: "42.circle") }
                    )
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
