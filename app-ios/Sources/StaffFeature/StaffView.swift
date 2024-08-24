import ComposableArchitecture
import SwiftUI
import Theme
import CommonComponents
import Model

public struct StaffView: View {
    private let store: StoreOf<StaffReducer>
    @State var selectedStaffData: Staff?

    public init(store: StoreOf<StaffReducer>) {
        self.store = store
    }

    public var body: some View {
        ScrollView {
            LazyVStack {
                ForEach(store.list) { staff in
                    Button {
                        selectedStaffData = staff
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
        .sheet(item: $selectedStaffData, content: { data in
            SafariView(url: data.github)
                .ignoresSafeArea()
        })
    }
}

#Preview {
    StaffView(store: .init(initialState: .init(), reducer: { StaffReducer() }))
}
