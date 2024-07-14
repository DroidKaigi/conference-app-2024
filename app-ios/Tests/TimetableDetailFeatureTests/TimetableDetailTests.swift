import XCTest
import ComposableArchitecture
import shared
@testable import TimetableDetailFeature

final class TimetableDetail_iosTests: XCTestCase {
    @MainActor func testTappedBookmarkButton() async throws {
        let isBookmarked = false
        let store = TestStore(initialState: TimetableDetailReducer.State(timetableItem: TimetableItem.Session.companion.fake(), isBookmarked: isBookmarked)) {
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
        let store = TestStore(initialState: TimetableDetailReducer.State(timetableItem: TimetableItem.Session.companion.fake(), isBookmarked: false)) {
            TimetableDetailReducer()
        } withDependencies: {
            $0.timetableClient.toggleBookmark =  { @Sendable _ in throw TimetableDetailTestError.fail }
        }
        
        await store.send(.view(.bookmarkButtonTapped))
        await store.receive(\.bookmarkResponse)
    }
    
    @MainActor func testTappedURL() async throws {
        let store = TestStore(initialState: TimetableDetailReducer.State(timetableItem: TimetableItem.Session.companion.fake(), isBookmarked: false)) {
            TimetableDetailReducer()
        }
        let url = URL(string: "https://github.com/DroidKaigi/conference-app-2023")!
        await store.send(.view(.urlTapped(url))) {
            $0.tappedUrl = IdentifiableURL(url)
        }
    }
    
    @MainActor func testTappedCalendarButton() async throws {
        let store = TestStore(initialState: TimetableDetailReducer.State(timetableItem: TimetableItem.Session.companion.fake(), isBookmarked: false)) {
            TimetableDetailReducer()
        } withDependencies: {
            $0.eventKitClient.requestAccessIfNeeded =  { @Sendable in return true }
            $0.eventKitClient.addEvent =  { @Sendable _, _, _ in  }
        }
        
        await store.send(.view(.calendarButtonTapped))
        await store.receive(\.requestEventAccessResponse) {
            $0.confirmationDialog = ConfirmationDialogState(title: {
                TextState("")
            }, actions: {
                ButtonState(action: .addEvent) {
                    TextState(String(localized: "TimetableDetailAddEvent", bundle: .module))
                }
            })
        }
        
        await store.send(.confirmationDialog(.presented(.addEvent))) {
            $0.confirmationDialog = nil
        }
        await store.receive(\.addEventResponse)
    }
}

enum TimetableDetailTestError: Error {
    case fail
}
