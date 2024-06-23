import XCTest
import ComposableArchitecture
@testable import SponsorFeature

final class SponsorFeatureTests: XCTestCase {

    @MainActor
    func testExample() async throws {
        let store = TestStore(initialState: SponsorReducer.State(text: "HOGE")) {
            SponsorReducer()
        }
        await store.send(.onAppear) {
            $0.text = "Sponsor Feature"
        }
    }

}
