import shared
import SwiftUI

public struct ContentView: View {
    public init() {}

    @State var selection = 1
    public var body: some View {
        
        TabView(selection: $selection) {
            VStack {
                Image(systemName: "globe")
                    .imageScale(.large)
                    .foregroundStyle(.tint)
                Text("Hello, world!")
            }
            .padding()
            .tabItem {
                Label(
                    title: { Text("Label") },
                    icon: { Image(systemName: "42.circle") }
                )
            }
            
            
            VStack {
                ContributorComposeViewControllerWrapper()
            }
            .padding()
            .tabItem {
                Label(
                    title: { Text("KMP") },
                    icon: { Image(systemName: "42.circle") }
                )
            }
        
        }
    }
}

struct ContributorComposeViewControllerWrapper: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let container = Container.shared
        let repositories: Repositories = container.get(type: Repositories.self)
        return DarwinContributorsKt.contributorViewController(
            repositories: repositories,
            onContributorItemClick: {_ in}
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}


#Preview {
    ContentView()
}
