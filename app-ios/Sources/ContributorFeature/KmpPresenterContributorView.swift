import CommonComponents
import KMPClient
import Model
import SwiftUI
@preconcurrency import shared
import Theme
import ComposableArchitecture

struct KmpPresenterContributorView: View {
    private let repositories: any Repositories
    private let events: SkieSwiftMutableSharedFlow<any ContributorsScreenEvent>
    private let onContributorButtonTapped: (URL) -> Void
    @State private var currentState: ContributorsUiState? = nil

    init(onContributorButtonTapped: @escaping (URL) -> Void) {
        @Dependency(\.containerClient) var containerClient

        self.repositories = containerClient.repositories()
        self.events = SkieKotlinSharedFlowFactory<any ContributorsScreenEvent>()
            .createSkieKotlinSharedFlow(replay: 0, extraBufferCapacity: 0)
        self.onContributorButtonTapped = onContributorButtonTapped
    }

    var body: some View {
        Group {
            if let state = currentState {
                if let existsState = state as? Exists {
                    ScrollView {
                        LazyVStack(spacing: 0) {
                            
                            ContributorsCountItem(totalContributor: existsState.contributors.count)
                                .frame(maxWidth: .infinity)
                                .padding(.horizontal, 16)
                                .padding(.vertical, 10)
                            
                            ForEach(existsState.contributors, id: \.id) { value in
                                let contributor = Model.Contributor(
                                    id: Int(value.id),
                                    userName: value.username,
                                    profileUrl: value.profileUrl.map { URL(string: $0)! },
                                    iconUrl: URL(string: value.iconUrl)!
                                )
                                ContributorListItemView(
                                    contributor: contributor,
                                    onContributorButtonTapped: onContributorButtonTapped
                                )
                            }
                        }
                    }
                } else if state is Loading {
                    ProgressView()
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
    }

    @MainActor
    private func startListening() async {
        let uiStateStateFlow = contributorsScreenPresenterStateFlow(repositories: repositories.map, events: SkieSwiftMutableSharedFlow(events))

        for await state in uiStateStateFlow {
            self.currentState = state
        }
    }
}
