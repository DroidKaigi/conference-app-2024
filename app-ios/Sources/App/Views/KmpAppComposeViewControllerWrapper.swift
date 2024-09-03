import SwiftUI
import LicenseList
import shared
import ComposableArchitecture

@MainActor
public struct KmpAppComposeViewControllerWrapper: UIViewControllerRepresentable {
    @Dependency(\.containerClient) var containerClient

    public init() {}

    public func makeUIViewController(context: Context) -> UIViewController {
        IosComposeKaigiAppKt.kaigiAppController(
            repositories: containerClient.repositories(),
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
