import SwiftUI
import shared
import ComposableArchitecture

public struct KmpContributorComposeViewControllerWrapper: UIViewControllerRepresentable {
    public typealias URLString = String

    public let repositories: any Repositories
    private let onContributorsItemClick: (URLString) -> Void

    public init(onContributorsItemClick: @escaping (URLString) -> Void) {
        @Dependency(\.containerClient) var containerClient

        self.repositories = containerClient.repositories()
        self.onContributorsItemClick = onContributorsItemClick
    }

    public func makeUIViewController(context: Context) -> UIViewController {
        return contributorsViewController(
            repositories: repositories,
            onContributorsItemClick: onContributorsItemClick
        )
    }

    public func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
