import SwiftUI
import CommonComponents
import ComposableArchitecture
import Theme

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
        }
        .background(AssetColors.Surface.surface.swiftUIColor)
        .navigationBarTitleDisplayMode(.large)
        .navigationTitle(String(localized: "NavigationTitle", bundle: .module))
    }
}

#Preview {
    EventMapView(store: .init(initialState: .init(), reducer: {
        EventMapReducer()
    }))
}
