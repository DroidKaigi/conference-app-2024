import XCTest
import ComposableArchitecture
import Model
@testable import ContributorFeature

final class ContributorFeatureTests: XCTestCase {

    @MainActor
    func testOnAppear() async throws {
        let store = TestStore(initialState: ContributorReducer.State()) {
            ContributorReducer()
        } withDependencies: {
            $0.contributorClient.refresh = {}
            $0.contributorClient.streamContributors = {
                AsyncThrowingStream {
                    $0.yield([
                        .init(id: 0, userName: "hoge", profileUrl: URL(string: "https://2024.droidkaigi.jp/"), iconUrl: URL(string: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4")!),
                        .init(id: 0, userName: "fuga", profileUrl: nil, iconUrl: URL(string: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4")!),
                    ])
                    $0.finish()
                }
            }
        }

        await store.send(.onAppear)
        await store.receive(\.response.success) {
            $0.contributors = [
                .init(id: 0, userName: "hoge", profileUrl: URL(string: "https://2024.droidkaigi.jp/"), iconUrl: URL(string: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4")!),
                .init(id: 0, userName: "fuga", profileUrl: nil, iconUrl: URL(string: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4")!),
            ]
        }
    }

    @MainActor
    func testOnAppearFail() async throws {
        let store = TestStore(initialState: ContributorReducer.State()) {
            ContributorReducer()
        } withDependencies: {
            $0.contributorClient.refresh = {
                throw ContributorTestError.fail
            }
            $0.contributorClient.streamContributors = {
                AsyncThrowingStream {
                    $0.finish()
                }
            }
        }

        await store.send(.onAppear)
        await store.receive(\.response.failure)
    }
    
    @MainActor
    func testContributorButtonTapped() async throws {
        let url = URL(string: "https://github.com/DroidKaigi/conference-app-2023")!
        let store = TestStore(initialState: ContributorReducer.State()) {
            ContributorReducer()
        }

        await store.send(.view(.contributorButtonTapped(url))) {
            $0.url = IdentifiableURL(url)
        }
    }
}

enum ContributorTestError: Error {
    case fail
}
