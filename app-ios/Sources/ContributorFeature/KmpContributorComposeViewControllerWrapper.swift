import SwiftUI
import shared
import ComposableArchitecture

struct KmpContributorComposeViewControllerWrapper: UIViewControllerRepresentable {
    public typealias URLString = String

    let repositories: any Repositories
    private let onContributorsItemClick: (URLString) -> Void

    init(onContributorsItemClick: @escaping (URLString) -> Void) {
        @Dependency(\.containerClient) var containerClient

        self.repositories = containerClient.repositories()
        self.onContributorsItemClick = onContributorsItemClick
    }

    func makeUIViewController(context: Context) -> UIViewController {
        return contributorsViewController(
            repositories: repositories,
            onContributorsItemClick: onContributorsItemClick
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
