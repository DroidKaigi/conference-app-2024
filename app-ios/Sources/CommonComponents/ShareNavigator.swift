import UIKit

public struct ShareNavigator {
    public init() {}
    
    @MainActor
    public func shareTextWithImage(text: String, image: UIImage) {
        let items: [Any] = [text, image]
        let activityViewController = UIActivityViewController(activityItems: items, applicationActivities: nil)
        
        DispatchQueue.main.async {
            if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
               let keyWindow = windowScene.windows.first(where: { $0.isKeyWindow }),
               let rootViewController = keyWindow.rootViewController {
                rootViewController.present(activityViewController, animated: true, completion: nil)
            } else {
                print("Unable to find the root view controller.")
            }
        }
    }
}
