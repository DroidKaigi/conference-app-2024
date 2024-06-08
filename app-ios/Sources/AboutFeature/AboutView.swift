import ComposableArchitecture
import SwiftUI

public struct AboutView: View {
    private let store: StoreOf<AboutReducer>

    public init(store: StoreOf<AboutReducer>) {
        self.store = store
    }

    public var body: some View {
        VStack {
            KeyVisual()
                .padding(16)
            Spacer()
        }
        .background(Color(.background))
    }
}

#Preview {
    AboutView(store: .init(initialState: .init(text: "Hoge"), reducer: { AboutReducer() }))
        .environment(\.locale, Locale(identifier: "ja"))
}
