import XCTest
import ComposableArchitecture
@testable import SponsorFeature

final class SponsorFeatureTests: XCTestCase {

    @MainActor
    func testExample() async throws {
        let store = TestStore(initialState: SponsorReducer.State()) {
            SponsorReducer()
        } withDependencies: {
            $0.sponsorsClient.streamSponsors = {
                AsyncThrowingStream {
                    $0.yield([
                        .init(name: "Hoge", logo: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4", plan: .platinum, link: "https://2024.droidkaigi.jp/"),
                        .init(name: "Huga", logo: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4", plan: .gold, link: "https://2024.droidkaigi.jp/"),
                        .init(name: "FUga", logo: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4", plan: .supporter, link: "https://2024.droidkaigi.jp/"),
                    ])
                    $0.finish()
                }
            }
        }
        await store.send(.onAppear)
        await store.receive(\.response.success) {
            $0.platinums = [
                .init(id: "Hoge", logo: .init(string: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4")!, link: .init(string: "https://2024.droidkaigi.jp/")!)
            ]
            $0.golds = [
                .init(id: "Huga", logo: .init(string: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4")!, link: .init(string: "https://2024.droidkaigi.jp/")!)
            ]
            $0.supporters = [
                .init(id: "FUga", logo: .init(string: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4")!, link: .init(string: "https://2024.droidkaigi.jp/")!)
            ]
        }
    }

}
