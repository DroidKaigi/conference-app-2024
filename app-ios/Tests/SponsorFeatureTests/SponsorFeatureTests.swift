import XCTest
import ComposableArchitecture
import Model
@testable import SponsorFeature

final class SponsorFeatureTests: XCTestCase {

    @MainActor
    func testOnAppear() async throws {
        let store = TestStore(initialState: SponsorReducer.State()) {
            SponsorReducer()
        } withDependencies: {
            $0.sponsorsClient.streamSponsors = {
                AsyncThrowingStream {
                    $0.yield([
                        .init(id: "Hoge", logo: URL(string: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4")!, link: URL(string: "https://2024.droidkaigi.jp/")!, plan: .platinum),
                        .init(id: "Huga", logo: URL(string: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4")!, link: URL(string: "https://2024.droidkaigi.jp/")!, plan: .gold),
                        .init(id: "FUga", logo: URL(string: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4")!, link: URL(string: "https://2024.droidkaigi.jp/")!, plan: .supporter),
                    ])
                    $0.finish()
                }
            }
        }
        await store.send(.view(.onAppear))
        await store.receive(\.response.success) {
            $0.platinums = [
                .init(id: "Hoge", logo: .init(string: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4")!, link: .init(string: "https://2024.droidkaigi.jp/")!, plan: .platinum)
            ]
            $0.golds = [
                .init(id: "Huga", logo: .init(string: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4")!, link: .init(string: "https://2024.droidkaigi.jp/")!, plan: .gold)
            ]
            $0.supporters = [
                .init(id: "FUga", logo: .init(string: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4")!, link: .init(string: "https://2024.droidkaigi.jp/")!, plan: .supporter)
            ]
        }
    }

    @MainActor
    func testSponsorTapped() async throws {
        let url = URL(string: "https://github.com/DroidKaigi/conference-app-2024")!
        let store = TestStore(initialState: SponsorReducer.State()) {
            SponsorReducer()
        }
        await store.send(.view(.sponsorTapped(url))) {
            $0.url = IdentifiableURL(url)
        }
    }
}
