import XCTest
import ComposableArchitecture
@testable import TimetableDetailFeature

final class TimetableDetail_iosTests: XCTestCase {
    @MainActor func testExample() async throws {
        let store = TestStore(initialState: TimetableDetailReducer.State()) {
            TimetableDetailReducer()
        }
        
        await store.send(.favoriteButtonTapped) {
            $0.toast = .init(text: String(localized: "TimetableDetailAddBookmark", bundle: .module))
        }
        
    }
}
