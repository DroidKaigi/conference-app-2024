import XCTest
import ComposableArchitecture
@testable import AboutFeature

final class AboutFeatureTests: XCTestCase {

    @MainActor
    func testExample() async throws {
        let store = TestStore(initialState: AboutReducer.State(text: "HOGE")) {
            AboutReducer()
        }
        await store.send(.onAppear) {
            $0.text = "About Feature"
        }
    }

}
