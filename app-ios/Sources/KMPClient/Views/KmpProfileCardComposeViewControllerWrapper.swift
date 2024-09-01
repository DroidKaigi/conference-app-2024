import SwiftUI
import shared

public struct KmpProfileCardComposeViewControllerWrapper: UIViewControllerRepresentable {
    public init() {}
    
    public func makeUIViewController(context: Context) -> UIViewController {
        profileCardViewController(
            repositories: Container.shared.get(type: (any Repositories).self),
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
