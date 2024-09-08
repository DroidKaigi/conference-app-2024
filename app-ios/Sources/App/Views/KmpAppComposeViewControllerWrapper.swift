import SwiftUI
import LicenseList
import shared
import ComposableArchitecture

@MainActor
struct KmpAppComposeViewControllerWrapper: UIViewControllerRepresentable {
    @Dependency(\.containerClient) var containerClient

    init() {}

    func makeUIViewController(context: Context) -> UIViewController {
        IosComposeKaigiAppKt.kaigiAppController(
            repositories: containerClient.repositories(),
            onLicenseScreenRequest: {
                openLicenseScreen()
            }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
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
