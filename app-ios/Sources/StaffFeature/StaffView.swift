import ComposableArchitecture
import SwiftUI

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
                }
            }
            .padding(16)
        }
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
