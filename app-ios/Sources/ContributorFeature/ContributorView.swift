import ComposableArchitecture
import KMPClient
import SwiftUI

public struct ContributorView: View {
    private enum ViewType: String, CaseIterable {
        case swift
        case kmpPresenter
        case fullKmp

        var title: String {
            switch self {
            case .swift:
                "Swift"

            case .kmpPresenter:
                "KMP Presenter"

            case .fullKmp:
                "KMP Compose view"
            }
        }
    }

    @State private var viewType: ViewType = .swift

    private let store: StoreOf<ContributorReducer>

    public init(store: StoreOf<ContributorReducer>) {
        self.store = store
    }

    public var body: some View {
        Group {
            switch viewType {
            case .swift:
                Text(store.text)
                    .onAppear {
                        store.send(.onAppear)
                    }

            case .kmpPresenter:
                KmpContributorView()

            case .fullKmp:
                KmpContributorComposeViewControllerWrapper()
            }
        }
        .toolbar {
            ToolbarItem(placement: .principal) {
                Picker("", selection: $viewType) {
                    ForEach(ViewType.allCases, id: \.self) { segment in
                        Text(segment.rawValue)
                    }
                }
                .pickerStyle(.segmented)
            }
        }
    }
}

#Preview {
    ContributorView(store: .init(initialState: .init(text: "Hoge"), reducer: { ContributorReducer() }))
}
