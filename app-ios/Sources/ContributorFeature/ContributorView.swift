import ComposableArchitecture
import SwiftUI

public struct ContributorView: View {
    private let store: StoreOf<ContributorReducer>

    public init(store: StoreOf<ContributorReducer>) {
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
    ContributorView(store: .init(initialState: .init(text: "Hoge"), reducer: { ContributorReducer() }))
}
