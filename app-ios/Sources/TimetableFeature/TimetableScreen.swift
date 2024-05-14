import ComposableArchitecture
import SwiftUI

public struct TimetableScreen: View {
    private let store: StoreOf<TimetableCore>

    public init(store: StoreOf<TimetableCore>) {
        self.store = store
    }

    public var body: some View {
        ForEach(store.timetableItems, id: \.timetableItem.id) { item in
            Text(item.timetableItem.title.jaTitle)
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
