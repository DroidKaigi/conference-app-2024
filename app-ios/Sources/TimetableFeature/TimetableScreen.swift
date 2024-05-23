import ComposableArchitecture
import SwiftUI

public struct TimetableScreen: View {
    private let store: StoreOf<TimetableCore>

    public init(store: StoreOf<TimetableCore>) {
        self.store = store
    }

    public var body: some View {
        ForEach(store.timetableItems, id: \.self) { item in
            Text(item)
        }
    }
}

#Preview {
    TimetableScreen(
        store: .init(
            initialState: .init(),
            reducer: {}
        )
    )
}
