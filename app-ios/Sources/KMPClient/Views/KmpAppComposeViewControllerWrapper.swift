import SwiftUI
import LicenseList
@preconcurrency import shared

public struct KmpAppComposeViewControllerWrapper: UIViewControllerRepresentable {
    public init() {}

    public func makeUIViewController(context: Context) -> UIViewController {
        let container = Container.shared
        let repositories: any Repositories = container.get(type: (any Repositories).self)
        return IosComposeKaigiAppKt.kaigiAppController(
            repositories: repositories,
            onLicenseScreenRequest: {
                openLicenseScreen()
            }
        )
    }

    public func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
    
    private func openLicenseScreen() {
        if let windowScene = UIApplication.shared.connectedScenes.first(where: { $0.activationState == .foregroundActive }) as? UIWindowScene {
            if let rootViewController = windowScene.windows.first?.rootViewController {
                let licenseView = LicenseListView()
                let hostingController = UIHostingController(rootView: licenseView)
                rootViewController.present(hostingController, animated: true, completion: nil)
            }
        }
    }
}
