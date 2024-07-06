import ComposableArchitecture
import SwiftUI

public struct FavoriteScreen: View {
    private let store: StoreOf<FavoriteReducer>

    public init(store: StoreOf<FavoriteReducer>) {
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
    FavoriteScreen(
        store: .init(
            initialState: .init(),
            reducer: {}
        )
    )
}
