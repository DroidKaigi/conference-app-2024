import SwiftUI

#if canImport(App)
import App
import ComposableArchitecture
import Theme

final class AppDelegate: NSObject, UIApplicationDelegate {
    let store = Store(
        initialState: RootReducer.State(),
        reducer: { RootReducer() }
    )

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        store.send(.appDelegate(.didFinishLaunching))
        return true
    }
}

@main
struct DroidKaigi2024AppApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    init() { FontAssets.registerAllCustomFonts() }

    var body: some Scene {
        WindowGroup {
            RootView(store: appDelegate.store)
        }
    }
}

#elseif canImport(AppExperiments)
import AppExperiments

@main
struct DroidKaigi2024AppApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

#endif

