import XCTest
import ComposableArchitecture
@testable import TimetableDetailFeature

final class TimetableDetail_iosTests: XCTestCase {
    @MainActor func testExample() async throws {
        let store = TestStore(initialState: TimetableDetailReducer.State(title: "Test")) {
            TimetableDetailReducer()
        }
        await store.send(.onAppear) {
            $0.title = "Timetable Detail"
        }
    }
}
