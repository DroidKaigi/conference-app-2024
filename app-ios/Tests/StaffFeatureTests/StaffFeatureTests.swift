import XCTest
import ComposableArchitecture
@testable import StaffFeature

final class StaffFeatureTests: XCTestCase {

    @MainActor
    func testExample() async throws {
        let store = TestStore(initialState: StaffReducer.State(text: "HOGE")) {
            StaffReducer()
        }
        await store.send(.onAppear) {
            $0.text = "About Feature"
        }
    }

}
