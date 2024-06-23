import XCTest
import ComposableArchitecture
@testable import AboutFeature

final class AboutFeatureTests: XCTestCase {

    @MainActor
    func testTappedStaffs() async {
        let store = TestStore(initialState: AboutReducer.State()) {
            AboutReducer()
        }
        
        await store.send(\.view.staffsTapped) {
            $0.path[id: 0] = .staffs
        }
    }
    
    @MainActor
    func testTappedControbuters() async {
        let store = TestStore(initialState: AboutReducer.State()) {
            AboutReducer()
        }
        await store.send(\.view.contributersTapped) {
            $0.path[id: 0] = .contributers
        }
    }
    
    @MainActor
    func testTappedSponsors() async {
        let store = TestStore(initialState: AboutReducer.State()) {
            AboutReducer()
        }
        await store.send(\.view.sponsorsTapped) {
            $0.path[id: 0] = .sponsors
        }
    }
    
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
    func testTappedAcknowledgements() async {
        let store = TestStore(initialState: AboutReducer.State()) {
            AboutReducer()
        }
        await store.send(\.view.acknowledgementsTapped) {
            $0.path[id: 0] = .acknowledgements
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
