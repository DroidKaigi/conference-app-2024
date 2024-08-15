import ComposableArchitecture
import SwiftUI
import Theme
import CommonComponents

public struct StaffView: View {
    private let store: StoreOf<StaffReducer>
    @State var selectedStaffData: StaffData?

    public init(store: StoreOf<StaffReducer>) {
        self.store = store
    }

    public var body: some View {
        ScrollView {
            LazyVStack {
                ForEach(store.list, id: \.id) { staff in
                    Button {
                        selectedStaffData = staff
                    } label: {
                        StaffLabel(name: staff.name, icon: staff.icon)
                    }
                    .padding(.vertical, 12)
                }
            }
            .padding(16)
            // bottom floating tabbar padding
            Color.clear.padding(.bottom, 60)
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
