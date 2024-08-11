import XCTest
import ComposableArchitecture
@testable import EventMapFeature

final class EventMap_iosTests: XCTestCase {
    @MainActor func testFloorMapSelected() async throws {
        let store = TestStore(initialState: EventMapReducer.State()) {
            EventMapReducer()
        }
        
        await store.send(.view(.selectFloorMap(.firstBasement))) {
            $0.selectedFloorMap = .firstBasement
        }
    }
}
