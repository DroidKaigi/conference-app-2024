import ComposableArchitecture
import KMPClient
import SwiftUI
import Theme
import shared
import CommonComponents

public struct ContributorView: View {
    private enum ViewType: String, CaseIterable {
        case swift
        case kmpPresenter
        case fullKmp

        var title: String {
            switch self {
            case .swift:
                "SwiftUI"

            case .kmpPresenter:
                "KMP Presenter"

            case .fullKmp:
                "KMP Compose view"
            }
        }
    }

    @State private var viewType: ViewType = .swift

    @Bindable var store: StoreOf<ContributorReducer>

    public init(store: StoreOf<ContributorReducer>) {
        self.store = store
    }

    public var body: some View {
        VStack(spacing: 0) {
            Picker("", selection: $viewType) {
                ForEach(ViewType.allCases, id: \.self) { segment in
                    Text(segment.title)
                }
            }
            .pickerStyle(.segmented)
            .padding(16)

            switch viewType {
            case .swift:
                SwiftUIContributorView(store: store)

            case .kmpPresenter:
                KmpPresenterContributorView()

            case .fullKmp:
                KmpContributorComposeViewControllerWrapper { urlString in
                    guard let url = URL(string: urlString) else {
                        return
                    }
                    store.send(.view(.contributorButtonTapped(url)))
                }
            }
        }
        .background(AssetColors.Surface.surface.swiftUIColor)
        .navigationTitle(String(localized: "Contributor", bundle: .module))
        .navigationBarTitleDisplayMode(.large)
        .sheet(item: $store.url, content: { url in
            SafariView(url: url.id)
                .ignoresSafeArea()
        })
    }
}

#Preview {
    ContributorView(store: .init(initialState: .init(), reducer: { ContributorReducer() }))
}
