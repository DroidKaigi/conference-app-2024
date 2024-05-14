import SwiftUI
import shared

struct ContributorsViewWithKmpPresenter: View {
    private let repositories: Repositories
    private let events: SkieSwiftMutableSharedFlow<ContributorsScreenEvent>
    @State private var currentState: ContributorsUiState? = nil

    init(repositories: Repositories) {
        self.repositories = repositories
        self.events = SkieKotlinSharedFlowFactory<ContributorsScreenEvent>()
            .createSkieKotlinSharedFlow(replay: 0, extraBufferCapacity: 0)
        
    }

    var body: some View {
        VStack {
            if let state = currentState {
                Text("Current State: \(state.description)")
            } else {
                Text("Loading...")
            }
        }
        .onAppear {
            startListening()
        }
    }
    
    private func startListening() {
        Task {
            let uiStateStateFlow =  contributorScreenPresenterStateFlow(repositories: repositories.map, events: SkieSwiftFlow(events))
            for await state in uiStateStateFlow
            {
                self.currentState = state
            }
        }
    }
}
