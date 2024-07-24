import ComposableArchitecture
import SwiftUI

public struct SponsorView: View {
    private let store: StoreOf<SponsorReducer>

    public init(store: StoreOf<SponsorReducer>) {
        self.store = store
    }

    public var body: some View {
        Text("HOGE")
            .onAppear {
                store.send(.onAppear)
            }
    }
}

#Preview {
    SponsorView(store: .init(initialState: .init(), reducer: { SponsorReducer() }))
}
