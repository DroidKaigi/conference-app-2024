import XCTest
import ComposableArchitecture
@testable import AboutFeature

final class AboutFeatureTests: XCTestCase {

    @MainActor
    func testExample() async throws {
        let store = TestStore(initialState: AboutCore.State(text: "HOGE")) {
            AboutCore()
        }
        await store.send(.onAppear) {
            $0.text = "About Feature"
        }
    }

}
