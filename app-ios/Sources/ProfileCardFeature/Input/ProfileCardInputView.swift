import SwiftUI
import CommonComponents
import ComposableArchitecture
import Theme

public struct ProfileCardInputView: View {
    @Bindable private var store: StoreOf<ProfileCardInputReducer>

    public init(store: StoreOf<ProfileCardInputReducer>) {
        self.store = store
    }

    public var body: some View {
        ScrollView {
            VStack(spacing: 32) {
                Text("Let's create a profile card to introduce yourself at events or on social media!", bundle: .module)
                    .textStyle(.bodyLarge)
                    .foregroundStyle(AssetColors.Surface.onSurfaceVariant.swiftUIColor)

                ProfileCardInputTextField(
                    title: String(localized: "Nickname", bundle: .module),
                    text: .init(
                        get: {
                            store.nickname
                        }, set: {
                            store.send(.view(.nicknameChanged($0)))
                        }
                    )
                )

                ProfileCardInputTextField(
                    title: String(localized: "Occupation", bundle: .module),
                    text: .init(
                        get: {
                            store.occupation
                        }, set: {
                            store.send(.view(.occupationChanged($0)))
                        }
                    )
                )

                ProfileCardInputTextField(
                    title: String(localized: "Link（ex.X、Instagram...）", bundle: .module),
                    placeholder: "https://",
                    text: .init(
                        get: {
                            store.link
                        }, set: {
                            store.send(.view(.linkChanged($0)))
                        }
                    )
                )

                ProfileCardInputImage(
                    selectedPhoto: .init(
                        get: {
                            store.photo
                        },
                        set: {
                            store.send(.view(.photoChanged($0)))
                        }
                    ), 
                    title: String(localized: "Image", bundle: .module)
                )

                ProfileCardInputCardType(
                    selectedCardType: .init(
                        get: {
                            store.cardType
                        },
                        set: {
                            store.send(.view(.cardTypeChanged($0)))
                        }
                    )
                )

                Button {
                    store.send(.view(.createCardTapped))
                } label: {
                    Text("Create Card", bundle: .module)
                        .textStyle(.labelLarge)
                        .frame(maxWidth: .infinity, minHeight: 56, alignment: .center)
                        .foregroundStyle(AssetColors.Primary.onPrimary.swiftUIColor)
                        .background(AssetColors.Primary.primary.swiftUIColor)
                        .clipShape(Capsule())
                }
            }
        }
        .padding(.horizontal, 16)
        // bottom floating tabbar padding
        .padding(.bottom, 60)
        .background(AssetColors.Surface.surface.swiftUIColor)
        .navigationBarTitleDisplayMode(.large)
        .navigationTitle(String(localized: "Profile Card", bundle: .module))
        .onAppear { 
            store.send(.view(.onAppear))
        }
    }
}

#Preview {
    ProfileCardInputView(store: .init(initialState: .init(), reducer: {
        EmptyReducer()
    }))
}
