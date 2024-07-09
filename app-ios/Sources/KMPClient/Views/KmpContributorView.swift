import SwiftUI
@preconcurrency import shared

public struct KmpContributorView: View {
    private let repositories: any Repositories
    private let events: SkieSwiftMutableSharedFlow<any ContributorsScreenEvent>
    @State private var currentState: ContributorsUiState? = nil

    public init() {
        self.repositories = Container.shared.get(type: (any Repositories).self)

        self.events = SkieKotlinSharedFlowFactory<any ContributorsScreenEvent>()
            .createSkieKotlinSharedFlow(replay: 0, extraBufferCapacity: 0)
    }

    public var body: some View {
        VStack {
            if let state = currentState {
                Text("Current State: \(state.description)")
            } else {
                Text("Loading...")
            }
        }
        .task {
            await startListening()
        }
    }

    @MainActor
    private func startListening() async {
        let uiStateStateFlow = contributorScreenPresenterStateFlow(repositories: repositories.map, events: SkieSwiftFlow(events))

        for await state in uiStateStateFlow {
            self.currentState = state
        }
    }
}
