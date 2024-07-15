import SwiftUI
@preconcurrency import shared

public struct KmpAppComposeViewControllerWrapper: UIViewControllerRepresentable {
    public init() {}

    public func makeUIViewController(context: Context) -> UIViewController {
        let container = Container.shared
        let repositories: any Repositories = container.get(type: (any Repositories).self)
        return IosComposeKaigiAppKt.kaigiAppController(
            repositories: repositories
        )
    }

    public func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
