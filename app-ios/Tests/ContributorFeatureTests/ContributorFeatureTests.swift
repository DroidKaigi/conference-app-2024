import XCTest
import ComposableArchitecture
import Model
@preconcurrency import shared
@testable import ContributorFeature

final class ContributorFeatureTests: XCTestCase {

    @MainActor
    func testOnAppear() async throws {
        let contributors = Contributor.companion.fakes()
        let store = TestStore(initialState: ContributorReducer.State()) {
            ContributorReducer()
        } withDependencies: {
            $0.contributorClient.refresh = {}
            $0.contributorClient.streamContributors = {
                AsyncThrowingStream {
                    $0.yield(contributors)
                    $0.finish()
                }
            }
        }

        await store.send(.onAppear)
        await store.receive(\.response) {
            $0.contributors = contributors
        }
    }

    @MainActor
    func testOnAppearFail() async throws {
        let contributors = Contributor.companion.fakes()
        let store = TestStore(initialState: ContributorReducer.State()) {
            ContributorReducer()
        } withDependencies: {
            $0.contributorClient.refresh = {
                throw ContributorTestError.fail
            }
            $0.contributorClient.streamContributors = {
                AsyncThrowingStream {
                    $0.yield(contributors)
                    $0.finish()
                }
            }
        }

        await store.send(.onAppear)
        await store.receive(\.response)
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
