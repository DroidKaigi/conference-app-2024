import SwiftUI
@preconcurrency import shared

public struct KmpContributorComposeViewControllerWrapper: UIViewControllerRepresentable {
    public let repositories: any Repositories

    public init() {
        self.repositories = Container.shared.get(type: (any Repositories).self)
    }

    public func makeUIViewController(context: Context) -> UIViewController {
        return contributorViewController(
            repositories: repositories,
            onContributorItemClick: {_ in}
        )
    }

    public func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
