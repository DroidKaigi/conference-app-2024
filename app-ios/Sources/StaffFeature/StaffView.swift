import ComposableArchitecture
import SwiftUI

public struct StaffView: View {
    private let store: StoreOf<StaffReducer>

    public init(store: StoreOf<StaffReducer>) {
        self.store = store
    }

    public var body: some View {
        Text(store.text)
            .onAppear {
                store.send(.onAppear)
            }
    }
}

#Preview {
    StaffView(store: .init(initialState: .init(text: "Hoge"), reducer: { StaffReducer() }))
}
