import SafariServices
import SwiftUI

struct SafariView: UIViewControllerRepresentable {
    let url: URL

    func makeUIViewController(context: Context) -> some UIViewController {
        let configuration = SFSafariViewController.Configuration()
        configuration.barCollapsingEnabled = false
        return SFSafariViewController(
            url: url,
            configuration: configuration
        )
    }

    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) { }
}

#Preview {
    SafariView(
        url: URL(string: "https://2024.droidkaigi.jp/")!
    )
}

