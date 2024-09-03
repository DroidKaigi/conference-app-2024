import SwiftUI
import shared
import ComposableArchitecture

public struct KmpProfileCardComposeViewControllerWrapper: UIViewControllerRepresentable {
    @Dependency(\.containerClient) var containerClient

    public init() {}

    public func makeUIViewController(context: Context) -> UIViewController {
        profileCardViewController(
            repositories: containerClient.repositories(),
            onClickShareProfileCard: { image, text in
                let activityViewController = UIActivityViewController(activityItems: [text, image], applicationActivities: nil)
                if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
                   let keyWindow = windowScene.windows.first(where: { $0.isKeyWindow }),
                   let rootViewController = keyWindow.rootViewController {
                    rootViewController.present(activityViewController, animated: true, completion: nil)
                } else {
                    print("Unable to find the root view controller.")
                }
            }
        )
    }

    public func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
