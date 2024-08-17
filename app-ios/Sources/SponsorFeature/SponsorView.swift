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
                Text("PLATINUM SPONSORS")
                    .textStyle(.headlineSmall)
                    .foregroundStyle(AssetColors.Primary.primaryFixed.swiftUIColor)
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                ForEach(store.platinums) { platinum in
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
                }
                
                Text("GOLD SPONSORS")
                    .textStyle(.headlineSmall)
                    .foregroundStyle(AssetColors.Primary.primaryFixed.swiftUIColor)
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                LazyVGrid(columns: Array(repeating: .init(.fixed(184)), count: 2)) {
                    ForEach(store.golds) { gold in
                        Button {
                            selectedSponsorData = gold
                        } label: {
                            AsyncImage(url: gold.logo) {
                                $0.image?
                                    .resizable()
                                    .scaledToFit()
                            }
                            .frame(height: 80)
                        }
                    }
                }
                
                Text("SUPPORTERS")
                    .textStyle(.headlineSmall)
                    .foregroundStyle(AssetColors.Primary.primaryFixed.swiftUIColor)
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                LazyVGrid(columns: Array(repeating: .init(.fixed(118)), count: 3)) {
                    ForEach(store.supporters) { supporter in
                        Button {
                            selectedSponsorData = supporter
                        } label: {
                            AsyncImage(url: supporter.logo) {
                                $0.image?
                                    .resizable()
                                    .scaledToFit()
                            }
                            .frame(height: 80)
                        }
                    }
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
