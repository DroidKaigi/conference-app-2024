import SwiftUI
import shared

public struct KmpContributorComposeViewControllerWrapper: UIViewControllerRepresentable {
    public typealias URLString = String

    public let repositories: any Repositories
    private let onContributorsItemClick: (URLString) -> Void

    public init(onContributorsItemClick: @escaping (URLString) -> Void) {
        self.repositories = Container.shared.get(type: (any Repositories).self)
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
