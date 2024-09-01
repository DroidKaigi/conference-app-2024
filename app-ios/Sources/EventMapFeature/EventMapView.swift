import SwiftUI
import CommonComponents
import ComposableArchitecture
import Theme
import shared

public struct EventMapView: View {
    @Bindable private var store: StoreOf<EventMapReducer>

    public init(store: StoreOf<EventMapReducer>) {
        self.store = store
    }

    public var body: some View {
        ScrollView {
            Text("Description", bundle: .module)
                .textStyle(.bodyMedium)
                .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
                .padding(.horizontal, 16)
            
            SelectionChips<FloorMap>(
                selected: Binding(
                    get: { store.selectedFloorMap },
                    set: { store.send(.view(.selectFloorMap($0 ?? .first))) }
                )
            )
            
            Image(store.selectedFloorMap.image)
                .resizable()
                .frame(maxWidth: .infinity)
                .padding(.horizontal, 16)
                .scaledToFit()
                .padding(.bottom, 24)
            
            VStack(spacing: 24) {
                ForEach(store.events, id: \.self) { event in
                    EventItem(event: event) { url in
                        store.send(.view(.moreDetailButtonTapped(url)))
                    }
                }
            }
            // bottom floating tabbar padding
            Color.clear.padding(.bottom, 60)
        }
        .background(AssetColors.Surface.surface.swiftUIColor)
        .navigationBarTitleDisplayMode(.large)
        .navigationTitle(String(localized: "NavigationTitle", bundle: .module))
        .onAppear { store.send(.view(.onAppear)) }
        .sheet(item: $store.url, content: { url in
            SafariView(url: url.id)
                .ignoresSafeArea()
        })
    }
}

#Preview {
    EventMapView(store: .init(initialState: .init(), reducer: {
        EventMapReducer()
    }))
}
