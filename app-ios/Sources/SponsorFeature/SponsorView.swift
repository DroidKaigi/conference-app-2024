import ComposableArchitecture
import SwiftUI
import Theme

public struct SponsorView: View {
    private let store: StoreOf<SponsorReducer>
    @State var selectedSponsorData: SponsorData?

    public init(store: StoreOf<SponsorReducer>) {
        self.store = store
    }

    public var body: some View {
        ScrollView {
            LazyVStack {
                ForEach(store.platinums, id: \.id) { platinum in
                    Button {
                        selectedSponsorData = platinum
                    } label: {
                        AsyncImage(url: platinum.logo) {
                            $0.image?
                                .resizable()
                                .scaledToFit()
                        }
                        .frame(height: 110)
                    }
//                    .padding(.vertical, 12)
                }
            }
            .padding(16)
        }
        .background(AssetColors.Surface.surface.swiftUIColor)
        .onAppear {
            store.send(.onAppear)
        }
        .navigationBarTitleDisplayMode(.large)
        .navigationTitle(String(localized: "Sponsor", bundle: .module))
    }
}

#Preview {
    SponsorView(store: .init(initialState: .init(), reducer: { SponsorReducer() }))
}
