import SwiftUI
import ComposableArchitecture
import Theme

struct SwiftUIContributorView: View {
    private let store: StoreOf<ContributorReducer>

    init(store: StoreOf<ContributorReducer>) {
        self.store = store
    }

    var body: some View {
        Group {
            if let contributors = store.contributors {
                ScrollView {
                    LazyVStack(spacing: 0) {
                        
                        ContributorsCountItem(totalContributor: contributors.count)
                            .frame(maxWidth: .infinity)
                            .padding(.horizontal, 16)
                            .padding(.vertical, 10)
                        
                        ForEach(contributors, id: \.id) { contributor in
                            ContributorListItemView(contributor: contributor) { url in
                                store.send(.view(.contributorButtonTapped(url)))
                            }
                        }
                    }
                }
            } else {
                ProgressView()
                    .task {
                        store.send(.onAppear)
                    }
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(AssetColors.Surface.surface.swiftUIColor)
    }
}

#Preview {
    SwiftUIContributorView(store: .init(initialState: .init(), reducer: { ContributorReducer() }))
}
