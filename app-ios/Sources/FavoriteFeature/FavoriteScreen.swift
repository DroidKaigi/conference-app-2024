import ComposableArchitecture
import KMPClient
import SwiftUI

public struct FavoriteScreen: View {
    private let store: StoreOf<FavoriteReducer>

    public init(store: StoreOf<FavoriteReducer>) {
        self.store = store
    }

    public var body: some View {
        // Once, favorite as fullly kmp compose app
        KmpAppComposeViewControllerWrapper()
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
