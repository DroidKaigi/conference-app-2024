import ComposableArchitecture
import SwiftUI
import LicenseList

@ViewAction(for: AboutReducer.self)
public struct AboutView: View {
    @Bindable public var store: StoreOf<AboutReducer>
    
    var version: String {
        Bundle.main.object(forInfoDictionaryKey: "CFBundleShortVersionString") as? String ?? ""
    }

    public init(store: StoreOf<AboutReducer>) {
        self.store = store
    }

    public var body: some View {
        content
            .sheet(item: $store.scope(state: \.destination?.codeOfConduct, action: \.presentation.codeOfConduct), content: { _ in
                SafariView(url: .codeOfConduct)
                    .ignoresSafeArea()
            })
            .sheet(item: $store.scope(state: \.destination?.privacyPolicy, action: \.presentation.privacyPolicy), content: { _ in
                SafariView(url: .privacyPolicy)
                    .ignoresSafeArea()
            })
            .sheet(item: $store.scope(state: \.destination?.youtube, action: \.presentation.youtube), content: { _ in
                SafariView(url: .youtube)
                    .ignoresSafeArea()
            })
            .sheet(item: $store.scope(state: \.destination?.xcom, action: \.presentation.xcom), content: { _ in
                SafariView(url: .xcom)
                    .ignoresSafeArea()
            })
            .sheet(item: $store.scope(state: \.destination?.medium, action: \.presentation.medium), content: { _ in
                SafariView(url: .medium)
                    .ignoresSafeArea()
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
                
                HStack(spacing: 12) {
                    Button(action: {
                        send(.youtubeTapped)
                    }, label: {
                        Image(systemName: "play.circle")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .foregroundStyle(Color(.surfaceOnSurface))
                    })
                    .frame(width: 48, height: 48)

                    Button(action: {
                        send(.xcomTapped)
                    }, label: {
                        Image(systemName: "x.circle")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .foregroundStyle(Color(.surfaceOnSurface))
                    })
                    .frame(width: 48, height: 48)

                    Button(action: {
                        send(.mediumTapped)
                    }, label: {
                        Image(systemName: "m.circle")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .foregroundStyle(Color(.surfaceOnSurface))
                    })
                    .frame(width: 48, height: 48)
                }
                .padding(.vertical, 24)

                Text(String(localized: "AppVersion", bundle: .module))
                    .font(.body)
                    .foregroundStyle(Color(.surfaceOnSurface))
                    .padding(.bottom, 10)
                
                Text(version)
                    .font(.body)
                    .foregroundStyle(Color(.surfaceOnSurface))
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
