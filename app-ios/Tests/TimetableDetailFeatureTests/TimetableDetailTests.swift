import XCTest
import ComposableArchitecture
import shared
import Model
@testable import TimetableDetailFeature

final class TimetableDetail_iosTests: XCTestCase {
    @MainActor func testTappedFavoriteButton() async throws {
        let isFavorited = false
        let store = TestStore(initialState: TimetableDetailReducer.State(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: isFavorited)) {
            TimetableDetailReducer()
        } withDependencies: {
            $0.timetableClient.toggleBookmark =  { @Sendable _ in }
        }
        
        // add favorite
        await store.send(.view(.favoriteButtonTapped))
        await store.receive(\.favoriteResponse.success) {
            $0.isFavorited = !isFavorited
            $0.toast = .init(text: String(localized: "TimetableDetailAddBookmark", bundle: .module))
        }
        
        // remove favorite
        await store.send(.view(.favoriteButtonTapped))
        await store.receive(\.favoriteResponse.success) {
            $0.isFavorited = isFavorited
        }
        
    }
    
    @MainActor func testTappedBookmarkButtonFailed() async throws {
        let store = TestStore(initialState: TimetableDetailReducer.State(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false)) {
            TimetableDetailReducer()
        } withDependencies: {
            $0.timetableClient.toggleBookmark =  { @Sendable _ in throw TimetableDetailTestError.fail }
        }
        
        await store.send(.view(.favoriteButtonTapped))
        await store.receive(\.favoriteResponse.failure)
    }
    
    @MainActor func testTappedURL() async throws {
        let store = TestStore(initialState: TimetableDetailReducer.State(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false)) {
            TimetableDetailReducer()
        }
        let url = URL(string: "https://github.com/DroidKaigi/conference-app-2024")!
        await store.send(.view(.urlTapped(url))) {
            $0.url = IdentifiableURL(url)
        }
    }
    
    @MainActor func testTappedCalendarButton() async throws {
        let store = TestStore(initialState: TimetableDetailReducer.State(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false)) {
            TimetableDetailReducer()
        } withDependencies: {
            $0.eventKitClient.requestAccessIfNeeded =  { @Sendable in return true }
            $0.eventKitClient.addEvent =  { @Sendable _, _, _ in  }
        }
        
        await store.send(.view(.calendarButtonTapped))
        await store.receive(\.requestEventAccessResponse.success) {
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
        await store.receive(\.addEventResponse.success)
    }
    
    @MainActor func testTappedVideoButton() async throws {
        let store = TestStore(initialState: TimetableDetailReducer.State(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false)) {
            TimetableDetailReducer()
        }
        let videoUrl = URL(string: "https://www.youtube.com/watch?v=hFdKCyJ-Z9A")!
        await store.send(.view(.videoButtonTapped(videoUrl))) {
            $0.url = IdentifiableURL(videoUrl)
        }
    }
    
    @MainActor func testTappedSlideButton() async throws {
        let store = TestStore(initialState: TimetableDetailReducer.State(timetableItem: TimetableItem.Session.companion.fake(), isFavorited: false)) {
            TimetableDetailReducer()
        }
        let videoUrl = URL(string: "https://droidkaigi.jp/2021/")!
        await store.send(.view(.slideButtonTapped(videoUrl))) {
            $0.url = IdentifiableURL(videoUrl)
        }
    }
}

enum TimetableDetailTestError: Error {
    case fail
}
