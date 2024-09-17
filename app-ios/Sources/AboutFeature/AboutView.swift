import ComposableArchitecture
import SwiftUI
import Theme

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
            .navigationTitle(String(localized: "NavigationTitle", bundle: .module))
            .navigationBarTitleDisplayMode(.large)
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
            .alert($store.scope(state: \.destination?.alert, action: \.presentation.alert))
    }
    
    @ViewBuilder var content: some View {
        ScrollView {
            VStack(spacing: 32) {
                KeyVisual()
                    .padding(.top, 28)
                
                VStack(alignment: .leading, spacing: 0) {
                    Text("Credits")
                        .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
                        .textStyle(.titleMedium)

                    Button(action: {
                        send(.contributorsTapped)
                    }, label: {
                        Label {
                            Text(String(localized: "Contributers", bundle: .module))
                                .textStyle(.titleMedium)
                        } icon: {
                            Image(.icDiversity)
                        }
                    })
                    
                    Divider()
                        .background(AssetColors.Outline.outlineVariant.swiftUIColor)

                    Button(action: {
                        send(.staffsTapped)
                    }, label: {
                        Label {
                            Text(String(localized: "Staffs", bundle: .module))
                                .textStyle(.titleMedium)
                        } icon: {
                            Image(.icVerySatisfied)
                        }
                    })

                    Divider()
                        .background(AssetColors.Outline.outlineVariant.swiftUIColor)

                    Button(action: {
                        send(.sponsorsTapped)
                    }, label: {
                        Label {
                            Text(String(localized: "Sponsors", bundle: .module))
                                .textStyle(.titleMedium)
                        } icon: {
                            Image(.icApartment)
                        }
                    })

                    Divider()
                        .background(AssetColors.Outline.outlineVariant.swiftUIColor)

                }
                .buttonStyle(.about)

                VStack(alignment: .leading, spacing: 0) {
                    Text("Others")
                        .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
                        .textStyle(.titleMedium)

                    Button(action: {
                        send(.codeOfConductTapped)
                    }, label: {
                        Label {
                            Text(String(localized: "CodeOfConduct", bundle: .module))
                                .textStyle(.titleMedium)
                        } icon: {
                            Image(.icGavel)
                        }
                    })

                    Divider()
                        .background(AssetColors.Outline.outlineVariant.swiftUIColor)

                    Button(action: {
                        send(.acknowledgementsTapped)
                    }, label: {
                        Label {
                            Text(String(localized: "Acknowledgements", bundle: .module))
                                .textStyle(.titleMedium)
                        } icon: {
                            Image(.icFileCopy)
                        }
                    })

                    Divider()
                        .background(AssetColors.Outline.outlineVariant.swiftUIColor)

                    Button(action: {
                        send(.privacyPolicyTapped)
                    }, label: {
                        Label {
                            Text(String(localized: "PrivacyPolicy", bundle: .module))
                                .textStyle(.titleMedium)
                        } icon: {
                            Image(.icPrivacyTip)
                        }
                    })

                    Divider()
                        .background(AssetColors.Outline.outlineVariant.swiftUIColor)

                    Button(action: {
                        send(.switchComposeModeTapped)
                    }, label: {
                        Label {
                            Text(String(localized: "SwitchComposeMultiplatform", bundle: .module))
                                .textStyle(.titleMedium)
                        } icon: {
                            Image(systemName: "switch.2")
                                .resizable()
                                .frame(width: 18, height: 18)
                        }
                    })

                    Divider()
                        .background(AssetColors.Outline.outlineVariant.swiftUIColor)

                }
                .buttonStyle(.about)
                
                HStack(spacing: 12) {
                    Button(action: {
                        send(.youtubeTapped)
                    }, label: {
                        Image(.youtubeSocialCircle)
                            .resizable()
                    })

                    Button(action: {
                        send(.xcomTapped)
                    }, label: {
                        Image(.xSocialCircle)
                            .resizable()
                    })

                    Button(action: {
                        send(.mediumTapped)
                    }, label: {
                        Image(.mediumSocialCircle)
                            .resizable()
                    })
                }
                .buttonStyle(.social)

                VStack(spacing: 0) {
                    Text(String(localized: "AppVersion", bundle: .module))
                        .textStyle(.titleSmall)
                    
                    Text(version)
                        .textStyle(.titleSmall)
                }
                .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
                .padding(.bottom, 16)
            }
            .padding(.horizontal, 16)
            // bottom floating tabbar padding
            Color.clear.padding(.bottom, 60)
        }
        .background(AssetColors.Surface.surface.swiftUIColor)
    }
}

private extension LabelStyle where Self == AboutLabelStyle {
    static var about: AboutLabelStyle {
        .init()
    }
}

private struct AboutLabelStyle: LabelStyle {
    func makeBody(configuration: Configuration) -> some View {
        HStack(spacing: 12) {
            configuration.icon
                .frame(width: 24, height: 24)
            configuration.title
        }
        .foregroundStyle(AssetColors.Primary.primaryFixed.swiftUIColor)
    }
}

private extension ButtonStyle where Self == AboutListButtonStyle {
    static var about: AboutListButtonStyle {
        .init()
    }
}

private struct AboutListButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .labelStyle(.about)
            .padding(.vertical, 24)
            .padding(.leading, 12)
            .padding(.trailing, 16)
            .padding(.horizontal, 16)
            .frame(maxWidth: .infinity, alignment: .leading)
            .contentShape(Rectangle())
            .opacity(configuration.isPressed ? 0.3 : 1)
            .animation(.easeOut, value: configuration.isPressed)
            .padding(.horizontal, -16)
    }
}

private extension ButtonStyle where Self == SocialButtonStyle {
    static var social: SocialButtonStyle {
        .init()
    }
}

private struct SocialButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .frame(width: 48, height: 48)
            .opacity(configuration.isPressed ? 0.3 : 1)
            .animation(.easeOut, value: configuration.isPressed)
    }
}

#Preview {
    AboutView(store: .init(initialState: .init(), reducer: { AboutReducer() }))
}
