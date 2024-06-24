import XCTest
import ComposableArchitecture
@testable import ContributorFeature

final class ContributorFeatureTests: XCTestCase {

    @MainActor
    func testExample() async throws {
        let store = TestStore(initialState: ContributorReducer.State(text: "HOGE")) {
            ContributorReducer()
        }
        await store.send(.onAppear) {
            $0.text = "Contributor Feature"
        }
    }

}
