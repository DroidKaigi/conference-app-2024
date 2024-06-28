import ComposableArchitecture
import SwiftUI

public struct StaffView: View {
    private let store: StoreOf<StaffReducer>

    public init(store: StoreOf<StaffReducer>) {
        self.store = store
    }

    public var body: some View {
        List(store.list) { data in
            Button(action: {
                // TODO: present GitHub profile page
            }, label: {
                StaffLabel(name: data.name, icon: data.icon)
            })
            .listRowSeparator(.hidden)
        }
        .listStyle(PlainListStyle())
        .onAppear {
            store.send(.onAppear)
        }
        .navigationBarTitleDisplayMode(.large)
        .navigationTitle("Staff")
    }
}

#Preview {
    StaffView(store: .init(initialState: .init(), reducer: { StaffReducer() }))
}
