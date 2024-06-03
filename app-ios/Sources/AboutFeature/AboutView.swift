import ComposableArchitecture
import SwiftUI

public struct AboutView: View {
    private let store: StoreOf<AboutReducer>

    public init(store: StoreOf<AboutReducer>) {
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
    AboutView(store: .init(initialState: .init(text: "Hoge"), reducer: { AboutReducer() }))
}
