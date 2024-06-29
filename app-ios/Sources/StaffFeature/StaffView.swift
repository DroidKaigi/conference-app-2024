import ComposableArchitecture
import SwiftUI
import Theme

public struct StaffView: View {
    private let store: StoreOf<StaffReducer>

    public init(store: StoreOf<StaffReducer>) {
        self.store = store
    }

    public var body: some View {
        ScrollView {
            LazyVStack {
                ForEach(store.list, id: \.id) { staff in
                    Button {
                        
                    } label: {
                        StaffLabel(name: staff.name, icon: staff.icon)
                    }
                    .padding(.vertical, 12)
                }
            }
            .padding(16)
        }
        .background(AssetColors.Surface.surface.swiftUIColor)
        .onAppear {
            store.send(.onAppear)
        }
        .navigationBarTitleDisplayMode(.large)
        .navigationTitle(String(localized: "Staff", bundle: .module))
    }
}

#Preview {
    StaffView(store: .init(initialState: .init(), reducer: { StaffReducer() }))
}
