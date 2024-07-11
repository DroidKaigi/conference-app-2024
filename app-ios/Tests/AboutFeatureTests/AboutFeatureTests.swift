import XCTest
import ComposableArchitecture
@testable import AboutFeature

final class AboutFeatureTests: XCTestCase {

    @MainActor
    func testTappedCodeOfConduct() async {
        let store = TestStore(initialState: AboutReducer.State()) {
            AboutReducer()
        }
        await store.send(\.view.codeOfConductTapped) {
            $0.destination = .codeOfConduct
        }
    }

    @MainActor
    func testTappedPrivacyPolicy() async {
        let store = TestStore(initialState: AboutReducer.State()) {
            AboutReducer()
        }
        await store.send(\.view.privacyPolicyTapped) {
            $0.destination = .privacyPolicy
        }
    }
    
    @MainActor
    func testTappedYoutube() async {
        let store = TestStore(initialState: AboutReducer.State()) {
            AboutReducer()
        }
        await store.send(\.view.youtubeTapped) {
            $0.destination = .youtube
        }
    }

    @MainActor
    func testTappedXcom() async {
        let store = TestStore(initialState: AboutReducer.State()) {
            AboutReducer()
        }
        await store.send(\.view.xcomTapped) {
            $0.destination = .xcom
        }
    }

    @MainActor
    func testTappedMedium() async {
        let store = TestStore(initialState: AboutReducer.State()) {
            AboutReducer()
        }
        await store.send(\.view.mediumTapped) {
            $0.destination = .medium
        }
    }

}
