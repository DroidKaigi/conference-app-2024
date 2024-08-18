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
                
                makeSponsorGrid(columnCount: 1, items: store.platinums, imageHeight: 110)
                
                Text("GOLD SPONSORS")
                    .textStyle(.headlineSmall)
                    .foregroundStyle(AssetColors.Primary.primaryFixed.swiftUIColor)
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                makeSponsorGrid(columnCount: 2, items: store.golds, imageHeight: 77)
                
                Text("SUPPORTERS")
                    .textStyle(.headlineSmall)
                    .foregroundStyle(AssetColors.Primary.primaryFixed.swiftUIColor)
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                makeSponsorGrid(columnCount: 3, items: store.supporters, imageHeight: 77)
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

    @ViewBuilder
    func makeSponsorGrid(columnCount: Int, items: [SponsorData], imageHeight: Double) -> some View {
        let gridItems = (1...columnCount)
            .map { _ in
                GridItem(.flexible(), spacing: 12, alignment: .center)
            }
        LazyVGrid(columns: gridItems, spacing: 12) {
            ForEach(items) { item in
                Button {
                    selectedSponsorData = item
                } label: {
                    ZStack {
                        AssetColors.Inverse.inverseSurface.swiftUIColor
                            .clipShape(RoundedRectangle(cornerRadius: 12))
                        AsyncImage(url: item.logo) {
                            $0.image?
                                .resizable()
                                .scaledToFit()
                        }
                        .frame(height: imageHeight)
                        .padding(.vertical, 6)
                    }
                }
            }
        }
    }
}

#Preview {
    SponsorView(store: .init(initialState: .init(), reducer: { SponsorReducer() }))
}
