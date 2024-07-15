import SafariServices
import SwiftUI

public struct SafariView: UIViewControllerRepresentable {
    let url: URL

    public init(url: URL) {
        self.url = url
    }

    public func makeUIViewController(context: Context) -> some UIViewController {
        let configuration = SFSafariViewController.Configuration()
        configuration.barCollapsingEnabled = false
        return SFSafariViewController(
            url: url,
            configuration: configuration
        )
    }

    public func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) { }
}

#Preview {
    SafariView(
        url: URL(string: "https://2023.droidkaigi.jp/")!
    )
}

