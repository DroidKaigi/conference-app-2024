import SwiftUI

#if canImport(App)
import App

@main
struct DroidKaigi2024AppApp: App {
    var body: some Scene {
        WindowGroup {
            RootView(
                store: .init(
                    initialState: .init(),
                    reducer: { RootCore() }
                )
            )
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

