import XCTest
import ComposableArchitecture
@testable import TimetableDetailFeature

final class TimetableDetail_iosTests: XCTestCase {
    @MainActor func testTappedBookmarkButton() async throws {
        let isBookmarked = false
        let store = TestStore(initialState: TimetableDetailReducer.State(isBookmarked: isBookmarked)) {
            TimetableDetailReducer()
        } withDependencies: {
            $0.timetableClient.toggleBookmark =  { @Sendable _ in }
        }
        
        // add bookmark
        await store.send(.view(.bookmarkButtonTapped))
        await store.receive(\.bookmarkResponse) {
            $0.isBookmarked = !isBookmarked
            $0.toast = .init(text: String(localized: "TimetableDetailAddBookmark", bundle: .module))
        }
        
        // remove bookmark
        await store.send(.view(.bookmarkButtonTapped))
        await store.receive(\.bookmarkResponse) {
            $0.isBookmarked = isBookmarked
        }
        
    }
    
    @MainActor func testTappedBookmarkButtonFailed() async throws {
        let store = TestStore(initialState: TimetableDetailReducer.State(isBookmarked: false)) {
            TimetableDetailReducer()
        } withDependencies: {
            $0.timetableClient.toggleBookmark =  { @Sendable _ in throw TimetableDetailTestError.fail }
        }
        
        await store.send(.view(.bookmarkButtonTapped))
        await store.receive(\.bookmarkResponse)
    }
}

enum TimetableDetailTestError: Error {
    case fail
}
