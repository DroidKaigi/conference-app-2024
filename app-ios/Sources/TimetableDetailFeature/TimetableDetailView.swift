import SwiftUI
import ComposableArchitecture

public struct TimetableDetailView: View {
    private let store: StoreOf<TimetableDetailReducer>

    public var body: some View {
        Text(store.title)
            .onAppear {
                store.send(.onAppear)
            }
    }
    
    public init(store: StoreOf<TimetableDetailReducer>) {
        self.store = store
    }
}

#Preview {
    TimetableDetailView(
        store: .init(initialState: .init(title: "")) {
            TimetableDetailReducer()
        }
    )
}
