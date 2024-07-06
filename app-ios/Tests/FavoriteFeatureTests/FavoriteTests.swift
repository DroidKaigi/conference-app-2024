import XCTest
import ComposableArchitecture
@testable import FavoriteFeature

final class FavoriteTests: XCTestCase {
    @MainActor func testExample() async throws {
        let store = TestStore(
            initialState: FavoriteReducer.State(text: "Test")
        ) {
            FavoriteReducer()
        }
        await store.send(.onAppear) {
            $0.text = "Favorite Feature"
        }
    }
}
