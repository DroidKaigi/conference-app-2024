import XCTest
import ComposableArchitecture
@testable import StaffFeature

final class StaffFeatureTests: XCTestCase {

    @MainActor
    func testOnAppear() async throws {
        let store = TestStore(initialState: StaffReducer.State()) {
            StaffReducer()
        } withDependencies: {
            $0.staffClient.streamStaffs = {
                AsyncThrowingStream {
                    $0.yield([
                        .init(
                            id: 0,
                            name: "testValue",
                            icon: URL(string: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4")!,
                            github: URL(string: "https://2024.droidkaigi.jp/")!
                        )
                    ])
                    $0.finish()
                }
            }
        }
        await store.send(.view(.onAppear))
        await store.receive(\.response.success) {
            $0.list = [.init(id: 0, name: "testValue", icon: .init(string: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4")!, github: .init(string: "https://2024.droidkaigi.jp/")!)]
        }
    }

}
