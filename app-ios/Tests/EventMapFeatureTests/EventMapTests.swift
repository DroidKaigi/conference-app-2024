import XCTest
import ComposableArchitecture
import Model
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
    
    @MainActor func testMoreDetailButtonTapped() async throws {
        let store = TestStore(initialState: EventMapReducer.State()) {
            EventMapReducer()
        }
        let url = URL(string: "https://portal.droidkaigi.jp")!
        await store.send(.view(.moreDetailButtonTapped(url))) {
            $0.url = IdentifiableURL(url)
        }
    }
}
