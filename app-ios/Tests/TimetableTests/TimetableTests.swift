import XCTest
import ComposableArchitecture
@testable import TimetableFeature

final class TimetableTests: XCTestCase {
    @MainActor func testExample() async throws {
        let store = TestStore(initialState: TimetableReducer.State()) {
            TimetableReducer()
        }
        await store.send(.onAppear) {
            $0.timetableItems = SampleData.init().day1Data
        }
    }
}
