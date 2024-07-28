import SwiftUI
import ComposableArchitecture
import Theme

struct SwiftUIContributorView: View {
    private let store: StoreOf<ContributorReducer>

    public init(store: StoreOf<ContributorReducer>) {
        self.store = store
    }

    var body: some View {
        Group {
            if let contributors = store.contributors {
                ScrollView {
                    LazyVStack(spacing: 0) {
                        ForEach(contributors, id: \.id) { contributor in
                            Button {
                                if let urlString = contributor.profileUrl,
                                   let url = URL(string: urlString) {
                                    store.send(.view(.contributorButtonTapped(url)))
                                }
                            } label: {
                                HStack(alignment: .center, spacing: 12) {
                                    AsyncImage(url: URL(string: contributor.iconUrl)) {
                                        $0.image?.resizable()
                                    }
                                    .frame(width: 52, height: 52)
                                    .clipShape(Circle())

                                    Text(contributor.username)
                                        .textStyle(.bodyLarge)
                                        .foregroundStyle(AssetColors.Surface.onSurface.swiftUIColor)
                                        .multilineTextAlignment(.leading)
                                        .lineLimit(2)

                                    Spacer()
                                    
                                }
                                .frame(maxWidth: .infinity)
                                .padding(.init(top: 12, leading: 16, bottom: 12, trailing: 16))
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
