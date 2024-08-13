import CommonComponents
import KMPClient
import Model
import SwiftUI
@preconcurrency import shared
import Theme

struct KmpPresenterContributorView: View {
    private let repositories: any Repositories
    private let events: SkieSwiftMutableSharedFlow<any ContributorsScreenEvent>
    @State private var currentState: ContributorsUiState? = nil
    @State private var showingUrl: IdentifiableURL?

    init() {
        self.repositories = Container.shared.get(type: (any Repositories).self)

        self.events = SkieKotlinSharedFlowFactory<any ContributorsScreenEvent>()
            .createSkieKotlinSharedFlow(replay: 0, extraBufferCapacity: 0)
    }

    var body: some View {
        Group {
            if let contributors = currentState.map(\.contributors) {
                ScrollView {
                    LazyVStack(spacing: 0) {
                        ForEach(contributors, id: \.id) { contributor in
                            ContributorListItemView(contributor: contributor) { url in
                                showingUrl = IdentifiableURL(url)
                            }
                        }
                    }
                }
            } else {
                ProgressView()
                    .task {
                        await startListening()
                    }
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(AssetColors.Surface.surface.swiftUIColor)
        .sheet(item: $showingUrl, content: { url in
            SafariView(url: url.id)
                .ignoresSafeArea()
        })
    }

    @MainActor
    private func startListening() async {
        let uiStateStateFlow = contributorsScreenPresenterStateFlow(repositories: repositories.map, events: SkieSwiftFlow(events))

        for await state in uiStateStateFlow {
            self.currentState = state
        }
    }
}
