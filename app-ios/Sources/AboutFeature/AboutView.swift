import ComposableArchitecture
import SwiftUI
import LicenseList
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
            VStack(spacing: 0) {
                KeyVisual()
                    .padding(.top, 28)
                    .padding(.bottom, 32)
                
                VStack(alignment: .leading) {
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
                        .labelStyle(AboutLabelStyle())
                        Spacer()
                    })
                    .padding(.init(top: 24, leading: 12, bottom: 24, trailing: 16))
                    
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
                        .labelStyle(AboutLabelStyle())

                        Spacer()
                    })
                    .padding(.init(top: 24, leading: 12, bottom: 24, trailing: 16))

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
                        .labelStyle(AboutLabelStyle())
                        Spacer()
                    })
                    .padding(.init(top: 24, leading: 12, bottom: 24, trailing: 16))

                    Divider()
                        .background(AssetColors.Outline.outlineVariant.swiftUIColor)

                }
                .padding(.bottom, 32)

                VStack(alignment: .leading) {
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
                        .labelStyle(AboutLabelStyle())
                        Spacer()
                    })
                    .padding(.init(top: 24, leading: 12, bottom: 24, trailing: 16))

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
                        .labelStyle(AboutLabelStyle())
                        Spacer()
                    })
                    .padding(.init(top: 24, leading: 12, bottom: 24, trailing: 16))

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
                        .labelStyle(AboutLabelStyle())
                        Spacer()
                    })
                    .padding(.init(top: 24, leading: 12, bottom: 24, trailing: 16))

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
                        .labelStyle(AboutLabelStyle())
                        Spacer()
                    })
                    .padding(.init(top: 24, leading: 12, bottom: 24, trailing: 16))

                    Divider()
                        .background(AssetColors.Outline.outlineVariant.swiftUIColor)

                }
                
                HStack(spacing: 12) {
                    Button(action: {
                        send(.youtubeTapped)
                    }, label: {
                        Image(.youtubeSocialCircle)
                            .resizable()
                            .frame(width: 48, height: 48)
                    })
                    .frame(width: 48, height: 48)

                    Button(action: {
                        send(.xcomTapped)
                    }, label: {
                        Image(.xSocialCircle)
                            .resizable()
                            .frame(width: 48, height: 48)
                    })
                    .frame(width: 48, height: 48)

                    Button(action: {
                        send(.mediumTapped)
                    }, label: {
                        Image(.mediumSocialCircle)
                            .resizable()
                            .frame(width: 48, height: 48)
                    })
                    .frame(width: 48, height: 48)
                }
                .padding(.vertical, 24)

                Text(String(localized: "AppVersion", bundle: .module))
                    .textStyle(.titleSmall)
                    .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
                
                Text(version)
                    .textStyle(.titleSmall)
                    .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)
                    .padding(.bottom, 16)
            }
            .padding(.horizontal, 16)
            // bottom floating tabbar padding
            Color.clear.padding(.bottom, 60)
        }
        .background(AssetColors.Surface.surface.swiftUIColor)
    }

    struct AboutLabelStyle: LabelStyle {
        func makeBody(configuration: Configuration) -> some View {
            HStack(spacing: 12) {
                configuration.icon
                    .frame(width: 24, height: 24)
                configuration.title
            }
            .foregroundStyle(AssetColors.Primary.primaryFixed.swiftUIColor)
        }
    }
}

#Preview {
    AboutView(store: .init(initialState: .init(), reducer: { AboutReducer() }))
}
