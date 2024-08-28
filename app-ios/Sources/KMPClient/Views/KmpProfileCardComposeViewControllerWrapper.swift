import SwiftUI
import shared

public struct KmpProfileCardComposeViewControllerWrapper: UIViewControllerRepresentable {
    public let repositories: any Repositories
    private let onClickShareProfileCard: (String, UIImage) -> Void
    
    public init(onClickShareProfileCard: @escaping (String, UIImage) -> Void) {
        self.repositories = Container.shared.get(type: (any Repositories).self)
        self.onClickShareProfileCard = onClickShareProfileCard
    }
    
    public func makeUIViewController(context: Context) -> UIViewController {
        return profileCardViewController(
            repositories: repositories,
            onClickShareProfileCard: onClickShareProfileCard
        )
    }
    
    public func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
