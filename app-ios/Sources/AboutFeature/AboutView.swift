import ComposableArchitecture
import SwiftUI

@ViewAction(for: AboutReducer.self)
public struct AboutView: View {
    @Bindable public var store: StoreOf<AboutReducer>

    public init(store: StoreOf<AboutReducer>) {
        self.store = store
    }

    public var body: some View {
        NavigationStack(path: $store.scope(state: \.path, action: \.path)) {
            content
        } destination: { store in
            switch store.state {
            case .staffs:
                Text("Staffs")
            case .contributers:
                Text("Contributers")
            case .sponsors:
                Text("Sponsors")
            case .acknowledgements:
                Text("Acknowledgements")
            }
        }
        .sheet(item: $store.scope(state: \.destination?.codeOfConduct, action: \.presentation.codeOfConduct), content: { _ in
            Text("CodeOfConduct")
        })
        .sheet(item: $store.scope(state: \.destination?.privacyPolicy, action: \.presentation.privacyPolicy), content: { _ in
            Text("PrivacyPolicy")
        })
    }
    
    @ViewBuilder var content: some View {
        ScrollView {
            VStack(spacing: 0) {
                KeyVisual()
                    .padding(.top, 28)
                    .padding(.bottom, 32)
                
                VStack(alignment: .leading) {
                    Text("Credits")
                        .foregroundStyle(Color(.surfaceOnSurfaceVariant))
                        .font(.headline)

                    Button(action: {
                        send(.staffsTapped)
                    }, label: {
                        Label(
                            String(localized: "Staffs", bundle: .module),
                            systemImage: "face.smiling"
                        )
                        .labelStyle(AboutLabelStyle())
                        Spacer()
                    })
                    .padding(.init(top: 24, leading: 14, bottom: 24, trailing: 14))
                    
                    Divider()
                        .background(Color(.outlineOutlineVariant))

                    Button(action: {
                        send(.contributersTapped)
                    }, label: {
                        Label(
                            String(localized: "Contributers", bundle: .module),
                            systemImage: "person"
                        )
                        .labelStyle(AboutLabelStyle())
                        Spacer()
                    })
                    .padding(.init(top: 24, leading: 14, bottom: 24, trailing: 14))

                    Divider()
                        .background(Color(.outlineOutlineVariant))

                    Button(action: {
                        send(.sponsorsTapped)
                    }, label: {
                        Label(
                            String(localized: "Sponsors", bundle: .module),
                            systemImage: "building.2"
                        )
                        .labelStyle(AboutLabelStyle())
                        Spacer()
                    })
                    .padding(.init(top: 24, leading: 14, bottom: 24, trailing: 14))

                    Divider()
                        .background(Color(.outlineOutlineVariant))

                }
                .padding(.bottom, 32)

                VStack(alignment: .leading) {
                    Text("Others")
                        .foregroundStyle(Color(.surfaceOnSurfaceVariant))
                        .font(.headline)

                    Button(action: {
                        send(.codeOfConductTapped)
                    }, label: {
                        Label(
                            String(localized: "CodeOfConduct", bundle: .module),
                            systemImage: "apple.logo"
                        )
                        .labelStyle(AboutLabelStyle())
                        Spacer()
                    })
                    .padding(.init(top: 24, leading: 14, bottom: 24, trailing: 14))

                    Divider()
                        .background(Color(.outlineOutlineVariant))

                    Button(action: {
                        send(.acknowledgementsTapped)
                    }, label: {
                        Label(
                            String(localized: "Acknowledgements", bundle: .module),
                            systemImage: "doc.on.doc"
                        )
                        .labelStyle(AboutLabelStyle())
                        Spacer()
                    })
                    .padding(.init(top: 24, leading: 14, bottom: 24, trailing: 14))

                    Divider()
                        .background(Color(.outlineOutlineVariant))

                    Button(action: {
                        send(.privacyPolicyTapped)
                    }, label: {
                        Label(
                            String(localized: "PrivacyPolicy", bundle: .module),
                            systemImage: "lock.shield"
                        )
                        .labelStyle(AboutLabelStyle())
                        Spacer()
                    })
                    .padding(.init(top: 24, leading: 14, bottom: 24, trailing: 14))

                    Divider()
                        .background(Color(.outlineOutlineVariant))

                }

            }
            .padding(.horizontal, 16)
        }
        .background(Color(.background))
    }

    struct AboutLabelStyle: LabelStyle {
        func makeBody(configuration: Configuration) -> some View {
            HStack(spacing: 14) {
                configuration.icon
                    .font(.headline)
                configuration.title
                    .font(.body)
            }
            .foregroundStyle(Color(.surfaceOnSurface))
        }
    }
}

#Preview {
    AboutView(store: .init(initialState: .init(), reducer: { AboutReducer() }))
}
